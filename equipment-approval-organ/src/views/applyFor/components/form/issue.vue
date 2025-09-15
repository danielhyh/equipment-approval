<template>
  <el-form
    :model="formData"
    :rules="rules"
    ref="formRef"
    label-width="120px"
    label-position="top"
    class="grid-form"
    v-loading="loading"
    :disabled="disabled"
  >
    <!-- 组织机构名称（配置单位名称）* -->
    <el-form-item label="组织机构名称（配置单位名称）*" prop="institutionName" disabled>
      <el-input v-model="formData.institutionName" placeholder="请输入组织机构名称（配置单位名称）" disabled />
    </el-form-item>
    <!-- 法定代表人（或主要负责人）* -->
    <el-form-item label="法定代表人（或主要负责人）*" prop="legalPerson">
      <el-input v-model="formData.legalPerson" placeholder="请输入法定代表人（或主要负责人）" disabled />
    </el-form-item>
    <!-- 统一社会信用代码* -->
    <el-form-item label="统一社会信用代码*" prop="unifiedSocialCreditCode">
      <el-input v-model="formData.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码" disabled />
    </el-form-item>
    <!-- 所有制性质* -->
    <el-form-item label="所有制性质*" prop="ownershipNature">
      <el-input v-model="formData.ownershipNature" placeholder="请输入所有制性质" disabled />
    </el-form-item>
    <!-- 许可设备名称* -->
    <el-form-item label="许可设备名称*" prop="licenseDeviceName" class="grid-item-l-2">
      <el-select v-model="formData.licenseDeviceName" placeholder="请选择许可设备名称" clearable :disabled="submitSuccess">
        <el-option
          v-for="item in deviceOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          :disabled="item.disabled"
        />
      </el-select>
    </el-form-item>
    <!-- 阶梯配置机型* -->
    <el-form-item label="阶梯配置机型*" prop="ladderConfigModel" class="grid-item-r-2">
      <el-select v-model="formData.ladderConfigModel" placeholder="请输入阶梯配置机型" clearable :disabled="submitSuccess">
        <el-option v-for="item in modelOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <!-- 设备配置地址* -->
    <el-form-item label="设备配置地址*" prop="detailedAddress" class="grid-item-row">
      <template #label="{ label }">
        <span>{{ label }}</span>
      </template>
      <el-input v-model="formData.detailedAddress" placeholder="请输入设备配置地址" disabled />
    </el-form-item>
    <!-- 联系人* -->
    <el-form-item label="联系人*" prop="contactPerson" class="grid-item-l-2">
      <el-input v-model="formData.contactPerson" placeholder="请输入联系人" disabled />
    </el-form-item>
    <!-- 联系人电话* -->
    <el-form-item label="联系人电话*" prop="contactPhone" class="grid-item-r-2">
      <el-input v-model="formData.contactPhone" placeholder="请输入联系人电话" disabled />
    </el-form-item>
  </el-form>
</template>

<script setup>
import { isEmptyObject } from "@/utils/tools";
import { useUserStore } from "@/pinia/modules/user";
import { useDictStore } from "@/pinia/modules/dict";
import { createApply, updateApply } from "@/apis/applyFor";
let userStore = useUserStore();
let dictStore = useDictStore();
const appType = inject("applyType"); // 申请类型

let formAllData = inject("formAllData");
let disabled = inject("disabled");
let formRef = ref(null);
let formData = reactive({
  institutionName: "", // 组织机构名称（配置单位名称）
  legalPerson: "", // 法定代表人（或主要负责人）
  unifiedSocialCreditCode: "", // 统一社会信用代码
  ownershipNature: "", // 所有制性质
  licenseDeviceName: "", // 许可设备名称
  ladderConfigModel: "", // 阶梯配置机型
  detailedAddress: "", // 设备配置地址
  contactPerson: "", // 联系人
  contactPhone: "", // 联系人电话
});
let rules = reactive({
  licenseDeviceName: [{ required: true, message: "请选择许可设备名称", trigger: "blur" }],
  ladderConfigModel: [{ required: true, message: "请输入阶梯配置机型", trigger: "blur" }],
});
let userInfo = computed(() => userStore.getUser);
// 数据初始化
watch(
  () => userInfo.value,
  (user) => {
    Object.keys(formData).forEach((key) => {
      formData[key] = user[key] || formData[key];
    });
  },
  { immediate: true }
);
watch(
  () => formAllData.value,
  (newVal) => {
    formData.id = newVal.id;
    formData.appStatus = newVal.appStatus;
    formData.licenseDeviceName = newVal.licenseDeviceName; // 许可设备名称
    formData.ladderConfigModel = newVal.ladderConfigModel; // 阶梯配置机型
  },
  { immediate: true }
);
// 许可设备
let deviceOptions = computed(() => dictStore.getDictTypeList("biz_main_equipment_type"));
// 阶梯配置机型
let modelOptions = computed(() => dictStore.getDictTypeList("biz_ladder_config_model"));

const validor = () => {
  return new Promise((resolve, reject) => {
    formRef.value.validate((valid) => {
      if (valid) {
        resolve(true);
      } else {
        reject(false);
      }
    });
  });
};
let loading = ref(false);
let submitSuccess = ref(false);
const submit = () => {
  return new Promise(async (resolve, reject) => {
    try {
      await validor();
      if (submitSuccess.value) {
        resolve(JSON.parse(JSON.stringify(formData)));
        return;
      }
      let params = {
        institutionId: userInfo.value.institutionId,
        // licenseDeviceName:formData.licenseDeviceName, // 这个下一步会用到
        licenseDeviceName: deviceOptions.value.find((item) => item.value === formData.licenseDeviceName)?.label,
        ladderConfigModel: modelOptions.value.find((item) => item.value === formData.ladderConfigModel)?.label,
        appType: appType,
        appStatus: 1,
      };
      if (formData.id) {
        params.id = formData.id;
      }
      if (formData.appStatus) {
        params.appStatus = formData.appStatus;
      }
      if (formAllData.value.institutionId) {
        params.institutionId = formAllData.value.institutionId;
      }
      loading.value = true;
      let response = await (formData.id ? updateApply(params) : createApply(params));
      formData.appNoId = formData.id ? formData.id : response.data; // 申请Id 资料上传需要
      formData.appStatus = 1 // 前端状态
      loading.value = false;
      submitSuccess.value = true;
      resolve(JSON.parse(JSON.stringify(formData)));
    } catch (err) {
      loading.value = false;
      reject(err);
    }
  });
};
defineExpose({
  validor,
  submit,
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px 20px;
  &:deep(.el-form-item) {
    margin: 0;
    --el-text-color-placeholder: #555555;
    .el-form-item__label {
      font-size: 14px;
      color: #666;
      font-weight: 700;
    }
    .el-select {
      width: 100%;
      .el-select__wrapper {
        line-height: 32px;
        min-height: 40px;
      }
    }
    .el-input {
      --el-input-height: 40px;
    }
  }
  .grid-item-l-2 {
    grid-column: 1/3;
  }
  .grid-item-r-2 {
    grid-column: 3/5;
  }
  .grid-item-row {
    grid-column: 1/5;
  }
}
</style>
