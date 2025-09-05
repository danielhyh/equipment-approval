import { createRouter, createWebHashHistory } from "vue-router";
import page from "./pageRouter.js";
import { beforeEach, afterEach } from "./permission.js";

const routers = [...page];

const router = createRouter({
  history: createWebHashHistory(),
  routes: routers,
  // 路由跳转后滚动条回到顶部
  scrollBehavior: (to, from) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const scrollDom = document.getElementById("AppMain");
        if (scrollDom) {
          scrollDom.scrollTop = 0;
          scrollDom.scrollLeft = 0;
        }
        resolve({ x: 0, y: 0 });
      }, 0);
    });
    // return {
    //   el: "#AppMain",
    //   top: 0,
    //   behavior: "smooth",
    //   x: 0,
    //   y: 0,
    // };
  },
});

// 路由拦截
router.beforeEach(beforeEach);
router.afterEach(afterEach);

export default router;
