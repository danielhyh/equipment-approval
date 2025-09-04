import { createApp } from "vue";
import "./styles/index.scss";
import App from "./App.vue";
import router from "./router";
import { createPiniaStore } from "./pinia/index";
// svg 精灵图
import "virtual:svg-icons-register";
// svg 图标组件
import SvgIcon from "./icons/SvgIcon.vue";


function initApp() {
  const app = createApp(App);
  app.use(router);
  createPiniaStore(app);
  // 全局注册 svg 图标组件
  app.component("svg-icon", SvgIcon);
  app.mount("#app");
}

initApp();
