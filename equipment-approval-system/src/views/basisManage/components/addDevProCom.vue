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
      <el-form-item label="企业简称" prop="abbreviation">
        <el-input
          v-model="formData.abbreviation"
          placeholder="请输入企业简称"
          style="width: 100%"
          clearable
        />
      </el-form-item>
      <!-- 设备类型 -->
      <el-form-item class="el-form-item--span-row" label="主要设备类型" prop="mainEquipmentType">
        <el-select
          v-model="formData.mainEquipmentType"
          placeholder="请选择设备类型"
          style="width: 100%"
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
      <el-form-item class="el-form-item--span-row" label="备注信息" prop="remark">
        <el-input
          type="textarea"
          :autosize="{ minRows: 8, maxRows: 12 }"
          v-model="formData.remark"
          placeholder="请输入备注信息"
          style="width: 100%"
          clearable
        />
      </el-form-item>
      <!-- 企业状态 -->
      <el-form-item label="企业状态" prop="status">
        <el-switch
          v-model="formData.status"
          :active-value="1"
          :inactive-value="0"
          active-text="启用"
          inactive-text="禁用"
          :disabled="isView"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { createproductionCompany, editproductionCompany } from '@/api/biz/basisManagement'
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
  abbreviation: string
  mainEquipmentType: string | string[]
  createTime?: string
  status: string | number
  remark: string
}
let formData = reactive<tableDataType>({
  companyName: '',
  abbreviation: '',
  mainEquipmentType: '',
  status: '',
  remark: ''
})
let rules = ref({
  companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  abbreviation: [{ required: false, message: '请输入企业简称', trigger: 'blur' }],
  mainEquipmentType: [{ required: true, message: '请选择主要设备类型', trigger: 'change' }],
  status: [{ required: true, message: '请选择企业状态', trigger: 'change' }],
  remark: [{ required: false, message: '请输入备注信息', trigger: 'blur' }]
})

let formRef = ref<FormInstance | null>(null)
let submitFormFn = async () => {
  await formRef.value?.validate()
  try {
    loading.value = true
    if (isEdit.value) {
      await editproductionCompany(formData)
    } else {
      await createproductionCompany(formData)
    }
    ElMessage.success('操作成功')
    return true
  } catch (err) {
    return false
  } finally {
    loading.value = false
  }
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
