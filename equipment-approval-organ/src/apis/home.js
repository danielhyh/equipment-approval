import instance from "@/utils/request.js";

// 获取首页办理列表
export function getApplyList() {
  return instance({
    url: "/app-api/biz/application/list",
    method: "get",
    params: { pageNo: 1, pageSize: 100 },
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
