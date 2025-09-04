import axios from "axios";
import router from "../router/index.js";
// import { useUserStore } from '../store/modules/user.js';
import { ElMessage } from "element-plus";
// 创建请求实例
const instance = axios.create({
  baseURL: import.meta.env.VITE_API,
  // 指定请求超时的毫秒数
  timeout: 120000,
  // 表示跨域请求时是否需要使用凭证
  withCredentials: false,
});
// 白名单接口
let noNeedTokenReq = ["/noCodeLogin2", "/qr/scan"];
// 前置拦截器（发起请求之前的拦截）
instance.interceptors.request.use(
  (config) => {
    let { customHeader } = config.headers;

    /**
     * 在这里一般会携带前台的参数发送给后台，比如下面这段代码：
     * const token = getToken()
     * if (token) {
     *  config.headers.token = token
     * }
     */
    if (config.data instanceof FormData) {
      config.headers = {
        "Content-Type": "multipart/form-data",
        authorization: localStorage.getItem("token"),
      };
    } else {
      config.data = JSON.stringify(config.data);
      config.headers = {
        "Content-Type": "application/json",
      };
    }

    if (!noNeedTokenReq.includes(config.url)) {
      // config.headers.authorization = localStorage.getItem('token')
    }
    if (customHeader && Object.keys(customHeader).length) {
      config.headers = { ...config.headers, ...customHeader };
    }

    if (config.method === "get" && config.params) {
      let url = config.url + "?";
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName];
        const part = encodeURIComponent(propName) + "=";
        if (value !== null && typeof value !== "undefined") {
          if (typeof value === "object") {
            for (const key of Object.keys(value)) {
              let params = propName + "[" + key + "]";
              const subPart = encodeURIComponent(params) + "=";
              url += subPart + encodeURIComponent(value[key]) + "&";
            }
          } else {
            url += part + encodeURIComponent(value) + "&";
          }
        }
      }
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 后置拦截器（获取到响应时的拦截）
instance.interceptors.response.use(
  (response) => {
    /**
     * 根据你的项目实际情况来对 response 和 error 做处理
     * 这里对 response 和 error 不做任何处理，直接返回
     */
    // let store = useUserStore()

    if (!response.data.code && response.data) {
      // 图书资源返回
      return response.data;
    }
    if (response.data.code !== 200) {
      if (response.data.code === 401) {
        // store.setToken({ token: '' })
        ElMessage({ type: "warning", message: "登录信息过期\n请重新登录" });
        // router.push("/login");
      }
      ElMessage({ type: "warning", message: response.data.msg || "请求异常" });
      return Promise.reject(response.data);
    }

    return response.data;
  },
  (error) => {
    const { response, message, code } = error;
    if (code === "ECONNABORTED" || !response || error.status === 404) {
      ElMessage({ type: "warning", message: message });
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

// 导出常用函数

/**
 * @param {string} url
 * @param {object} data
 * @param {object} params
 */
export const post = (url, data = {}, params = {}) => {
  return instance({
    method: "post",
    url,
    data,
    params,
  });
};

/**
 * @param {string} url
 * @param {object} params
 */
export const get = (url, params) => {
  return instance({
    method: "get",
    url,
    params,
  });
};

// /**
//  * @param {string} url
//  * @param {object} data
//  * @param {object} params
//  */
export const put = (url, data = {}, params = {}) => {
  return instance({
    method: "put",
    url,
    params,
    data,
  });
};

// /**
//  * @param {string} url
//  * @param {object} params
//  */
export const _delete = (url, params = {}) => {
  return instance({
    method: "delete",
    url,
    params,
  });
};

export default instance;
