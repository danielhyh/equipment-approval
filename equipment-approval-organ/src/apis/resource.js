import instance from "@/utils/request.js";

/**
 * 获取图书资源图片
 * @param {bookId,pageSize:页码} params
 * @returns
 */
export function getBookImg(bookId, pageSize) {
  return instance({
    url: `/web/library/getPdfPng/${bookId}`,
    method: "get",
    params: { pageSize },
  });
}

/**
 * 获取图书 基础信息
 * @param {bookId} params
 * @returns
 */
export function getBookBasis(bookId) {
  return instance({
    url: `/web/library/info/${bookId}`,
    method: "get",
  });
}
// 获取目录树
export function getCatalogTree(bookId) {
  return instance({
    url: `/web/library/tree/${bookId}`,
    method: "get",
  });
}

// 获取媒体详情
export function getMediaDetail(mediaId) {
  return instance({
    url: `/web/library/mediaInfo/${mediaId}`,
    method: "get",
  });
}

// 获取志载咸阳列表 {pageNum,pageSize,searchValue,category}
export function getarticleList(params) {
  return instance({
    url: `/web/search/essay`,
    method: "get",
    params,
  });
}

// 获取志载咸阳详情
export function getarticleDetail(articleId) {
  return instance({
    url: `/web/essay/infoById`,
    method: "get",
    params: { essayId: articleId },
  });
}

// 获取语音播报
export function getAudio(bookId, pageNum) {
  return instance({
    url: `/web/common/contentVoice`,
    method: "post",
    data: { bookId, pageNum },
  });
}
// 获取内容文字
export function getBookText(bookId, pageNum) {
  return instance({
    url: `/web/common/getContent`,
    method: "post",
    data: { bookId, pageNum },
  });
}
