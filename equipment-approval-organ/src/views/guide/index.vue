<template>
  <div>
    <div class="page-header">
      <h1 class="page-title">{{ title }}</h1>
      <p class="page-subtitle">办事指南详细信息</p>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
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
</style>
