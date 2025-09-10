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