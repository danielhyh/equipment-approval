<template>
  <div class="p-l-r-5">
    <h3 class="c-165DFF m-b-15 f-w-700">{{ pageTitle }}(申请单位为{{ deptName }})</h3>
    <h4 class="c-165DFF m-b-10">审批条件</h4>
    <p class="l-h-1_5 c-64748b f-s-16 m-b-10">{{ condition.remark }}</p>
    <ul class="m-b-10">
      <li class="c-475569 m-b-5 l-h-1_5 f-s-16" v-for="item in condition.list" :key="item">{{ item }}</li>
    </ul>
    <h4 class="c-165DFF m-b-10">收取材料</h4>
    <p class="l-h-1_5 c-64748b f-s-16 m-b-10">{{ mateialRemark }}</p>
    <ul class="m-b-15">
      <li class="c-475569 m-b-10 l-h-1_5 f-s-16" v-for="item in material.list" :key="item">
        <span>{{ item.text }}</span>
        <em class="c-dc2626" v-if="item.required">【必需材料】</em>
      </li>
    </ul>

    <!-- 备注下载 -->
    <div class="m-b-30 m-l-r-20 p-t-b-10 p-l-r-20 bg-c-10b9811a b-solid-1 b-r-10 f-s-14 l-h-1_5 c-059669">
      <svg-icon name="fa-solid:download" size="16" color="#059669" class="c-059669"></svg-icon>
      <span class="m-l-r-6">请在</span>
      <span class="c-165DFF c-pointer f-w-700" @click.stop="goGuide">【办事指南】</span>
      <span class="m-l-r-6">页面下载模板</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import applyForMsg from "../index";

let route = useRoute();
let router = useRouter();
let type = computed(() => route.query.type);

let entity = computed(() => applyForMsg[type.value]);
let pageTitle = computed(() => entity.value?.title);
// 审批条件
let condition = computed(() => entity.value?.notice?.condition);

let dept = "shby"; // todo 后续从pinia 取值转化
let material = computed(() => entity.value?.notice?.material.dept[dept]);
let mateialRemark = computed(() => entity.value?.notice?.material.remark);
// 部门名称
let deptName = computed(() => material.value.label);

// 办事指南
const goGuide = () => {
  router.push({
    path: "/deputy/guide",
    query: {
      type: type.value,
    },
  });
};
</script>

<style lang="scss" scoped>
.m-b-30 {
  margin-bottom: 30px;
}
.m-b-15 {
  margin-bottom: 15px;
}
.m-b-10 {
  margin-bottom: 10px;
}
.m-b-5 {
  margin-bottom: 5px;
}
.m-l-r-20 {
  margin-left: 20px;
  margin-right: 20px;
}
.m-l-r-6 {
  margin-left: 6px;
  margin-right: 6px;
}
.p-t-b-10 {
  padding-top: 10px;
  padding-bottom: 10px;
}
.p-l-r-20 {
  padding-left: 20px;
  padding-right: 20px;
}
.p-l-r-5 {
  padding-left: 5px;
  padding-right: 5px;
}
.c-165DFF {
  color: #165dff;
}
.c-64748b {
  color: #64748b;
}
.c-475569 {
  color: #475569;
}
.c-dc2626 {
  color: #dc2626;
}
.c-059669 {
  color: #059669;
}
.f-s-16 {
  font-size: 16px;
}
.f-s-14 {
  font-size: 14px;
}
.f-w-700 {
  font-weight: 700;
}
.l-h-24 {
  line-height: 24px;
}
.l-h-1_5 {
  line-height: 1.5;
}
.bg-c-10b9811a {
  background-color: #10b9811a;
}
.b-r-10 {
  border-radius: 10px;
}
.b-solid-1 {
  border: 1px solid #10b981;
}
.c-pointer {
  cursor: pointer;
}
ul {
  padding-left: 2em;
  list-style-position: outside;
  list-style: lower;
}
</style>
