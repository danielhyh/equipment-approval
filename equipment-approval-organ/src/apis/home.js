import instance, { get } from "@/utils/request.js";

// 获取首页办理列表
export function getApplyList(params) {
  return instance({
    url: "/app-api/biz/application/list",
    method: "get",
    params: params,
  });
}
// 证书列表 {pageSize,pageNum,type}
export function getLicenseList(params) {
  return instance({
    url: "/app-api/biz/license/page",
    method: "get",
    params,
  });
}
// 证书列表正本信息查询
export function getLicenseOrigin(id) {
  return get("/app-api/biz/license/getOriginalById", { id });
}
// 证书列表副本信息查询
export function getLicenseCopy(id) {
  return get("/app-api/biz/license/getDuplicateById", { id });
}
/**
 * 副本、其他信息提交
 * @param {
 * originalId：正本ID
 * productionEnterprise：生产企业 string
 * infoSubmitDate:信息报送时间
 * specificModel：具体型号 string
 * duplicateIssuingAuthority:副本发证机关
 * productSerialNo:产品序列号 string
 * duplicateIssueDate:副本发证日期
 * installationDate:装机日期
 * purchasePrice：采购价格
 * specialDescription：设备特殊说明
 * equipmentUsers：设备使用人员 JSON格式 JSON.stringify 列表
 * remark：备注
 * } data
 * @returns
 */
export function submitCopy(data) {
  return instance({
    url: "/app-api/biz/license/duplicate-submit",
    method: "post",
    data,
  });
}

// 验收资料上传
export function createFilesMaterial(data){
  return instance({
    url: "/app-api/biz/acceptance-material/create",
    method: "post",
    data,
  });
}
// 获取验收资料列表 {applicationId}
export function getFilesMaterialList(params){
  return instance({
    url: "/app-api/biz/acceptance-material/list",
    method: "get",
    params,
  });
}