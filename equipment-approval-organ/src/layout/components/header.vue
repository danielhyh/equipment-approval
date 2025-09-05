<template>
  <!-- 这里可以添加模板内容 -->
  <header>
    <div class="header-left">
      <Logo />
    </div>

    <div class="header-right">
      <div class="header-right-item">
        <el-button type="primary" size="small">登录</el-button>
      </div>
    </div>
  </header>
</template>
<script setup name="HeaderTop">
import Logo from "./logo.vue";
</script>

<style lang="scss" scoped>
/* 这里可以添加样式代码 */
header {
  height: var(--nav-height);
  padding: 0 20px;
  outline: 1px solid rgba(154, 208, 255, 0.5);
  backdrop-filter: blur(10px);
  background-image: linear-gradient(135deg, #165dff 0%, #06b6d4 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
