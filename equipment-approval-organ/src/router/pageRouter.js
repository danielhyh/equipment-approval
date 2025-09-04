export default [
  {
    path: "/",
    redirect: "/login", //重定向
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/login/index.vue"),
    meta: { title: "登录" },
  }
];
