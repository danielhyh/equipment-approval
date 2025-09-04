import { defineStore } from "pinia";

// 判断是否是 JSON 字符串
function isJsonString(str) {
  try {
    JSON.parse(str);
    return true;
  } catch (e) {
    return false;
  }
}
export const useSearchStore = defineStore("search", {
  state: () => ({
    allSearch: !isJsonString(localStorage.getItem("allSearch")) ? {} : JSON.parse(localStorage.getItem("allSearch")),
    pageSearch: !isJsonString(localStorage.getItem("pageSearch")) ? {} : JSON.parse(localStorage.getItem("pageSearch")),
  }),
  getters: {
    getAllSearch(state) {
      return state.allSearch;
    },
    getPageSearch(state) {
      return state.pageSearch;
    },
  },
  actions: {
    setAllSearch(params) {
      this.allSearch = params || {};
      localStorage.setItem("allSearch", JSON.stringify(params));
    },
    setPageSearch(params) {
      this.pageSearch = params || {};
      localStorage.setItem("pageSearch", JSON.stringify(params));
    },
  },
});
