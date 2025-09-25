<template>
  <div class="apply-for-box">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <h1 class="page-title flex items-center m-r-5">
        <svg-icon name="f7:doc-text-fill" size="24" color="#165DFF" />
        {{ title }}(申请单位为{{ deptName }})
      </h1>
      <p class="page-subtitle">在线办理流程</p>
    </div>

    <!-- 步骤器组件 -->
    <Steps v-model:currentStep="currentStepIndex" :steps="steps" :handleStep="maxStep" />

    <Card style="margin-top: 24px" class="card-style-page">
      <template #header>
        <svg-icon :name="currentPage.icon" size="24" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">{{ currentPage.title }}</span>
      </template>

      <div class="content-box">
        <!-- 对应索引 组件 -->
        <Transition mode="out-in" appear name="fade-slide">
          <KeepAlive>
            <component :is="currentPage.component" ref="pageComponentRef" />
          </KeepAlive>
        </Transition>
      </div>

      <div class="footer-box" v-if="showFooter">
        <el-button class="prev-btn" type="info" :icon="Back" v-if="currentbtns.prev" @click="prevStepFn">上一步</el-button>
        <el-button class="next-btn" type="primary" :icon="Right" v-if="currentbtns.next" @click="nextStepFn">下一步</el-button>
        <el-button type="info" :icon="List" v-if="currentbtns.stagingg" @click="stagingFn">暂存</el-button>
        <el-button class="submit-btn" type="primary" :icon="Checked" v-if="currentbtns.submit" @click="submitFn">提交</el-button>
      </div>
    </Card>
  </div>
</template>

<script setup>
import applyForMsg from "./index.js";
import { getApplyDetail } from "@/apis/applyFor.js";
import { Back, Right, Checked, List } from "@element-plus/icons-vue";
import Notice from "./components/notice.vue";
import DeclareMsg from "./components/declareMsg.vue";
import Note from "./components/note.vue";
import UploadMsg from "./components/uploadMsg.vue";
import Review from "./components/review.vue";
import reviewEnd from "./components/reviewEnd.vue";
import { useDictStore } from "@/pinia/modules/dict.js";
import { useUserStore } from "@/pinia/modules/user.js";
const dictStore = useDictStore();
const userStore = useUserStore();
const applyTypeDict = computed(() => dictStore.getDictTypeList("biz_app_type"));
const institutionDict = computed(() => dictStore.getDictTypeList("biz_institution_type"));
// 许可设备
let deviceOptions = computed(() => dictStore.getDictTypeList("biz_main_equipment_type"));
// 阶梯配置机型
let modelOptions = computed(() => dictStore.getDictTypeList("biz_ladder_config_model"));
const route = useRoute();
const applyId = route.query.id;
const type = route.query.type; 
const handle = route.query.handle;
provide("disabled", handle === "detail");
let dept = computed(() => {
  let userInfo = userStore.getUser;
  let deptMsg = institutionDict.value.find((item) => item.value === userInfo?.institutionType);
  return deptMsg || { cssClass: "shby", label: "社会办医", value: "1" };
});
// 部门名称
let deptName = computed(() => dept.value.label);
// 标题
const title = computed(() => applyForMsg[type].title);
const headTitle = computed(() => `${title.value}（申请单位为${deptName.value}）`);
provide("headTitle", headTitle.value);
provide("deptMsg", dept.value);
// 当前步骤索引（从0开始）
const currentStepIndex = ref(0);
let maxStep = ref(0);
let pageComponentRef = ref(null);
// 对应索引 组件
const pageComponents = [
  {
    component: markRaw(Notice),
    icon: "fa:info-circle",
    title: "申报须知",
    status: "10%",
    btns: { next: true, prev: false, staging: false, submit: false },
  },
  {
    component: markRaw(DeclareMsg),
    icon: "fa-solid:edit",
    title: "申报信息",
    status: "30%",
    btns: { next: true, prev: true, staging: true, submit: false },
  },
  {
    component: markRaw(Note),
    icon: "bi:exclamation-triangle-fill",
    title: "注意事项",
    status: "50%",
    btns: { next: true, prev: true, staging: false, submit: false },
  },
  {
    component: markRaw(UploadMsg),
    icon: "garden:upload-fill-12",
    title: "材料上传",
    status: "80%",
    btns: { next: false, prev: true, staging: false, submit: true },
  },
  {
    component: markRaw(Review),
    icon: "subway:search",
    title: "评审状态",
    status: "90%",
    btns: { next: false, prev: false, staging: false, submit: false },
  },
  {
    component: markRaw(reviewEnd),
    icon: "fa-solid:hourglass-end",
    title: "申报结束",
    status: "100%",
    btns: { next: false, prev: false, staging: false, submit: false },
  },
];
const steps = computed(() => {
  return pageComponents.map((item) => ({ title: item.title, status: item.status }));
});
const currentPage = computed(() => {
  return pageComponents[currentStepIndex.value];
});
const currentbtns = computed(() => {
  return currentPage.value.btns;
});
const showFooter = computed(() => {
  return (
    (currentbtns.value.next || currentbtns.value.prev || currentbtns.value.staging || currentbtns.value.submit) &&
    handle !== "detail"
  );
});

