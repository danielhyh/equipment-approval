import { defineStore } from "pinia";
import { isJsonString, isEmptyObject } from "@/utils/tools";
import { getDict } from "@/apis/dic";
import { sessionGet, sessionSet, sessionRemove } from "@/utils/storage";

let _dictPromise = null; // 请求锁，防止并发重复请求

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
      // 如果已有请求在进行中，等待同一个 Promise，避免重复请求
      if (_dictPromise) return _dictPromise;
      _dictPromise = this._fetchDict();
      try {
        await _dictPromise;
      } finally {
        _dictPromise = null;
      }
    },
    async _fetchDict() {
      try {
        let { data } = await getDict();
        if (!Array.isArray(data)) return;
        // 先构建临时对象，再一次性赋值，避免中间状态被其他调用读取
        const temp = {};
        for (let i = 0; i < data.length; i++) {
          if (temp[data[i].dictType]) {
            temp[data[i].dictType].push(data[i]);
          } else {
            temp[data[i].dictType] = [data[i]];
          }
        }
        this.dictList = temp;
        this.initDICT = true;
        sessionSet("dictCache", JSON.stringify(this.dictList));
      } catch (err) {
        console.log(err, "字典获取失败");
      }
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
