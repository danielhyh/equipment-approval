import instance from "@/utils/request.js";

// 获取首页分类统计
export function getHomeCount() {
  return instance({
    url: "/web/homePage/category/statics",
    method: "get",
  });
}