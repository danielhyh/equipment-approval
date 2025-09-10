<template>
  <div>
    <div class="page-header">
      <h1 class="page-title flex items-center m-r-5">
        <svg-icon name="f7:doc-text-fill" size="24" color="#165DFF" />
        {{ title }}(申请单位为{{ deptName }})
      </h1>
      <p class="page-subtitle">办事指南详细信息</p>
    </div>
    <Card style="margin-top: 24px">
      <template #header>
        <svg-icon name="fa:info-circle" size="24" color="#165DFF" />
        <span style="margin-left: 8px; font-size: 18px">办事信息</span>
      </template>
      <div class="card-guid-msg"></div>
    </Card>
  </div>
</template>

<script setup>
import { computed } from "vue";
import applyForMsg from "../applyFor/index.js";
const route = useRoute();
const type = route.query.type;
let dept = "shby"; // todo 后续从pinia 取值转化
// 部门名称
let deptName = computed(() => applyForMsg.dept[dept]);
// 标题
const title = computed(() => applyForMsg[type].title);

const entity = computed(() => applyForMsg[type]);
// 办理信息
const processInfo = computed(() => entity.value.processInfo);
// 基本信息
const basisInfo = computed(() => entity.value.basisInfo);
// 申请材料
const material = computed(() => entity.value.material.dept[dept].list);
</script>

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
</style>
