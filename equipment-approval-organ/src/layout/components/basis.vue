<template>
  <!-- 这里可以添加模板内容 -->
  <div class="basis-container"></div>
</template>

<script setup name="BasisInfo">
import { computed } from "vue";
// 这里可以添加 setup 语法糖下的脚本逻辑
import { useUserStore } from "@/pinia/modules/user";
let userStore = useUserStore();

const userData = computed(() => userStore.getUser);
</script>

<style lang="scss" scoped>
// 这里可以添加 SCSS 样式
.basis-container {
  height: 100%;
  background-color: #f5f5f5;
}
</style>
