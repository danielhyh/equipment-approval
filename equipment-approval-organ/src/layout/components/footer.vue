<template>
  <footer class="footer">
    <!-- 这里可以添加页脚内容 -->
    <p>&copy; {{ copyright }}</p>
    <p>技术支持：029-123456</p>
    <p>业务办理咨询：029-987654</p>
  </footer>
</template>

<script setup name="FooterBottom">
import config from "@/config";
// 使用 setup 语法糖的 Vue 3 组件

let { copyright } = config;
</script>

<style lang="scss" scoped>
.footer {
  // 这里可以添加页脚样式
  background-color: #f5f5f5;
  text-align: center;
  height: var(--footer-height);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
  font-size: 14px;
  background-color: rgba(30, 41, 59, 0.8);
}
</style>
