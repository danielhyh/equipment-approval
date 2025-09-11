import request from "@/utils/request";
// import SM4Util from "./tools/SM4Util.js";
// const APPSECRET = "SUFiL9@E4PCIjhGx";
// const sm4 = new SM4Util(APPSECRET);
// 账号密码登录 {username: "admin", password: "123456"}
export function accountLogin(data) {
  return request({
    url: "/admin-api/system/auth/login",
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

// 获取用户信息
export function getUserInfo() {
  return request({
    url: "/app-api/biz/institution/getUserInfo",
    method: "get",
  });
}
