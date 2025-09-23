import request from "@/utils/request";

// 正本获取
export const getOriginMsg = (id) => {
  return request({
    url: "/app-api/biz/license/getOriginalById",
    method: "get",
    params: {
      id,
    },
  });
};
// 副本获取
export const getCopyMsg = (id) => {
  return request({
    url: "/app-api/biz/license/getDuplicateById",
    method: "get",
    params: {
      id,
    },
  });
};
