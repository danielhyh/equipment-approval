import config from "@/config.js";
import { useUserStore } from "@/pinia/modules/user.js";
import nProgress from "nprogress";
import "nprogress/nprogress.css";

// 路由白名单 - 不需要登录就能访问的路由
const whiteList = config.routeWhiteList;

export const beforeEach = async function (to, from, next) {
  let useStore = useUserStore();
  // 设置页面标题
  nProgress.start();
  document.title = to.meta?.title || "陕西省大型医用设备在线审批归档系统";

  // 获取token
  const token = useStore.getToken;
  console.log(token);

  // 如果有token，可以继续访问
  if (token) {
    // 如果已经登录且访问的是登录页，则重定向到首页
    if (to.path === "/login") {
    //   next({ path: "/" });
    next()
    } else {
      next();
    }
  } else {
    // 如果没有token
    // 判断访问的路径是否在白名单中
    if (whiteList.includes(to.path)) {
      // 在白名单中，直接访问
      next();
    } else {
      // 不在白名单中，重定向到登录页
      next({ path: "/login", query: { redirect: to.path } });
    }
  }
};

export const afterEach = function (to, from) {
  nProgress.done();
};
