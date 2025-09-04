import { defineStore } from "pinia";
import { getStorage, setStorage, removeStorage } from "../../utils/storage";
export const useUserStore = defineStore("user", {
  state: () => ({
    user: null || getStorage("user"),
    token: null || getStorage("token"),
    vaildLogin: false,
  }),
  getters: {
    getUser(state) {
      return state.user;
    },
    getToken(state) {
      return state.token;
    },
    getIsLogin(state) {
      return state.user !== null || state.token !== null;
    },
    getVaildLogin(state) {
      return state.vaildLogin;
    }
  },
  actions: {
    setUser(user) {
      this.user = user;
      setStorage("user", user);
    },
    setToken(token) {
      setStorage("token", token);
      this.token = token;
    },
    loginOut() {
      this.user = null;
      this.token = null;
      removeStorage("user");
      removeStorage("token");
    },
    setVaildLogin() {
      this.vaildLogin = !this.vaildLogin;
    },
  },
});
