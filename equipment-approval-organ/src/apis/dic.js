import instance from "@/utils/request.js";

// 获取子级分类  {topBanner:1,parentId:0}：顶级分类, {topBanner:0,parentId:parentId}：子级分类
export function getSubCategory(parentId, topBanner) {
  return instance({
    url: `/web/common/findCategoryByParentId/${parentId}`,
    params: {
      topBanner,
    },
  });
}
// 分类所有子级树
export function getSubCategoryTree(parentId) {
  return instance({
    url: `/web/common/findCategoryTreeByParentId/${parentId}`,
    method: "get",
  });
}
// 获取所有区域
export function getArea() {
  return instance({
    url: `/web/common/findRegion`,
    method: "get",
  });
}
// 获取标签树列表
export function getTagList() {
  return instance({
    url: `/web/common/findAllLabels`,
    method: "get",
  });
}

// 获取轮次
export function getRounds() {
  return instance({
    url: `/web/common/findRounds`,
    method: "get",
  });
}
// 获取系统配置信息
export function getSysConfig() {
  return instance({
    url: `/out/sysConfig/list`,
    method: "get",
  });
}
