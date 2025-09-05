import { defineStore } from "pinia";
import config from "@/config.js";
export const useSystemStore = defineStore("system", {
  state: () => ({
    // 系统名称
    sysName: "" || config.systemName,
    // 系统水印
    sysWatermark: "",
    // 系统版权信息
    sysCopyright: "" || config.copyright,
    // 系统基础信息
    sysBasisInfo: {
      
    },
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
    getSysBasisInfo(state) {
      return state.sysBasisInfo;
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
    setSysBasisInfo(basisInfo) {
      this.sysBasisInfo = basisInfo;
    },
  },
});
