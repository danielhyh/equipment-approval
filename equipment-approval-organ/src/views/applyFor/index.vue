<template>
  <div class="apply-for-box">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <h1 class="page-title">{{ title }}</h1>
      <p class="page-subtitle">在线办理流程</p>
    </div>

    <!-- 步骤器组件 -->
    <Steps v-model:currentStep="currentStepIndex" :handleStep="4" />

    <Card style="margin-top: 24px" class="card-style-page">
      <template #header>
        <svg-icon :name="currentPage.icon" size="24" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">{{ currentPage.title }}</span>
      </template>

      <div class="content-box">
        <!-- 对应索引 组件 -->
        <!-- <component :is="currentPage.component" /> -->
        <Notice />
      </div>

      <div class="footer-box" v-if="showFooter">
        <el-button type="info" :icon="List">暂存</el-button>
        <el-button class="prev-btn" type="info" :icon="Back">上一步</el-button>
        <el-button class="next-btn" type="primary" :icon="Right">下一步</el-button>
        <el-button class="submit-btn" type="primary" :icon="Checked">提交</el-button>
      </div>
    </Card>
  </div>
</template>

<script setup>
import applyForMsg from "./index.js";
import { Back, Right, Checked, List } from "@element-plus/icons-vue";
import Notice from "./components/notice.vue";
import { computed, markRaw } from "vue";
// 标题
const title = computed(() => applyForMsg.issue.title);
// 当前步骤索引（从0开始）
const currentStepIndex = ref(0);
// 对应索引 组件
const pageComponents = [
  {
    component: markRaw(Notice),
    icon: "fa:info-circle",
    title: "申报须知",
    btns: { next: true, prev: false, staging: false, submit: false },
  },
  {
    component: "",
    icon: "fa-solid:edit",
    title: "申报信息",
    btns: { next: true, prev: true, staging: true, submit: false },
  },
  {
    component: "",
    icon: "bi:exclamation-triangle-fill",
    title: "注意事项",
    btns: { next: true, prev: true, staging: false, submit: false },
  },
  {
    component: "",
    icon: "garden:upload-fill-12",
    title: "材料上传",
    btns: { next: false, prev: true, staging: false, submit: true },
  },
  {
    component: "",
    icon: "subway:search",
    title: "评审状态",
    btns: { next: false, prev: false, staging: false, submit: false },
  },
  {
    component: "",
    icon: "fa-solid:hourglass-end",
    title: "申报结束",
    btns: { next: false, prev: false, staging: false, submit: false },
  },
];
const currentPage = computed(() => {
  return pageComponents[currentStepIndex.value];
});
const currentbtns = computed(() => {
  return currentPage.value.btns;
});
const showFooter = computed(() => {
  return currentbtns.value.next || currentbtns.value.prev || currentbtns.value.staging || currentbtns.value.submit;
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
    font-size: 18px;
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
    margin-top: 10px;
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
</style>
