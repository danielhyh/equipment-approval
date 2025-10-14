<template>
  <div class="dialog-content-page" v-loading="loading">
    <el-form
      class="grid-form-style"
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-position="top"
      :disabled="loading || isView"
    >
      <!-- 机构名称 -->
      <el-form-item label="机构名称" prop="institutionName">
        <el-input v-model="formData.institutionName" placeholder="请输入机构名称" />
      </el-form-item>
      <!-- 法定代表 -->
      <el-form-item label="法定代表人" prop="legalPerson">
        <el-input v-model="formData.legalPerson" placeholder="请输入法定代表" />
      </el-form-item>
      <!-- 社会统一信用代码 -->
      <el-form-item label="社会统一信用代码" prop="unifiedSocialCreditCode">
        <el-input
          v-model="formData.unifiedSocialCreditCode"
          placeholder="请输入社会统一信用代码"
          show-word-limit
          maxlength="18"
        />
      </el-form-item>
      <!-- 联系人 -->
      <el-form-item label="联系人" prop="contactPerson">
        <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
      </el-form-item>
      <!-- 联系电话 -->
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
      </el-form-item>
      <!-- 机构级别 -->
      <el-form-item label="机构级别" prop="institutionLevel">
        <el-select v-model="formData.institutionLevel" placeholder="请选择机构类型">
          <el-option
            v-for="item in orgLevelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <!-- 机构类型 -->
      <el-form-item label="机构类型" prop="institutionType">
        <el-select v-model="formData.institutionType" placeholder="请选择机构类型">
          <el-option
            v-for="item in orgTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 所有制性质 -->
      <el-form-item label="所有制性质" prop="ownershipNature">
        <el-select v-model="formData.ownershipNature" placeholder="请选择所有制性质">
          <el-option
            v-for="item in ownershipTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <!-- 所属区域 -->
      <el-form-item label="所属区域" prop="region">
        <el-select v-model="formData.region" placeholder="请选择所属区域">
          <el-option
            v-for="item in areaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <!-- 详情地址 -->
      <el-form-item class="el-form-item--span-row" label="详情地址" prop="detailAddress">
        <el-input
          v-model="formData.detailAddress"
          type="textarea"
          :autosize="{ minRows: 5 }"
          placeholder="请输入详情地址"
        />
      </el-form-item>
      <!-- 营业执照 -->
      <el-form-item class="el-form-item--span-row" label="营业执照" prop="businessLicensePic">
        <UploadFile
          v-model:model-value="formData.businessLicensePic"
          :fileType="['png', 'jpg', 'jpeg']"
          :drag="true"
          :file-size="10"
          :limit="1"
          :auto-upload="true"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { createHospital, editHospital } from '@/api/biz/basisManagement'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import type { FormInstance } from 'element-plus'
let props = defineProps({
  row: {
    type: Object,
    default: () => ({})
  },
  type: { type: String, default: 'view' }
})
let isView = computed(() => props.type === 'view')
let isEdit = computed(() => props.type === 'edit')
let loading = ref(false)
interface DictDataTypeT extends DictDataType {
  value: string | number
}
// 机构级别
const orgLevelOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_institution_level'))
// 机构类型
const orgTypeOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_institution_type'))
// 所有制性质
const ownershipTypeOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_ownership_nature'))
// 所属区域
const areaOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_area_list'))
interface tableDataType {
  id?: number | string
  // 机构名称
  institutionName: string
  // 法定代表
  legalPerson: string
  // 社会统一信用代码
  unifiedSocialCreditCode: string
  // 机构级别
  institutionLevel: string
  // 机构类型
  institutionType?: string | number
  // 所有制性质
  ownershipNature: string
  // 所属区域
  region: string
  // 上级机构
  parentOrg?: string
  // 详情地址
  detailAddress?: string
  // 联系人
  contactPerson?: string
  // 联系电话
  contactPhone?: string
  // 营业执照
  businessLicensePic: string | string[]
}
let formData = reactive<tableDataType>({
  institutionName: '',
  legalPerson: '',
  unifiedSocialCreditCode: '',
  institutionLevel: '',
  institutionType: '',
  ownershipNature: '',
  region: '',
  detailAddress: '',
  contactPerson: '',
  businessLicensePic: []
})
let rules = ref({
  institutionName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
  legalPerson: [{ required: true, message: '请输入法定代表', trigger: 'blur' }],
  unifiedSocialCreditCode: [
    { required: true, message: '请输入社会统一信用代码', trigger: 'blur' },
    { pattern: /^[0-9A-Z]{18}$/, message: '请输入正确的社会统一信用代码', trigger: 'blur' }
  ],
  institutionLevel: [{ required: true, message: '请选择机构级别', trigger: 'change' }],
  institutionType: [{ required: true, message: '请选择机构类型', trigger: 'change' }],
  ownershipNature: [{ required: true, message: '请选择所有制性质', trigger: 'change' }],
  region: [{ required: true, message: '请选择所属区域', trigger: 'change' }],
  detailAddress: [{ required: false, message: '请输入详情地址', trigger: 'blur' }],
  contactPerson: [{ required: false, message: '请输入联系人', trigger: 'blur' }],
  contactPhone: [
    { required: false, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的联系电话', trigger: 'blur' }
  ],
  businessLicensePic: [{ required: false, message: '请上传营业执照', trigger: 'change' }]
})

let formRef = ref<FormInstance | null>(null)
let submitFormFn = async () => {
  await formRef.value?.validate()
  try {
    loading.value = true
    let params: tableDataType = {
      ...formData,
      institutionType: formData?.institutionType ? Number(formData.institutionType) : '',
      businessLicensePic: formData?.businessLicensePic
    }
    if (isEdit.value) {
      await editHospital(params)
    } else {
      await createHospital(params)
    }
    ElMessage.success('操作成功')
    return true
  } catch (e) {
    return false
  } finally {
    loading.value = false
  }
}
onMounted(() => {
  if (isView || isEdit) {
    let row = JSON.parse(JSON.stringify(props.row))
    row.businessLicensePic = row.businessLicensePic ? row.businessLicensePic : ''
    row.institutionType = row.institutionType ? row.institutionType + '' : ''
    row.formData = Object.assign(formData, row)
  }
})
defineExpose({
  loading,
  submitFormFn
})
</script>

<style lang="scss" scoped>
.dialog-content-page {
  width: 100%;
  .grid-form-style {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    .el-form-item {
      margin: 0;
    }
    .el-form-item--span-row {
      grid-column: 1/3;
      &:deep(.upload-file) {
        width: 100%;
        .upload-file-tip {
          > div {
            font-size: 12px !important;
          }
        }
      }
    }
  }
}
</style>
