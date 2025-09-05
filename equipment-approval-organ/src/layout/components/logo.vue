<template>
  <!-- 这里可以添加 logo 的模板内容 -->
  <div class="logo-container">
    <svg-icon name="solar:hospital-bold" :size="30" color="#93c5fd" />
    <span class="logo-text">{{ sysName }}</span>
  </div>
</template>
<script setup name="Logo">
import { computed } from "vue";
import { useSystemStore } from "@/pinia/modules/system";
const systemStore = useSystemStore();
const sysName = computed(() => systemStore.getSysName);
</script>

<style lang="scss" scoped>
// 这里可以添加 scss 样式代码
.logo-container {
  color: #333;
  display: flex;
  align-items: center;
  .logo-text {
    font-size: 22px;
    color: #fff;
    margin-left: 12px;
  }
}
</style>
