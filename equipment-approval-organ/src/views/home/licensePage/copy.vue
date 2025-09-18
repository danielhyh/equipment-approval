<template>
  <el-form class="grid-form" :model="formData" :rules="rules" ref="formRef" label-position="top" :disabled="disabled">
    <el-form-item label="生产企业" prop="productionEnterprise">
      <el-input v-model="formData.productionEnterprise" placeholder="请输入生产企业" />
    </el-form-item>
    <el-form-item label="具体型号" prop="specificModel">
      <el-input v-model="formData.specificModel" placeholder="请输入具体型号" />
    </el-form-item>
    <el-form-item label="产品序列号" prop="productSerialNo">
      <el-input v-model="formData.productSerialNo" placeholder="请输入产品序列号" />
    </el-form-item>
    <el-form-item label="装机日期" prop="installationDate">
      <el-date-picker v-model="formData.installationDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择装机日期" />
    </el-form-item>
    <el-form-item label="信息报送日期" prop="infoSubmitDate">
      <el-date-picker v-model="formData.infoSubmitDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择信息报送日期" />
    </el-form-item>
    <el-form-item label="副本发证日期" prop="duplicateIssueDate">
      <el-date-picker
        v-model="formData.duplicateIssueDate"
        type="date"
        value-format="YYYY-MM-DD"
        placeholder="请选择副本发证日期"
      />
    </el-form-item>
    <el-form-item label="副本发证机关" prop="duplicateIssuingAuthority">
      <el-input v-model="formData.duplicateIssuingAuthority" placeholder="请输入副本发证机关" disabled />
    </el-form-item>
    <el-form-item label="采购价格" prop="purchasePrice">
      <el-input v-model="formData.purchasePrice" placeholder="请输入采购价格" type="number" clearable>
        <template #prepend>￥</template>
        <template #append>元</template>
      </el-input>
    </el-form-item>
    <el-form-item label="设备特殊说明" prop="specialDescription" class="grid-item-1-2">
      <el-input
        v-model="formData.specialDescription"
        type="textarea"
        :autosize="{ minRows: 4, maxRows: 8 }"
        placeholder="请输入备注信息"
      />
    </el-form-item>
    <el-form-item label="备注信息" prop="remark" class="grid-item-2-4">
      <el-input v-model="formData.remark" type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入备注信息" />
    </el-form-item>
  </el-form>
</template>

<script setup>
import { getLicenseCopy, submitCopy } from "@/apis/home";
import { useBasisStore } from "@/pinia/modules/basis";
import { onMounted } from "vue";
let basisStore = useBasisStore();
let licenseBasis = computed(() => basisStore.getLicenseBasis);
let props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
});
let formRef = ref(null);
let formData = reactive({
  // 生产企业
  productionEnterprise: "",
  // 具体型号
  specificModel: "",
  // 产品序列号
  productSerialNo: "",
  // 装机日期
  installationDate: "",
  // 信息报送日期
  infoSubmitDate: "",
  // 副本发证机关
  duplicateIssuingAuthority: "陕西省卫生健康委员会",
  // 副本发证日期
  duplicateIssueDate: "",
  // 备注信息
  remark: "",
  purchasePrice: "", // 采购价格
  specialDescription: "", //设备特殊说明
});
let rules = ref({
  productionEnterprise: [{ required: true, message: "请输入生产企业", trigger: ["blur"] }],
  specificModel: [{ required: true, message: "请输入具体型号", trigger: ["blur"] }],
  productSerialNo: [{ required: true, message: "请输入产品序列号", trigger: ["blur"] }],
  installationDate: [{ required: true, message: "请输入装机日期", trigger: ["blur"] }],
  infoSubmitDate: [{ required: true, message: "请输入信息报送日期", trigger: ["blur"] }],
  duplicateIssueDate: [{ required: true, message: "请输入副本发证日期", trigger: ["blur"] }],
  purchasePrice: [{ required: true, message: "请输入采购价格", trigger: ["blur"] }],
});
// 获取副本信息
let loading = ref(false);
const getCopyInfo = () => {
  if (!licenseBasis.value.duplicateId) return;
  loading.value = true;
  getLicenseCopy(licenseBasis.value.duplicateId)
    .then((res) => {
      formData = Object.assign(formData, res.data);
    })
    .finally(() => {
      loading.value = false;
    });
};
// 校验
const validor = async () => {
  try {
    return await formRef.value.validate();
  } catch (err) {
    ElMessage.error("请填写完整信息");
    return false;
  }
};
const submit = async () => {
  try {
    let valid = await validor();
    if (!valid || loading.value) return false;
    loading.value = true;
    let params = {
      ...formData,
      originalId: licenseBasis.value.originalId,
    }
    await submitCopy(params);
    ElMessage.success("提交成功");
    return true
  } catch (err) {
    ElMessage.error("提交失败");
    return false
  }
};

onMounted(() => {
  getCopyInfo();
});
defineExpose({
  submit,
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  &:deep(.el-form-item) {
    margin: 0;
    --el-disabled-text-color: #171717;
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
    .el-date-editor {
      width: 100%;
    }
  }
  .grid-item-1-2 {
    grid-column: 1/3;
  }
  .grid-item-2-4 {
    grid-column: 3/5;
  }
}
</style>
