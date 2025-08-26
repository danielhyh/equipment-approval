<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="专家姓名" prop="name">
        <el-input v-model="formData.name" placeholder="请输入专家姓名" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-select v-model="formData.gender">
          <el-option
                v-for="dict in getDictOptions(DICT_TYPE.SYSTEM_USER_SEX)"
                     :key="dict.id"
                     :label="dict.label"
                     :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="formData.age" placeholder="请输入年龄" />
      </el-form-item>
      <el-form-item label="工作单位" prop="unit">
        <el-input v-model="formData.unit" placeholder="请输入工作单位" />
      </el-form-item>
      <el-form-item label="职称" prop="title">
        <el-input v-model="formData.title" placeholder="请输入职称" />
      </el-form-item>
      <el-form-item label="职务" prop="title">
        <el-input v-model="formData.position" placeholder="请输入职务" />
      </el-form-item>
      <el-form-item label="专业领域" prop="specialty">
        <el-select v-model="formData.specialty">
          <el-option
            v-for="item in categoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input v-model="formData.phone" placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formData.email" placeholder="请输入邮箱" />
      </el-form-item>

    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ExpertExtApi, ExpertExt } from '@/api/biz/expertext'
import { getDictOptions,DICT_TYPE } from '@/utils/dict'

/** 专家扩展信息 表单 */
defineOptions({ name: 'ExpertExtForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗
const categoryOptions = ref( [
  { value: 'radiology', label: '放射影像' },
  { value: 'radiotherapy', label: '放射治疗' },
  { value: 'nuclear_medicine', label: '核医学' },
  { value: 'health_management', label: '卫生管理' },
  { value: 'linear_accelerator', label: '直线加速器' },
  { value: 'medical_equipment', label: '医学设备与安全防护' },
  { value: 'medical_intelligence', label: '医学智能工程' }
])
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  gender: undefined,
  age: undefined,
  unit: undefined,
  title: undefined,
  specialty: undefined,
  phone: undefined,
  email: undefined,
  position: undefined,
})
const formRules = reactive({
  name: [{ required: true, message: '专家姓名不能为空', trigger: 'blur' }],
  gender: [{required: true, message: '专家性别不能为空', trigger: 'blur' }],
  age: [{required: true, message: '专家年龄不能为空', trigger: 'blur' }],
  unit: [{required: true, message: '专家工作单位不能为空', trigger: 'blur' }],
  title: [{required: true, message: '专家职称不能为空', trigger: 'blur' }],
  specialty: [{required: true, message: '专家专业领域不能为空', trigger: 'blur' }],
  phone: [{required: true, message: '专家手机号不能为空', trigger: 'blur' }],
  position: [{required: true, message: '专家职务不能为空', trigger: 'blur' }],
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ExpertExtApi.getExpertExt(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ExpertExt
    if (formType.value === 'create') {
      await ExpertExtApi.createExpertExt(data)
      message.success(t('common.createSuccess'))
    } else {
      await ExpertExtApi.updateExpertExt(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: undefined,
    gender: undefined,
    age: undefined,
    unit: undefined,
    title: undefined,
    specialty: undefined,
    phone: undefined,
    email: undefined,
    position: undefined
  }
  formRef.value?.resetFields()
}
</script>
