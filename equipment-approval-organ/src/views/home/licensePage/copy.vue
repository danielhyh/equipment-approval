<template>
  <el-form class="grid-form" :model="formData" :rules="rules" ref="formRef" label-position="top">
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
    <el-form-item label="备注信息" prop="remark" class="grid-item-row">
      <el-input v-model="formData.remark" type="textarea" :autosize="{ minRows: 4, maxRows: 8 }" placeholder="请输入备注信息" />
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive } from "vue";

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
});
let rules = ref({
  productionEnterprise: [{ required: true, message: "请输入生产企业", trigger: ["blur"] }],
  specificModel: [{ required: true, message: "请输入具体型号", trigger: ["blur"] }],
  productSerialNo: [{ required: true, message: "请输入产品序列号", trigger: ["blur"] }],
  installationDate: [{ required: true, message: "请输入装机日期", trigger: ["blur"] }],
  infoSubmitDate: [{ required: true, message: "请输入信息报送日期", trigger: ["blur"] }],
  duplicateIssueDate: [{ required: true, message: "请输入副本发证日期", trigger: ["blur"] }],
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap:  20px;
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
  .grid-item-row {
    grid-column: 1/5;
  }
}
</style>
