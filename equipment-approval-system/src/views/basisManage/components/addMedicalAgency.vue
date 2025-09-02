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
      <el-form-item label="机构名称" prop="orgName">
        <el-input v-model="formData.orgName" placeholder="请输入机构名称" />
      </el-form-item>
      <!-- 法定代表 -->
      <el-form-item label="法定代表人" prop="legalPerson">
        <el-input v-model="formData.legalPerson" placeholder="请输入法定代表" />
      </el-form-item>
      <!-- 社会统一信用代码 -->
      <el-form-item label="社会统一信用代码" prop="creditCode">
        <el-input v-model="formData.creditCode" placeholder="请输入社会统一信用代码" />
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
      <el-form-item label="机构级别" prop="orgLevel">
        <el-select v-model="formData.orgLevel" placeholder="请选择机构类型">
          <el-option
            v-for="item in orgLevelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 机构类型 -->
      <el-form-item label="机构类型" prop="orgType">
        <el-select v-model="formData.orgType" placeholder="请选择机构类型">
          <el-option
            v-for="item in orgTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 所有制性质 -->
      <el-form-item label="所有制性质" prop="ownershipType">
        <el-select v-model="formData.ownershipType" placeholder="请选择所有制性质">
          <el-option
            v-for="item in ownershipTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 所属区域 -->
      <el-form-item label="所属区域" prop="area">
        <el-select v-model="formData.area" placeholder="请选择所属区域">
          <el-option
            v-for="item in areaOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 设备数量 -->
      <el-form-item label="设备数量" prop="deviceCount">
        <el-input v-model="formData.deviceCount" placeholder="请输入设备数量" />
      </el-form-item>
      <!-- 详情地址 -->
      <el-form-item class="el-form-item--span-row" label="详情地址" prop="detailAddress">
        <el-input
          v-model="formData.detailAddress"
          type="textarea"
          :autosize="{ minRows: 7 }"
          placeholder="请输入详情地址"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
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
// 机构级别
const orgLevelOptions = computed<DictDataType[]>(() => getDictOptions('biz_institution_level'))
// 机构类型
const orgTypeOptions = computed<DictDataType[]>(() => getDictOptions('biz_institution_type'))
// 所有制性质
const ownershipTypeOptions = computed<DictDataType[]>(() => getDictOptions('biz_ownership_nature'))
// 所属区域
const areaOptions = computed<DictDataType[]>(() => getDictOptions('biz_area_list'))
interface tableDataType {
  id?: number | string
  // 机构名称
  orgName: string
  // 法定代表
  legalPerson: string
  // 社会统一信用代码
  creditCode: string
  // 机构级别
  orgLevel: string
  // 机构类型
  orgType?: string
  // 所有制性质
  ownershipType: string
  // 所属区域
  area: string
  // 设备数量
  deviceCount: number | string
  // 上级机构
  parentOrg?: string
  // 详情地址
  detailAddress?: string
  // 联系人
  contactPerson?: string
  // 联系电话
  contactPhone?: string
  // 营业执照
  businessLicense?: string | string[]
}
let formData = reactive<tableDataType>({
  orgName: '',
  legalPerson: '',
  creditCode: '',
  orgLevel: '',
  orgType: '',
  ownershipType: '',
  area: '',
  deviceCount: '',
  parentOrg: '',
  detailAddress: '',
  contactPerson: '',
  businessLicense: ''
})
let rules = ref({
  orgName: [{ required: true, message: '请输入机构名称', trigger: 'blur' }],
  legalPerson: [{ required: true, message: '请输入法定代表', trigger: 'blur' }],
  creditCode: [{ required: true, message: '请输入社会统一信用代码', trigger: 'blur' }],
  orgLevel: [{ required: true, message: '请选择机构级别', trigger: 'change' }],
  ownershipType: [{ required: true, message: '请选择所有制性质', trigger: 'change' }],
  area: [{ required: true, message: '请选择所属区域', trigger: 'change' }],
  deviceCount: [{ required: true, message: '请输入设备数量', trigger: 'blur' }],
  parentOrg: [{ required: true, message: '请选择上级机构', trigger: 'change' }],
  detailAddress: [{ required: true, message: '请输入详情地址', trigger: 'blur' }],
  contactPerson: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  businessLicense: [{ required: true, message: '请上传营业执照', trigger: 'change' }]
})

let formRef = ref<FormInstance | null>(null)
let submitFormFn = async () => {
  await formRef.value?.validate()
  loading.value = true
  setTimeout(() => {
    // 发送数据
    loading.value = false
  }, 3000)
}
onMounted(() => {
  if (isView || isEdit) {
    formData = Object.assign(formData, props.row)
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
    }
  }
}
</style>
