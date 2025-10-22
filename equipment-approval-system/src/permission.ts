import router from './router'
import type { RouteRecordRaw } from 'vue-router'
import { isRelogin } from '@/config/axios/service'
import { getAccessToken, setToken } from '@/utils/auth'
import { useTitle } from '@/hooks/web/useTitle'
import { useNProgress } from '@/hooks/web/useNProgress'
import { usePageLoading } from '@/hooks/web/usePageLoading'
import { useDictStoreWithOut } from '@/store/modules/dict'
import { useUserStoreWithOut } from '@/store/modules/user'
import { usePermissionStoreWithOut } from '@/store/modules/permission'
import { getUrlValue } from '@/utils'
import type { TokenType } from '@/api/login/types'

const { start, done } = useNProgress()

const { loadStart, loadDone } = usePageLoading()

const parseURL = (
  url: string | null | undefined
): { basePath: string; paramsObject: { [key: string]: string } } => {
  // 如果输入为 null 或 undefined，返回空字符串和空对象
  if (url == null) {
    return { basePath: '', paramsObject: {} }
  }

  // 找到问号 (?) 的位置，它之前是基础路径，之后是查询参数
  const questionMarkIndex = url.indexOf('?')
  let basePath = url
  const paramsObject: { [key: string]: string } = {}

  // 如果找到了问号，说明有查询参数
  if (questionMarkIndex !== -1) {
    // 获取 basePath
    basePath = url.substring(0, questionMarkIndex)

    // 从 URL 中获取查询字符串部分
    const queryString = url.substring(questionMarkIndex + 1)

    // 使用 URLSearchParams 遍历参数
    const searchParams = new URLSearchParams(queryString)
    searchParams.forEach((value, key) => {
      // 封装进 paramsObject 对象
      paramsObject[key] = value
    })
  }

  // 返回 basePath 和 paramsObject
  return { basePath, paramsObject }
}

// 路由不重定向白名单
const whiteList = [
  '/login',
  '/social-login',
  '/auth-redirect',
  '/bind',
  '/register',
  '/oauthLogin/gitee'
]

/**
 * 处理SSO回调，从URL参数中提取token并设置到本地缓存
 */
const handleSsoCallback = async () => {
  const accessToken = getUrlValue('token', location.href) || getUrlValue('access_token', location.href)
  const refreshToken = getUrlValue('refresh_token', location.href)
  
  if (accessToken) {
    try {
      // 构建token数据，从URL参数中获取可用的信息
      const tokenData: TokenType = {
        accessToken: accessToken,
        refreshToken: refreshToken || '',
        userId: parseInt(getUrlValue('user_id', location.href)) || 0,
        userType: parseInt(getUrlValue('user_type', location.href)) || 0,
        clientId: getUrlValue('client_id', location.href) || '',
        expiresTime: parseInt(getUrlValue('expires_time', location.href)) || 0,
        id: parseInt(getUrlValue('id', location.href)) || 0
      }
      
      setToken(tokenData)
      
      // 清除URL中的敏感参数，避免在地址栏中显示
      const url = new URL(location.href)
      url.searchParams.delete('token')
      url.searchParams.delete('access_token')
      url.searchParams.delete('refresh_token')
      url.searchParams.delete('user_id')
      url.searchParams.delete('user_type')
      url.searchParams.delete('client_id')
      url.searchParams.delete('expires_time')
      url.searchParams.delete('id')
      window.history.replaceState({}, '', url.toString())
      
      return true
    } catch (error) {
      console.error('SSO回调处理失败:', error)
      return false
    }
  }
  return false
}

// 防止重复请求SSO登录URL

// 路由加载前
router.beforeEach(async (to, from, next) => {
  start()
  loadStart()
  const ENABLE_SSO = import.meta.env.VUE_APP_ENABLE_SSO === 'true'
  // 首先检查是否为SSO回调，处理URL中的token（后端直接重定向到首页带token）
  if (ENABLE_SSO  && (to.query.token || to.query.access_token)) {
    const ssoCallbackHandled = await handleSsoCallback()
    if (ssoCallbackHandled) {
      // SSO回调处理成功，继续正常的路由流程
      // 由于token已经设置，后续的getAccessToken()检查会通过
    }
  }

  if (getAccessToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      // 获取所有字典
      const dictStore = useDictStoreWithOut()
      const userStore = useUserStoreWithOut()
      const permissionStore = usePermissionStoreWithOut()
      if (!dictStore.getIsSetDict) {
        await dictStore.setDictMap()
      }
      if (!userStore.getIsSetUser) {
        isRelogin.show = true
        await userStore.setUserInfoAction()
        isRelogin.show = false
        // 后端过滤菜单
        await permissionStore.generateRoutes()
        permissionStore.getAddRouters.forEach((route) => {
          router.addRoute(route as unknown as RouteRecordRaw) // 动态添加可访问路由表
        })
        const redirectPath = from.query.redirect || to.path
        // 修复跳转时不带参数的问题
        const redirect = decodeURIComponent(redirectPath as string)
        const { paramsObject: query } = parseURL(redirect)
        const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect, query }
        next(nextData)
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (ENABLE_SSO) {
        // 检测到没有token，尝试获取SSO登录地址并重定向
        const ssoRequesting = localStorage.getItem('ssoRequesting')
        if (!ssoRequesting || ssoRequesting === '0') {

          try {
            // 使用正确的API函数获取SSO登录URL
            const response = await fetch('http://127.0.0.1:48080/sso/login-url')
            const res = await response.json()
            console.log('获取SSO登录地址成功:', res)
            if (res?.data) {
              localStorage.setItem('ssoRequesting', '1')
              window.location.href = res.data
            }
          } catch (error) {
            console.error('获取SSO登录地址失败:', error)
            localStorage.setItem('ssoRequesting', '0')
          }
        }
      }

      //
      // // 如果SSO URL获取失败或为空，回退到普通登录
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
    }
  }
})

router.afterEach((to) => {
  useTitle(to?.meta?.title as string)
  done() // 结束Progress
  loadDone()
})
