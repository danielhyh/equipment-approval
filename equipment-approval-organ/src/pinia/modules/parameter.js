import { defineStore } from "pinia";
// 筛选 标签 地区 数据集合
export const useParameterStore = defineStore("parameter", {
  state: () => ({
    TagsTree: [],
    areaTree: [],
  }),
  getters: {
    getTagsTree(state) {
      return state.TagsTree;
    },
    getAreaTree(state) {
      return state.areaTree;
    },
  },
  actions: {
    setTagsTree(value) {
      this.TagsTree = value;
    },
    setAreaTree(value) {
      this.areaTree = value;
    },
  },
});
