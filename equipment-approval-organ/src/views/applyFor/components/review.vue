<template>
  <div class="">
    <div class="p-t-b-20 flex items-center justify-center">
      <svg-icon :name="reviewIcon.icon" :color="reviewIcon.color" size="50" />
    </div>
    <h1 class="flex items-center justify-center c-333 f-s-20 letter-spacing-2 m-b-5">{{ reviewStatusLabel }}</h1>
    <p class="flex items-center justify-center f-s-16">{{ reviewIcon.text }}</p>
    <!-- <p class="flex items-center justify-center f-s-16" v-if="reviewIcon.timeLabel">{{ reviewIcon.timeLabel }}</p> -->
    <p class="flex items-center justify-center f-s-16 w-500 m-auto" v-if="formAllData?.configReason">
      审批意见:{{ formAllData?.configReason }}
    </p>
    <el-divider />
    <div class="flex items-center justify-center">
      <el-button type="primary" size="default" @click="handleClick">
        <template #icon>
          <svg-icon name="fa7-solid:home-lg-alt" />
        </template>
        返回首页
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { useDictStore } from "@/pinia/modules/dict.js";
let formAllData = inject("formAllData");
const dictStore = useDictStore();
const reviewStatusDict = computed(() => dictStore.getDictTypeList("biz_app_status"));
const reviewStatusLabel = computed(() => {
  return reviewStatusDict.value.find((item) => item.value === formAllData.value?.appStatus + "")?.label || "未知";
});
const reviewIcon = computed(() => {
  switch (formAllData.value?.appStatus) {
    case 1:
    case 3:
      return {
        icon: "fa:warning",
        color: "#FF9800",
        text: "正在审核中，请耐心等待",
      };
    case 5:
      return { icon: "codicon:pass-filled", color: "#008000", text: "审核通过", timeLabel: "未通过时间：" };
    // 拒绝
    case 2:
    case 4:
      return { icon: "vaadin:close-circle", color: "#FF0000", text: "很遗憾，您的申请未通过" };
    default:
      return { icon: "vaadin:close-circle", color: "#FF0000", text: "状态未知" };
  }
});
const router = useRouter();
const handleClick = () => {
  router.replace({ path: "/home/index-page" });
};
</script>

<style lang="scss" scoped>
.w-500 {
  max-width: 500px;
}
.m-auto {
  margin: 0 auto;
}
</style>
