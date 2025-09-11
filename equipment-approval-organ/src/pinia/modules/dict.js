import { defineStore } from "pinia";
import { isJsonString, isEmptyObject } from "@/utils/tools";
import { getDict } from "@/apis/dic";
import { sessionGet, sessionSet, sessionRemove } from "@/utils/storage";

export const useDictStore = defineStore("dict", {
  state: () => ({
    dictList: isJsonString(sessionGet("dictCache")) ? JSON.parse(sessionGet("dictCache")) : {},
    initDICT: false,
  }),
  getters: {
    getDictAll(state) {
      return state.dictList;
    },
    getInitDICT(state) {
      return state.initDICT;
    },
  },
  actions: {
    async setDictList() {
      if (!isEmptyObject(this.dictList)) return;
      let { data } = await getDict();
      if (!Array.isArray(data)) return;
      for (let i = 0; i < data.length; i++) {
        if (this.dictList[data[i].dictType]) {
          this.dictList[data[i].dictType].push(data[i]);
        } else {
          this.dictList[data[i].dictType] = [data[i]];
        }
      }
      this.initDICT = true;
      sessionSet("dictCache", JSON.stringify(this.dictList));
    },
    getDictTypeList(dictType) {
      if (!this.dictList[dictType]) return [];
      return this.dictList[dictType];
    },
    resetDictCache() {
      this.removeDictCache();
      this.setDictList();
    },
    removeDictCache() {
      this.dictList = {};
      this.initDICT = false;
      sessionRemove("dictCache");
    },
  },
});
