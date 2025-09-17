<template>
  <el-form :model="licenseOriginBasis" disabled label-position="top" class="grid-form" v-loading="loading">
    <el-form-item label="配置单位名称" prop="configUnitName">
      <el-input v-model="licenseOriginBasis.configUnitName" placeholder="请输入配置单位名称" />
    </el-form-item>
    <el-form-item label="统一社会信用代码" prop="unifiedSocialCreditCode">
      <el-input v-model="licenseOriginBasis.unifiedSocialCreditCode" placeholder="请输入统一社会信用代码" />
    </el-form-item>
    <el-form-item label="法定代表人" prop="legalPerson">
      <el-input v-model="licenseOriginBasis.legalPerson" placeholder="请输入法定代表人" />
    </el-form-item>
    <el-form-item label="许可设备名称" prop="licenseDeviceName">
      <el-input v-model="licenseOriginBasis.licenseDeviceName" placeholder="请输入许可设备名称" />
    </el-form-item>
    <el-form-item label="所有制性质" prop="ownershipNature">
      <el-input v-model="licenseOriginBasis.ownershipNature" placeholder="请输入所有制性质" />
    </el-form-item>
    <el-form-item label="阶梯配置机型" prop="ladderConfigModel">
      <el-input v-model="licenseOriginBasis.ladderConfigModel" placeholder="请输入阶梯配置机型" />
    </el-form-item>
    <el-form-item label="设备配置地址" prop="equipmentConfigAddress">
      <el-input v-model="licenseOriginBasis.equipmentConfigAddress" placeholder="请输入设备配置地址" />
    </el-form-item>
    <el-form-item label="详细地址" prop="detailedAddress">
      <el-input v-model="licenseOriginBasis.detailedAddress" placeholder="请输入详细地址" />
    </el-form-item>
    <el-form-item label="发证机关" prop="issuingAuthority">
      <el-input v-model="licenseOriginBasis.issuingAuthority" placeholder="请输入详细地址" />
    </el-form-item>
  </el-form>
</template>

<script setup>
import { getLicenseOrigin } from "@/apis/home";
import { useBasisStore } from "@/pinia/modules/basis";
const basisStore = useBasisStore();
const licesneBasis = computed(() => basisStore.getLicenseBasis);

let licenseOriginBasis = reactive({
  configUnitName: "", // 配置单位名称
  unifiedSocialCreditCode: "", // 统一社会信用代码
  legalPerson: "", // 法定代表人
  licenseDeviceName: "", // 许可设备名称
  ownershipNature: "", // 所有制性质
  ladderConfigModel: "", // 阶梯配置机型
  equipmentConfigAddress: "", // 设备配置地址
  detailedAddress: "", // 详细地址
  issuingAuthority: "", // 发证机关
  issueDate: "", // 发证日期
});
let loading = ref(false);
const getOrigin = () => {
  loading.value = true;
  getLicenseOrigin(licesneBasis.value.originalId)
    .then((res) => {
      licenseOriginBasis = Object.assign(licenseOriginBasis, res.data);
    })
    .finally(() => {
      loading.value = false;
    });
};
const submit = ()=>{}
onMounted(() => {
  getOrigin();
});
</script>

<style lang="scss" scoped>
.grid-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px 20px;
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
  }
}
</style>
