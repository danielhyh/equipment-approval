import config from "@/config.js";
import { useUserStore } from "@/pinia/modules/user.js";
import { useDictStore } from "@/pinia/modules/dict.js";
import { getSsoLoginUrl } from "@/apis/login.js";
import { getUrlValue } from "@/utils/tools.js";
import nProgress from "nprogress";
import "nprogress/nprogress.css";

// 路由白名单 - 不需要登录就能访问的路由
const whiteList = config.routeWhiteList;

// SSO请求状态 - 使用内存变量而非localStorage，避免持久化存储
let ssoRequesting = false;

/**
 * 处理SSO回调，从URL参数中提取token并设置到本地缓存
 */
const handleSsoCallback = async (userStore) => {
  const accessToken = getUrlValue('token') || getUrlValue('access_token');
  const refreshToken = getUrlValue('refresh_token');
  
  if (accessToken) {
    try {
      // 构建token数据
      const tokenData = {
        accessToken: accessToken,
        refreshToken: refreshToken || '',
        userId: parseInt(getUrlValue('user_id')) || 0,
        userType: parseInt(getUrlValue('user_type')) || 0,
        clientId: getUrlValue('client_id') || '',
        expiresTime: parseInt(getUrlValue('expires_time')) || 0,
        id: parseInt(getUrlValue('id')) || 0
      };
      
      userStore.setSsoToken(tokenData);
      
      // 清除Hash路由URL中的敏感参数，避免在地址栏中显示
      const currentUrl = window.location.href;
      if (currentUrl.includes('#') && currentUrl.includes('?')) {
        // 提取hash部分: 例如 #/home/index-page?token=xxx&user_id=1
        const hashIndex = currentUrl.indexOf('#');
        const baseUrl = currentUrl.substring(0, hashIndex); // http://domain.com/
        const hashPart = currentUrl.substring(hashIndex + 1); // /home/index-page?token=xxx
        
        // 分离路径和查询参数
        const queryIndex = hashPart.indexOf('?');
        const hashPath = hashPart.substring(0, queryIndex); // /home/index-page
        const queryString = hashPart.substring(queryIndex + 1); // token=xxx&user_id=1
        
        // 解析查询参数并移除敏感信息
        const params = new URLSearchParams(queryString);
        const sensitiveKeys = ['token', 'access_token', 'refresh_token', 'user_id', 'user_type', 'client_id', 'expires_time', 'id'];
        sensitiveKeys.forEach(key => params.delete(key));
        
        // 重新构建URL
        const cleanQueryString = params.toString();
        const cleanHash = cleanQueryString ? `${hashPath}?${cleanQueryString}` : hashPath;
        const cleanUrl = `${baseUrl}#${cleanHash}`;
        
        // 使用replaceState替换历史记录，不刷新页面
        window.history.replaceState(null, '', cleanUrl);
        console.log('已清除URL中的敏感参数');
      }
      
      return true;
    } catch (error) {
      console.error('SSO回调处理失败:', error);
      return false;
    }
  }
  return false;
};

export const beforeEach = async function (to, from, next) {
  let userStore = useUserStore();
  let dictStore = useDictStore();
  // 设置页面标题
  nProgress.start();
  document.title = to.meta?.title ? to.meta?.title + "-" + config.systemName : config.systemName;
  
  // 检查是否启用SSO
  const ENABLE_SSO = import.meta.env.VITE_ENABLE_SSO === 'true';
  
  // 首先检查是否为SSO回调，处理URL中的token（后端直接重定向到首页带token）
  if (ENABLE_SSO && (to.query.token || to.query.access_token)) {
    const ssoCallbackHandled = await handleSsoCallback(userStore);
    if (ssoCallbackHandled) {
      // SSO回调处理成功，继续正常的路由流程
      // 由于token已经设置，后续的getToken检查会通过
    }
  }
  
  // 获取token
  const token = userStore.getToken;
  // 如果有token，可以继续访问
  if (token) {
    // 如果已经登录且访问的是登录页，则重定向到首页
    if (to.path === "/login") {
      next({ path: "/" });
    } else {
      await userStore.setUser();
      await dictStore.setDictList();
      next();
    }
  } else {
    // 判断访问的路径是否在白名单中
    if (whiteList.includes(to.path)) {
      // 在白名单中，直接访问
      next();
    } else {
      if (ENABLE_SSO) {
        // 启用SSO时，不在白名单且无token，重定向到SSO登录
        if (!ssoRequesting) {
          try {
            ssoRequesting = true;
            const response = await getSsoLoginUrl();
            console.log('获取SSO登录地址成功:', response);
            if (response?.data) {
              // 重定向到SSO登录页面
              window.location.href = response.data;
              next(false); // 取消当前路由导航
              return; // 阻止后续执行
            } else {
              // 如果没有获取到SSO地址，回退到普通登录
              console.error('SSO登录地址为空，回退到普通登录');
              ssoRequesting = false;
              next({ path: "/login", query: { redirect: to.path } });
            }
          } catch (error) {
            console.error('获取SSO登录地址失败:', error);
            ssoRequesting = false;
            // SSO失败时，回退到普通登录
            next({ path: "/login", query: { redirect: to.path } });
          }
        } else {
          // 正在请求SSO，阻止路由跳转
          nProgress.done();
          next(false); // 取消当前路由导航
          return; // 阻止后续执行
        }
      } else {
        // 未启用SSO，使用普通登录
        next({ path: "/login", query: { redirect: to.path } });
      }
    }
  }
};

export const afterEach = function (to, from) {
  nProgress.done();
};
