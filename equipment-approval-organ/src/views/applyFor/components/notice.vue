<template>
  <div>
    <h3 class="c-165DFF m-b-15 f-w-700">{{ pageTitle }}(申请单位为{{ deptName }})</h3>
    <h4 class="c-165DFF m-b-10">审批条件</h4>
    <p class="l-h-1_5 c-64748b f-s-16 m-b-10">{{ condition?.remark }}</p>
    <ul class="m-b-10">
      <li class="c-475569 m-b-5 l-h-1_5 f-s-16" v-for="item in condition?.list" :key="item">{{ item }}</li>
    </ul>
    <h4 class="c-165DFF m-b-10">收取材料</h4>
    <p class="l-h-1_5 c-64748b f-s-16 m-b-10">{{ mateialRemark }}</p>
    <ul class="m-b-15">
      <li class="c-475569 m-b-10 l-h-1_5 f-s-16" v-for="item in material?.list" :key="item">
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
    <!-- 声明勾选 -->
    <div class="m-t-40 m-l-r-20 p-t-b-10 p-l-r-20 b-r-10 bg-c-f8fafc b-solid-2" :class="{ error: validError }">
      <el-checkbox v-model="checked" class="custom-checkbox">
        我已仔细阅读并完全理解以上申报须知，确认我的申请满足所有审批条件，具备所有必需材料，并同意按照相关规定进行申报。
      </el-checkbox>
    </div>
  </div>
</template>

<script setup>
import applyForMsg from "../index";

let route = useRoute();
let router = useRouter();
let type = route.query.type;

let dept = "shby"; // todo 后续从pinia 取值转化
// 部门名称
let deptName = computed(() => applyForMsg.dept[dept]);
// 数据
let entity = computed(() => applyForMsg[type]);
let pageTitle = computed(() => entity.value?.title);
// 审批条件
let condition = computed(() => entity.value?.condition);
let material = computed(() => entity.value?.material?.dept[dept]);
let mateialRemark = computed(() => entity.value?.material?.remark);

let checked = ref(false);
let validError = ref(false);
// 办事指南
const goGuide = () => {
  router.push({
    path: "/deputy/guide",
    query: {
      type: type,
    },
  });
};

// 校验
const validor = () => {
  return new Promise((resolve, reject) => {
    if (!checked.value) {
      ElMessage.error("请先勾选声明");
      validError.value = true;
      setTimeout(() => {
        validError.value = false;
      }, 2000);
      reject(false);
    }
    resolve(true);
  });
};
// 提交
const submit = () => {
  return new Promise(async (resolve, reject) => {
    try {
      await validor();
      resolve({ checked: checked.value });
    } catch (err) {
      console.log(err);
      reject(false);
    }
  });
};

defineExpose({
  validor,
  submit,
});
</script>

<style lang="scss" scoped>
ul {
  padding-left: 2em;
  list-style-position: outside;
  list-style: lower;
}
.custom-checkbox {
  --el-checkbox-font-size: 16px;
  --el-checkbox-input-height: 20px;
  --el-checkbox-input-width: 20px;
  --el-checkbox-text-color: #475569;
  --el-checkbox-input-border: 1px solid #165dff;
  --el-checkbox-checked-text-color: #165dff;
  --el-checkbox-checked-bg-color: #165dff;
  &:deep(.el-checkbox__inner:after) {
    border-width: 2px;
    height: 10px;
    width: 5px;
  }
}
@keyframes shake-bottom {
  0%,
  100% {
    -webkit-transform: rotate(0deg);
    transform: rotate(0deg);
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
  }
  10% {
    -webkit-transform: rotate(2deg);
    transform: rotate(2deg);
  }
  20%,
  40%,
  60% {
    -webkit-transform: rotate(-4deg);
    transform: rotate(-4deg);
  }
  30%,
  50%,
  70% {
    -webkit-transform: rotate(4deg);
    transform: rotate(4deg);
  }
  80% {
    -webkit-transform: rotate(-2deg);
    transform: rotate(-2deg);
  }
  90% {
    -webkit-transform: rotate(2deg);
    transform: rotate(2deg);
  }
}
.error {
  animation: shake-bottom 0.8s cubic-bezier(0.455, 0.03, 0.515, 0.955) both;
  border-color: #dc2626;
}
</style>
