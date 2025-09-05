import layout from "@/layout/index.vue";
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
    redirect: "home/index-page",
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
];
