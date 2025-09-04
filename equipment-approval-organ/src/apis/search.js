import instance from "@/utils/request.js";

/**
 * 书籍查询
 * @param {category:分类,labelId:标签,region:区域,searchValue:文本,pageNum,pageSize} params
 * @returns
 */
export function getBookList(params) {
  return instance({
    url: "/web/search/book",
    method: "get",
    params,
  });
}

/**
 * 全文搜索
 * @param {category:分类,labelId:标签,region:区域,searchValue:文本,pageNum,pageSize} params
 * @returns
 */
export function getAllValue(params) {
  return instance({
    url: "/web/search/content",
    method: "get",
    params,
  });
}
/**
 * 多媒体查询
 * @param {category:分类,labelId:标签,region:区域,searchValue:文本,pageNum,pageSize} params
 * @returns
 */
export function getMediaList(params) {
  return instance({
    url: "/web/search/media",
    method: "get",
    params,
  });
}
