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
      <!-- 企业名称 -->
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model="formData.companyName"
          placeholder="请输入企业名称"
          style="width: 100%"
          clearable
        />
      </el-form-item>
      <!-- 企业简称 -->
      <el-form-item label="企业简称" prop="shortName">
        <el-input
          v-model="formData.shortName"
          placeholder="请输入企业简称"
          style="width: 100%"
          clearable
        />
      </el-form-item>
      <!-- 设备类型 -->
      <el-form-item class="el-form-item--span-row" label="主要设备类型" prop="mainDeviceType">
        <el-select
          v-model="formData.mainDeviceType"
          placeholder="请选择设备类型"
          style="width: 100%"
          multiple
          clearable
        >
          <el-option
            v-for="item in licenseDeviceOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <!-- 备注信息 -->
      <el-form-item class="el-form-item--span-row" label="备注信息" prop="remarks">
        <el-input
          type="textarea"
          :autosize="{ minRows: 8, maxRows: 12 }"
          v-model="formData.remarks"
          placeholder="请输入备注信息"
          style="width: 100%"
          clearable
        />
      </el-form-item>
      <!-- 企业状态 -->
      <el-form-item label="企业状态" prop="status">
        <el-switch
          v-model="formData.status"
          active-value="0"
          inactive-value="1"
          active-text="启用"
          inactive-text="禁用"
          :disabled="isView"
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
// 设备类型
const licenseDeviceOptions = computed<DictDataType[]>(() =>
  getDictOptions('biz_main_equipment_type')
)

interface tableDataType {
  id?: number | string
  companyName: string
  shortName: string
  mainDeviceType: string | string[]
  registrationDate?: string
  status: string
  remarks: string
}
let formData = reactive<tableDataType>({
  companyName: '',
  shortName: '',
  mainDeviceType: '',
  status: '',
  remarks: ''
})
let rules = ref({
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  shortName: [{ required: true, message: '请输入企业简称', trigger: 'blur' }],
  mainDeviceType: [{ required: true, message: '请选择主要设备类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择企业状态', trigger: 'change' }],
  remarks: [{ required: true, message: '请输入备注信息', trigger: 'blur' }]
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
