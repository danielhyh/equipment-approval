import { defineStore } from "pinia";
import { getStorage, setStorage, removeStorage } from "@/utils/storage";
import { isJsonString } from "@/utils/tools";
import { getUserInfo } from "@/apis/login.js";

// user ：数据结构
// {
//   nickname: "test", //用户昵称
//   institutionName: "西安交通大学第一附属医院", //机构名称
//   institutionId: null, //机构id
//   legalPerson: "韩永辉", //法人
//   unifiedSocialCreditCode: "12610000MB0123456X", //统一社会信用代码
//   detailedAddress: "陕西省西安市雁塔区紫薇尚层西一楼二单元", //详细地址
//   ownershipNature: "国有", //所有权性质
//   institutionType: "1", //机构性质
//   superiorInstitution: "陕西省卫生健康委员会", //上级机构
//   institutionLevel: "3级甲等", // 机构级别
//   region: "陕西省西安市", // 区域
//   contactPerson: "韩立", // 联系人
//   contactPhone: "15688888888", // 联系人电话
// }
export const useUserStore = defineStore("user", {
  state: () => ({
    user: isJsonString(getStorage("userInfo")) ? JSON.parse(getStorage("userInfo")) : null,
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
    },
  },
  actions: {
    async setUser() {
      try {
        if (this.user) return;
        let response = await getUserInfo();
        let { data } = response;
        this.user = data;
        setStorage("userInfo", JSON.stringify(data));
      } catch (err) {
        this.user = null;
        removeStorage("userInfo");
        console.log(err, "获取用户信息失败");
      }
    },
    async updateUser() {
      try {
        let response = await getUserInfo();
        let { data } = response;
        this.user = data;
        setStorage("userInfo", JSON.stringify(data));
      } catch (err) {
        this.user = null;
        removeStorage("userInfo");
        console.log(err, "获取用户信息失败");
      }
    },
    setToken(token) {
      this.token = token;
      setStorage("token", token);
    },
    // 设置SSO token数据
    setSsoToken(tokenData) {
      if (tokenData && tokenData.accessToken) {
        this.token = tokenData.accessToken;
        setStorage("token", tokenData.accessToken);
        // 如果有其他token信息也需要存储，可以在这里添加
        if (tokenData.refreshToken) {
          setStorage("refreshToken", tokenData.refreshToken);
        }
      }
    },
    loginOut() {
      this.user = null;
      this.token = null;
      removeStorage("userInfo");
      removeStorage("token");
    },
    setVaildLogin() {
      this.vaildLogin = !this.vaildLogin;
    },
  },
});
