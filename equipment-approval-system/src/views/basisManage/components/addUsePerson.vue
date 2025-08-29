<template>
  <el-form :model="userFormData" :rules="usePersonRules" ref="userFormRef" label-position="top">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="userFormData.name" placeholder="请输入姓名" clearable />
    </el-form-item>
    <el-form-item label="联系电话" prop="phone">
      <el-input v-model="userFormData.phone" placeholder="请输入联系电话" clearable />
    </el-form-item>
    <el-form-item label="身份证号" prop="idCard">
      <el-input
        v-model="userFormData.idCard"
        placeholder="请输入身份证号"
        clearable
        @blur="linkageBirthDate"
      />
    </el-form-item>
    <el-form-item label="性别" prop="gender">
      <el-select v-model="userFormData.gender" placeholder="请选择性别" clearable>
        <el-option label="男" value="男" />
        <el-option label="女" value="女" />
      </el-select>
    </el-form-item>
    <el-form-item label="出生日期" prop="birthDate">
      <el-date-picker
        v-model="userFormData.birthDate"
        type="date"
        value-format="YYYY-MM-DD"
        placeholder="请选择出生日期"
        style="width: 100%"
      />
    </el-form-item>
    <el-form-item label="职称" prop="careerTitle">
      <el-input v-model="userFormData.careerTitle" placeholder="请输入职称" clearable />
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts" name="AddUsePerson">
import type { FormInstance } from 'element-plus'
// 使用人员
interface UsePersonType {
  id?: string
  name: string
  phone: string
  idCard: string
  // 性别
  gender: string
  // 出生日期
  birthDate: string
  // 联系电话
  contactPhone: string
  // 职称
  careerTitle: string
}

// el-form 实例
let userFormRef = ref<FormInstance | null>(null)
let userFormData = ref<UsePersonType>({
  name: '',
  phone: '',
  idCard: '',
  gender: '',
  birthDate: '',
  contactPhone: '',
  careerTitle: ''
})
let usePersonRules = ref({
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    {
      pattern:
        /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/,
      message: '请输入正确的身份证号',
      trigger: 'blur'
    }
  ],
  gender: [{ required: true, message: '请输入性别', trigger: 'blur' }],
  birthDate: [{ required: true, message: '请选择出生日期', trigger: 'blur' }],
  contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  careerTitle: [{ required: true, message: '请输入职称', trigger: 'blur' }]
})
let isEditIndex = ref(-1)

const linkageBirthDate = () => {
  let text = userFormData.value.idCard
  let reg = new RegExp(
    /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
  )
  console.log(text, reg.test(text))
  if (text && reg.test(text)) {
    userFormData.value.birthDate =
      text.substring(6, 10) + '-' + text.substring(10, 12) + '-' + text.substring(12, 14)
  }
}

const resetUserForm = () => {
  userFormData.value = {
    name: '',
    phone: '',
    idCard: '',
    gender: '',
    birthDate: '',
    contactPhone: '',
    careerTitle: ''
  }
  isEditIndex.value = -1
}
const editUserForm = (v, index) => {
  isEditIndex.value = index
  userFormData.value = v
}
const submit = () => {
  return new Promise((resolve, reject) => {
    if (!userFormRef.value) {
      ElMessage.error('表单加载错误')
      reject({ type: 'error', msg: '表单加载错误' })
      return
    }
    userFormRef.value.validate((valid) => {
      if (valid) {
        ElMessage.success('提交成功')
        resolve({ value: userFormData.value, isEditIndex: isEditIndex.value })
      } else {
        ElMessage.error('请填写完整信息')
        reject({ type: 'error', msg: '请填写完整信息' })
      }
    })
  })
}

defineExpose({
  submit,
  resetUserForm,
  editUserForm
})
</script>

<style lang="scss" scoped></style>
