import { markRaw } from "vue";
// 正本信息
import origin from "./origin.vue";
// 副本信息
import copy from "./copy.vue";
// 其他信息
// 其他信息 回显
// 补充资料
// 二维码
// 操作记录
const copyComList = [
  { key: "originMsg", label: "正本信息", component: markRaw(origin), tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: markRaw(copy), tag: true, icon: "icon-park-solid:copy" },
  { key: "otherMsg", label: "其他信息", component: "", tag: true, icon: "material-symbols-light:other-admission" },
];
const fileComList = [
  { key: "originMsg", label: "正本信息", component: "", tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: "", tag: false, icon: "icon-park-solid:copy" },
  { key: "otherMsg", label: "其他信息", component: "", tag: false, icon: "material-symbols-light:other-admission" },
  { key: "fileMsg", label: "补充资料", component: "", tag: true, icon: "garden:upload-fill-16" },
];
const detailComList = [
  { key: "originMsg", label: "正本信息", component: "", tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: "", tag: false, icon: "icon-park-solid:copy" },
  { key: "otherMsg", label: "其他信息", component: "", tag: false, icon: "material-symbols-light:other-admission" },
  { key: "qrCodeMsg", label: "二维码", component: "", tag: false, icon: "uil:qrcode-scan" },
  { key: "handleMsg", label: "操作记录", component: "", tag: false, icon: "fa7-solid:history" },
];
export default {
  copyComList,
  fileComList,
  detailComList,
};
