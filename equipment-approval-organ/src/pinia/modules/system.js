import { defineStore } from "pinia";
export const useSystemStore = defineStore("system", {
  state: () => ({
    // 系统名称
    sysName: "",
    // 系统水印
    sysWatermark: "",
    // 系统版权信息
    sysCopyright: "",
  }),
  getters: {
    getSysName(state) {
      return state.sysName;
    },
    getSysWatermark(state) {
      return state.sysWatermark;
    },
    getSysCopyright(state) {
      return state.sysCopyright;
    },
  },
  actions: {
    setSysName(name) {
      this.sysName = name;
    },
    setSysWatermark(watermark) {
      this.sysWatermark = watermark;
    },
    setSysCopyright(copyright) {
      this.sysCopyright = copyright;
    },
  },
});