let formAllData = ref({});
provide("formAllData", formAllData);
let typeKey = computed(() => {
  switch (type) {
    case "issue":
      return 1;
    case "reissue":
      return 2;
    case "change":
      return 3;
    case "info":
      return 4;
  }
});
provide("applyType", typeKey.value); //申请类型
// 步骤器变化 ++
const maxStepChange = () => {
  if (currentStepIndex.value === maxStep.value) {
    maxStep.value++;
  }
  currentStepIndex.value++;
};
// 暂存
const stagingFn = () => {
  console.log("暂存");
};

// 上一步
const prevStepFn = () => {
  currentStepIndex.value--;
};
// 下一步
const nextStepFn = async () => {
  if (!pageComponentRef.value) return;
  try {
    let formData = await pageComponentRef.value.submit();
    if (!formData) return;
    formAllData.value = Object.assign(formAllData.value, formData); // formData { } 数据暂存
    maxStepChange();
  } catch (err) {
    console.log(err);
  }
};
// 提交
const submitFn = async () => {
  // 数据提交
  if (!pageComponentRef.value) return;
  try {
    let formData = await pageComponentRef.value.submit();
    if (!formData) return;
    formAllData.value = Object.assign(formAllData.value, formData); // formData { } 数据暂存
    maxStepChange();
  } catch (err) {
    console.log(err);
  }
};
// 获取详情
const getApplyDetailFn = async () => {
  if (!applyId) return;
  try {
    let res = await getApplyDetail(applyId);
    let { data } = res;
    formAllData.value = data;
    // 许可设备
    if (data.licenseDeviceName) {
      formAllData.value.licenseDeviceName = deviceOptions.value.find((item) => item.label === data.licenseDeviceName)?.value;
    }
    // 阶梯配置机型
    if (data.ladderConfigModel) {
      formAllData.value.ladderConfigModel = modelOptions.value.find((item) => item.label === data.ladderConfigModel)?.value;
    }
    formAllData.value.checked = true;
    // 评审状态
    switch (data.appStatus) {
      case 1:
      case 3:
        maxStep.value = 4;
        currentStepIndex.value = 4;
        break;
      case 2:
      case 4:
      case 5:
        maxStep.value = 5;
        currentStepIndex.value = 4;
        break;
      default:
        break;
    }
  } catch (err) {
    console.log(err);
  }
};
onMounted(() => {
  getApplyDetailFn();
});
</script>

<style lang="scss" scoped>
.apply-for-box {
}

.page-header {
  padding: 16px 20px;
  background-color: #fff;
  color: #165dff;
  box-shadow: 0 8px 32px rgba(22, 93, 255, 0.08);
  border-left: 4px solid #165dff;
  border-radius: 16px;
  margin-bottom: 24px;
  .page-title {
    font-size: 24px;
    font-weight: bold;
    color: #165dff;
    margin: 0 0 8px 0;
  }

  .page-subtitle {
    font-size: 14px;
    color: #64748b;
    margin: 0;
  }
}

.card-style-page {
  .footer-box {
    text-align: center;
    margin-top: 40px;
    .el-button {
      margin: 0 10px;
      border-radius: 8px;
      box-shadow: 0 4px 16px rgba(107, 114, 128, 0.3);
      transition: all 0.3s ease;
      &:hover {
        transform: translateY(-2px);
      }
    }
    .el-button--info {
      background-image: linear-gradient(135deg, #6b7280, #4b5563);
    }
    .el-button--primary {
      background: linear-gradient(135deg, #165dff, #3b82f6);
      box-shadow: 0 4px 16px rgba(22, 93, 255, 0.3);
    }
    .is-disabled {
      background: linear-gradient(135deg, #94a3b8, #94a3b8);
      opacity: 0.6;
      box-shadow: none;
      &:hover {
        transform: translateY(0);
      }
    }
  }
}

/* 过渡动画样式 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.55, 0, 0.1, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-slide-leave-to {
  opacity: 0.3;
  transform: translateX(-30px);
}

.fade-slide-enter-to,
.fade-slide-leave-from {
  opacity: 1;
  transform: translateX(0);
}
</style>
