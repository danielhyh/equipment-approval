<template>
  <div class="processing-page">
    <div class="processing-page-header">
      <div class="left">
        <div class="title"> 办件中心 </div>
        <div class="subtitle"> 大型医用设备审批办件管理 </div>
      </div>
    </div>
    <div class="container"> </div>
  </div>
</template>

<script setup lang="ts" name="ProcessingCenter">
// import
</script>

<style lang="scss" scoped>
.processing-page {
  border-radius: 20px 20px 0 0;
  background-color: #fff;
}
.processing-page-header {
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  // background-image: linear-gradient(to bottom, #409eff, #a8d9ff);
  border-radius: 20px 20px 0 0;
  background-color: #fff;
  border-bottom: 2px solid #e5e5e5;
  .left {
    .title {
      font-size: 20px;
      color: #333;
      font-weight: bold;
    }
    .subtitle {
      font-size: 14px;
      color: #333;
      margin-top: 5px;
    }
  }
}
</style>
