import request from "@/utils/request";
import SM4Util from "./tools/SM4Util.js";
const APPSECRET = "SUFiL9@E4PCIjhGx";
const sm4 = new SM4Util(APPSECRET);
// 账号密码登录 {userName: "admin", password: "123456"}
export function accountLogin(data) {
  return request({
    url: "/web/login/noCodeLogin",
    method: "post",
    // params: data,
    data: { content: sm4.encryptData_ECB(JSON.stringify(data)) },
  });
}
// 手机号 验证码登录 {userName: "*****", code: "123456"}
export function mobileLogin(data) {
  return request({
    url: "/web/login/phoneLogin",
    method: "post",
    data: { content: sm4.encryptData_ECB(JSON.stringify(data)) },
  });
}
// 发送注册验证码 {userName: "*****"}
export function sendSmsCode(data) {
  return request({
    url: "/web/login/sendEnrollCode",
    method: "post",
    data: { content: sm4.encryptData_ECB(JSON.stringify(data)) },
  });
}
// 发送登录验证码 {userName: "*****"}
export function sendLoginCode(data) {
  return request({
    url: "/web/login/sendLoginCode",
    method: "post",
    data: { content: sm4.encryptData_ECB(JSON.stringify(data)) },
  });
}

// 校验用户是否存在
export function checkUserExist(phone) {
  return request({
    url: "/web/login/verifyWebUserExist",
    method: "get",
    params: { phone },
  });
}

/**
 * 获取微信二维码
 * @param {} data {ticket,expire_seconds,url}
 */
export function getWechatQRCode() {
  return request({
    url: "/web/vx/getQrCode",
    method: "get",
  });
}

/**
 * 微信登录
 * @param {ticket:二维码票据} data :{token":null,"enroll":true：注册，false：登录}
 */

export function wechatLogin(ticket) {
  return request({
    url: "/web/login/getVXLoginInfo",
    method: "get",
    params: { ticket },
  });
}
/**
 * 微信登录注册
 * @param {username,code：验证码} ,ticket:二维码票据 "data {token":null}
 */
export function wechatLoginEnroll(data, ticket) {
  return request({
    url: "/web/login/bindVx",
    method: "post",
    data: { content: sm4.encryptData_ECB(JSON.stringify(data)) },
    params: { ticket },
  });
}
