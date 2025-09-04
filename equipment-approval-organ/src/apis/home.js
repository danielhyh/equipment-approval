import instance from "@/utils/request.js";

// 获取首页分类统计
export function getHomeCount() {
  return instance({
    url: "/web/homePage/category/statics",
    method: "get",
  });
}
// 热门推荐
export function getHotRecommend() {
  return instance({
    url: "/web/homePage/findLibraryHotTop",
    method: "get",
  });
}
// 数据资源统计
export function getStatics() {
  return instance({
    url: "/web/homePage/findLibraryStatics",
    method: "get",
  });
}
// 最新入库 Top5
export function getNewTop5() {
  return instance({
    url: "/web/homePage/findLibraryStorageTop",
    method: "get",
  });
}
// 多媒体陈展
export function getMultimedia(typeId) {
  return instance({
    url: "/web/homePage/findMediaInfo",
    method: "get",
    params: { mediaType: typeId },
  });
}
