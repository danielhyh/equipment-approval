import request from "@/utils/request";
// import SM4Util from "./tools/SM4Util.js";
// const APPSECRET = "SUFiL9@E4PCIjhGx";
// const sm4 = new SM4Util(APPSECRET);
// 账号密码登录 {username: "admin", password: "123456"}
export function accountLogin(data) {
  return request({
    url: "/app-api/user/auth/login",
    method: "post",
    data,
  });
}
// 手机号 验证码登录 {mobile: "*****", code: "123456"}
export function mobileLogin(data) {
  return request({
    url: "/admin-api/system/auth/sms-login",
    method: "post",
    data: data,
  });
}
// 发送登录验证码 {mobile: "*****",scene:1}
export function sendLoginCode(mobile) {
  return request({
    url: "/admin-api/system/auth/send-sms-code",
    method: "post",
    data: { mobile, scene: 1 },
  });
}

// 推出登录
export function loginOutSystem() {
  return request({
    url: "/app-api/user/auth/logout",
    method: "post",
  });
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: "/app-api/biz/institution/getUserInfo",
    method: "get",
  });
}

// 编辑用户信息
export function editUserInfo(data) {
  return request({
    url: "/app-api/biz/institution/update",
    method: "post",
    data,
  });
}

// 获取用户未读通知
export function getUnreadMsg() {
  return request({
    url: "/app-api/biz/notification/unread",
    method: "get",
  });
}
// 标记已读通知
export function markReadMsg(id) {
  return request({
    url: `/app-api/biz/notification/${id}/read`,
    method: "get",
  });
}

// 获取SSO单点登录URL
export function getSsoLoginUrl() {
  return request({
    url: "/app-api/sso/login-url",
    method: "get",
  });
}