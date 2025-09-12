<template>
  <div>
    <h3 class="c-165DFF m-b-15 f-w-700">{{ pageTitle }}(申请单位为{{ deptName }})</h3>
    <h4 class="c-165DFF m-b-10">审批条件</h4>
    <p class="l-h-1_5 c-64748b f-s-16 m-b-10 text-indent-2em">{{ NoteRemark }}</p>
    <div v-for="(item, index) in NoteList" :key="index" class="m-b-10">
      <h4 class="f-s-14 c-165DFF m-b-5">{{ item.title }}</h4>
      <template v-if="Array.isArray(item.content)">
        <ul class="p-l-r-20 c-64748b ul-style-inside">
          <li class="l-h-1_5 m-b-5" v-for="(eg, i) in item.content" :key="i">{{ eg }}</li>
        </ul>
      </template>
      <template v-if="'string' === typeof item.content">
        <p class="f-s-16 c-64748b text-indent-2em">{{ item.content }}</p>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import applyForMsg from "../index";
let formAllData = inject("formAllData");
let deviceType = formAllData.value?.licenseDeviceName; // 许可设备名称

let route = useRoute();
let type = route.query.type;
let dept = "shby"; // todo 后续从pinia 取值转化
// 部门名称
let deptName = computed(() => applyForMsg.dept[dept]);

let entity = computed(() => applyForMsg[type]);
let pageTitle = computed(() => entity.value?.title);
let Note = computed(() => entity.value?.note[deviceType]);
let NoteRemark = computed(() => Note.value.remark);
let NoteList = computed(() => Note.value.list);

// 提交
const submit = async () => {
  return {};
};
defineExpose({
  submit,
});
</script>

<style lang="scss" scoped></style>
