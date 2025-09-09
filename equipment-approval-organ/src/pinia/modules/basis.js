import { defineStore } from "pinia";
import { getStorage, setStorage, removeStorage } from "@/utils/storage";
// 医疗设备基础信息
export const useBasisStore = defineStore("basis", {
  state: () => ({
    pageTitle: "",
    modelList: [
      {
        id: "issue",
        title: "核发",
        description:
          "乙类大型医用设备配置许可证核发适用于首次申请大型医用设备配置许可证的医疗机构， 需要提供完整的申请材料和技术论证报告。",
        iconName: "svg-icon:coll",
        iconType: "green",
        iconColor: "#fff",
      },
      {
        id: "reissue",
        title: "补办",
        description:
          "乙类大型医用设备配置许可证补办适用于许可证遗失、损毁等情况需要重新补发证书的医疗机构，需要提供相关证明材料。",
        iconName: "svg-icon:rollback",
        iconType: "orange",
      },
      {
        id: "change",
        title: "变更",
        description:
          "乙类大型医用设备配置许可证信息变更适用于机构名称、地址、设备型号等关键信息发生变化需要变更许可证信息的情况。",
        iconName: "svg-icon:arrow-right",
        iconType: "blue",
      },
    ],
  }),
  getters: {
    getPageTitle(state) {
      return state.pageTitle;
    },
    getModelList(state) {
      return state.modelList;
    },
  },
  actions: {
    setPageTitle(id) {
      if (!id) return;
      let findItem = this.modelList.find((item) => item.id === id);
      this.pageTitle = findItem?.title || "未知";
    },
  },
});
