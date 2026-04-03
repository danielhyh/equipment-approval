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

    <!-- 注销类型：选择已有许可证 -->
    <template v-if="appType === 6">
      <el-form-item label="选择要注销的许可证*" prop="selectedLicenseId" class="grid-item-row">
        <el-select
          v-model="formData.selectedLicenseId"
          placeholder="请选择要注销的许可证"
          clearable
          :disabled="submitSuccess"
          @change="onLicenseChange"
        >
          <el-option
            v-for="item in licenseOptions"
            :key="item.originalId"
            :label="`${item.licenseNo} - ${item.licenseDeviceName}（${item.ladderConfigModel || ''}）`"
            :value="item.originalId"
          />
        </el-select>
      </el-form-item>
    </template>

    <!-- 非注销类型：原有的设备名称和机型选择 -->
    <template v-else>
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
    </template>

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
import { createApply, updateApply, getApplyReviewedList } from "@/apis/applyFor";
import { getLicenseList } from "@/apis/home";
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
  selectedLicenseId: null, // 注销时选择的许可证正本ID
});
let rules = reactive(
  appType === 6
    ? {
        selectedLicenseId: [{ required: true, message: "请选择要注销的许可证", trigger: "change" }],
      }
    : {
        licenseDeviceName: [{ required: true, message: "请选择许可设备名称", trigger: "blur" }],
        ladderConfigModel: [{ required: true, message: "请输入阶梯配置机型", trigger: "blur" }],
      }
);
let userInfo = computed(() => userStore.getUser);
// 数据初始化
watch(
  () => userInfo.value,
  (user) => {
    Object.keys(formData).forEach((key) => {
      if (key !== "selectedLicenseId") {
        formData[key] = user[key] || formData[key];
      }
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
let applyListData = ref([]);
const getChangeReviewedList = async () => {
  let { data } = await getApplyReviewedList();
  applyListData.value = data;
};

// 注销：许可证列表
let licenseOptions = ref([]);
let selectedLicenseInfo = ref(null); // 选中的许可证完整信息
const fetchLicenseList = async () => {
  try {
    let { data } = await getLicenseList({ pageSize: 999, pageNum: 1 });
    licenseOptions.value = (data?.list || []).filter((item) => item.originalId);
  } catch (err) {
    console.error("获取许可证列表失败", err);
  }
};
// 选择许可证时回填信息，将设备名称转为字典编号
const onLicenseChange = (originalId) => {
  let license = licenseOptions.value.find((item) => item.originalId === originalId);
  selectedLicenseInfo.value = license || null;
  if (license) {
    // 将许可证的设备名称文本反查为字典编号，供后续步骤使用
    let deviceDict = dictStore.getDictTypeList("biz_main_equipment_type").find((d) => d.label === license.licenseDeviceName);
    let modelDict = dictStore.getDictTypeList("biz_ladder_config_model").find((d) => d.label === license.ladderConfigModel);
    formData.licenseDeviceName = deviceDict?.value || license.licenseDeviceName;
    formData.ladderConfigModel = modelDict?.value || license.ladderConfigModel;
  } else {
    formData.licenseDeviceName = "";
    formData.ladderConfigModel = "";
    selectedLicenseInfo.value = null;
  }
};

// 许可设备
let deviceOptions = computed(() => {
  if (appType === 1) {
    return dictStore.getDictTypeList("biz_main_equipment_type");
  }
  return dictStore.getDictTypeList("biz_main_equipment_type").map((item) => {
    let apply = applyListData.value.find((apply) => apply.licenseDeviceName === item.label) || null;
    return {
      ...item,
      disabled: !apply,
    };
  });
});
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
        appType: appType,
        appStatus: 1,
      };

      if (appType === 6) {
        // 注销：使用选中许可证的信息
        let license = selectedLicenseInfo.value;
        params.licenseDeviceName = license?.licenseDeviceName || "";
        params.ladderConfigModel = license?.ladderConfigModel || "";
        // 将正本、副本、设备ID存入extra
        params.extra = {
          originalId: license?.originalId || null,
          duplicateId: license?.duplicateId || null,
          equipmentId: license?.equipmentId || null,
        };
      } else {
        params.licenseDeviceName = deviceOptions.value.find((item) => item.value === formData.licenseDeviceName)?.label;
        params.ladderConfigModel = modelOptions.value.find((item) => item.value === formData.ladderConfigModel)?.label;
      }

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
      formData.appStatus = 1; // 前端状态 待初审
      loading.value = false;
      submitSuccess.value = true;
      resolve(JSON.parse(JSON.stringify(formData)));
    } catch (err) {
      loading.value = false;
      reject(err);
    }
  });
};
onMounted(() => {
  if (appType === 6) {
    // 注销：加载许可证列表
    fetchLicenseList();
  } else if (appType !== 1) {
    getChangeReviewedList();
  }
});
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
