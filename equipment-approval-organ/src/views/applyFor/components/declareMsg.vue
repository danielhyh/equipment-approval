<template>
  <div>
    <h3 class="c-165DFF m-b-15 f-w-700">{{ pageTitle }}(申请单位为{{ deptName }})</h3>
    <div class="p-10 b-solid-1-0891b2 b-r-10 m-b-10">
      <div class="flex items-center c-0891b2 l-h-1_5 m-b-10">
        <svg-icon name="flowbite:lightbulb-solid" size="24" color="#0891b2" />
        <span class="f-w-700">温馨提示</span>
      </div>
      <p class="l-h-1_5 f-s-14 t-indent-8px c-475569">1. 带<em class="c-dc2626">*</em>号为必填项，请根据实际情况进行填写</p>
      <p class="l-h-1_5 f-s-14 t-indent-8px c-475569">
        2. <em class="c-dc2626">*</em> 为必要材料，您必须提交才能申报，△ 为容缺后补材料，您可以在网上预受理后在窗口提交，○
        为非必要材料，根据您实际情况提交
      </p>
    </div>
    <h4 class="c-165DFF m-b-10">基本信息</h4>
    <component :is="formComponent" ref="formComRef" />
  </div>
</template>

<script setup>
import IssueForm from "./form/issue.vue";
import applyForMsg from "../index";
let route = useRoute();
let type = route.query.type;
let entity = computed(() => applyForMsg[type]);
let pageTitle = computed(() => entity.value?.title);
let dept = "shby"; // todo 后续从pinia 取值转化
// 部门名称
let deptName = computed(() => applyForMsg.dept[dept]);

// 表单组件
let formComRef = ref(null);
let formComponent = computed(() => {
  switch (type) {
    case "issue":
      return markRaw(IssueForm);
    case "reissue":
    //   return markRaw(ReissueForm);
    case "change":
    //   return markRaw(ChangeForm);
    default:
      return markRaw(IssueForm);
  }
});
// 提交
const submit = async () => {
  return formComRef.value.submit();
};
// 校验
const validor = async () => {
  return formComRef.value.validor();
};
defineExpose({
  submit,
  validor,
});
</script>

<style lang="scss" scoped>
.flex {
  display: flex;
}
.items-center {
  align-items: center;
}
.c-0891b2 {
  color: #0891b2;
}
.b-solid-1-0891b2 {
  border: 1px solid #70d4ed;
}
.t-indent-8px {
  text-indent: 8px;
}
</style>
