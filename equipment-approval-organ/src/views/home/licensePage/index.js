import { markRaw } from "vue";
// 正本信息
import origin from "./origin.vue";
// 副本信息
import copy from "./copy.vue";
// 其他信息
import other from "./other.vue";
// 补充资料
import file from "./file.vue";
// 二维码
import qrCode from "./qrCode.vue";
// 操作记录
import history from "./history.vue";

const copyComList = [
  { key: "originMsg", label: "正本信息", component: markRaw(origin), tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: markRaw(copy), tag: true, icon: "icon-park-solid:copy" },
];
const fileComList = [
  { key: "originMsg", label: "正本信息", component: markRaw(origin), tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: markRaw(copy), tag: false, icon: "icon-park-solid:copy" },
  { key: "otherMsg", label: "其他信息", component: markRaw(other), tag: false, icon: "material-symbols-light:other-admission" },
  { key: "fileMsg", label: "补充资料", component: markRaw(file), tag: true, icon: "garden:upload-fill-16" },
];
const detailComList = [
  { key: "originMsg", label: "正本信息", component: markRaw(origin), tag: false, icon: "f7:doc-text-fill" },
  { key: "copyMsg", label: "副本信息", component: markRaw(copy), tag: false, icon: "icon-park-solid:copy" },
  { key: "otherMsg", label: "其他信息", component: markRaw(other), tag: false, icon: "material-symbols-light:other-admission" },
  { key: "fileMsg", label: "补充资料", component: markRaw(file), tag: false, icon: "garden:upload-fill-16" },
  { key: "qrCodeMsg", label: "二维码", component: markRaw(qrCode), tag: false, icon: "uil:qrcode-scan" },
  { key: "handleMsg", label: "操作记录", component: markRaw(history), tag: false, icon: "fa7-solid:history" },
];
export default {
  copyComList,
  fileComList,
  detailComList,
};
