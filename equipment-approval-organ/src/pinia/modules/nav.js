import { defineStore } from "pinia";
import { getImageUrl } from "@/utils/tools.js";
export const useNavStore = defineStore("nav", {
  state: () => ({
    navList: [
      // todo 需要name和后台管理系统 类型名称一致
      {
        name: "志书",
        value: "1",
        englishName: "local histories",
        typeId: "",
        type: "book",
        url: getImageUrl("gui/z-s.png"),
      },
      {
        name: "年鉴",
        value: "2",
        typeId: "",
        englishName: "year book",
        type: "book",
        url: getImageUrl("gui/n-j.png"),
      },
      {
        name: "地情",
        value: "3",
        typeId: "",
        englishName: "local situation",
        type: "book",
        url: getImageUrl("gui/dqcs.png"),
      },
      {
        name: "旧志",
        value: "4",
        typeId: "",
        englishName: "old records",
        type: "book",
        url: getImageUrl("gui/jz.png"),
      },
      {
        name: "多媒体",
        value: "7",
        typeId: "",
        englishName: "multimedia",
        type: "media",
        url: getImageUrl("gui/dmt.png"),
      },
      {
        name: "志载咸阳",
        value: "8",
        typeId: "",
        englishName: "chronicles of xinyang",
        type: "article",
        url: getImageUrl("gui/zzxy.png"),
      },
    ],
    activeNav: null || localStorage.getItem("activeNav"),
  }),
  getters: {
    getNavList(state) {
      return state.navList;
    },
    getActiveNav(state) {
      return state.activeNav;
    },
    getActiveNavItem(state) {
      let obj = state.navList.find((item) => item.value === state.activeNav);
      return obj;
    },
  },
  actions: {
    setActiveNav(value) {
      this.activeNav = value;
      localStorage.setItem("activeNav", value);
    },
    setNavType(name, typeId) {
      let Index = this.navList.findIndex((item) => name.includes(item.name));
      if (Index > -1) this.navList[Index].typeId = typeId;
    },
  },
});
