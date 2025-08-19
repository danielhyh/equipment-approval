<template>
  <div class="review-page">
    <div class="page-b-p">
      <div class="title">
        <Icon icon="heroicons:document-check-solid" :size="24" color="#165DFF" />
        <span>初步审核</span>
      </div>
      <el-form
        :model="formValue"
        :rules="rules"
        ref="formRef"
        label-width="120px"
        class="demo-ruleForm"
        label-position="top"
      >
        <el-form-item label="审核结果" prop="reivew">
          <el-radio-group v-model="formValue.reivew">
            <el-radio label="1">通过</el-radio>
            <el-radio label="2">不通过</el-radio>
            <el-radio label="3">退回补充材料</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="remark">
          <el-input
            v-model="formValue.remark"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 6 }"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts" name="Perliminary">
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
interface formType {
  reivew: string | number | null
  remark: string
}
let formValue = ref<formType>({
  reivew: null,
  remark: ''
})

let formRef = ref<FormInstance | null>(null)
let rules = reactive({
  reivew: [{ required: true, message: '请选择审核结果', trigger: 'blur' }],
  remark: [{ required: false, message: '请输入审核备注', trigger: 'blur' }]
})

const submitFn = async () => {
  if (!formRef.value) {
    ElMessage.error('表单加载错误')
    return
  }
  try {
    let valid = await formRef.value.validate()
    if (!valid) {
      ElMessage.error('请填写完整信息')
      return
    }
  } catch (err) {}
}

defineExpose({
  submitFn
})
</script>

<style lang="scss" scoped>
.review-page {
  padding: 0 10px;
  .page-b-p {
    background-color: rgba(248, 250, 252, 0.8);
    border: 1px solid rgba(226, 232, 240, 0.6);
    border-radius: 12px;
    padding: 10px 14px;
    margin-top: 20px;
  }
  .title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    padding-bottom: 8px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    span {
      margin-left: 6px;
    }
  }
  &:deep(.demo-ruleForm) {
    padding: 20px 0;
    .el-form-item {
      .el-form-item__label {
        color: #333;
        font-weight: 600;
      }
    }
  }
}
</style>
