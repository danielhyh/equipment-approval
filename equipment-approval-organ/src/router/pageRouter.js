import layout from "@/layout/index.vue";
import Deputy from "@/layout/deputy.vue";
export default [
  {
    path: "/",
    redirect: "/home", //重定向
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/index.vue"),
    meta: { title: "登录" },
  },
  {
    path: "/home",
    name: "Home",
    component: layout,
    redirect: "/home/index-page",
    meta: { title: "首页" },
    children: [
      {
        path: "index-page",
        name: "IndexPage",
        component: () => import("@/views/home/index.vue"),
        meta: { title: "首页" },
      },
    ],
  },
  {
    path: "/deputy",
    name: "Deputy",
    component: Deputy,
    redirect: "/deputy/apply-for",
    meta: { title: "在线办理" },
    children: [
      {
        path: "apply-for",
        name: "ApplyFor",
        component: () => import("@/views/applyFor/index.vue"),
        meta: { title: "在线办理" },
      },
      {
        path: "guide",
        name: "Guide",
        component: () => import("@/views/guide/index.vue"),
        meta: { title: "使用指南" },
      },
    ],
  },
  // 404路由配置 - 必须放在所有路由的最后
  {
    path: '/:pathMatch(.*)*', // 通配符路由，匹配任何未定义的路径
    name: 'NotFound',
    component: () => import('@/views/not-found/index.vue'),
    meta: {
      title: '404 - 页面不存在',
      requiresAuth: false // 不需要登录即可访问
    }
  },
];
