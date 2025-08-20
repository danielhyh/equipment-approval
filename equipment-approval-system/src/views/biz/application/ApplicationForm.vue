<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="申请编号" prop="appNo">
        <el-input v-model="formData.appNo" placeholder="请输入申请编号" />
      </el-form-item>
      <el-form-item label="机构ID（关联dept_id）" prop="institutionId">
        <el-input v-model="formData.institutionId" placeholder="请输入机构ID（关联dept_id）" />
      </el-form-item>
      <el-form-item label="申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更" prop="appType">
        <el-select v-model="formData.appType" placeholder="请选择申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="许可设备名称" prop="licenseDeviceName">
        <el-input v-model="formData.licenseDeviceName" placeholder="请输入许可设备名称" />
      </el-form-item>
      <el-form-item label="阶梯配置机型" prop="ladderConfigModel">
        <el-input v-model="formData.ladderConfigModel" placeholder="请输入阶梯配置机型" />
      </el-form-item>
      <el-form-item label="配置理由" prop="configReason">
        <el-input v-model="formData.configReason" placeholder="请输入配置理由" />
      </el-form-item>
      <el-form-item label="申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成" prop="appStatus">
        <el-radio-group v-model="formData.appStatus">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="初审结果：1-通过，0-不通过" prop="initialReviewResult">
        <el-input v-model="formData.initialReviewResult" placeholder="请输入初审结果：1-通过，0-不通过" />
      </el-form-item>
      <el-form-item label="初审意见" prop="initialReviewOpinion">
        <el-input v-model="formData.initialReviewOpinion" placeholder="请输入初审意见" />
      </el-form-item>
      <el-form-item label="初审人ID" prop="initialReviewerId">
        <el-input v-model="formData.initialReviewerId" placeholder="请输入初审人ID" />
      </el-form-item>
      <el-form-item label="初审时间" prop="initialReviewTime">
        <el-date-picker
          v-model="formData.initialReviewTime"
          type="date"
          value-format="x"
          placeholder="选择初审时间"
        />
      </el-form-item>
      <el-form-item label="专家审核结果：1-通过，0-不通过" prop="expertReviewResult">
        <el-input v-model="formData.expertReviewResult" placeholder="请输入专家审核结果：1-通过，0-不通过" />
      </el-form-item>
      <el-form-item label="专家审核意见" prop="expertReviewOpinion">
        <el-input v-model="formData.expertReviewOpinion" placeholder="请输入专家审核意见" />
      </el-form-item>
      <el-form-item label="专家ID" prop="expertId">
        <el-input v-model="formData.expertId" placeholder="请输入专家ID" />
      </el-form-item>
      <el-form-item label="专家审核时间" prop="expertReviewTime">
        <el-date-picker
          v-model="formData.expertReviewTime"
          type="date"
          value-format="x"
          placeholder="选择专家审核时间"
        />
      </el-form-item>
      <el-form-item label="许可证编号" prop="licenseNo">
        <el-input v-model="formData.licenseNo" placeholder="请输入许可证编号" />
      </el-form-item>
      <el-form-item label="许可证有效期" prop="licenseValidDate">
        <el-date-picker
          v-model="formData.licenseValidDate"
          type="date"
          value-format="x"
          placeholder="选择许可证有效期"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ApplicationApi, Application } from '@/api/biz/application'

/** 申请 表单 */
defineOptions({ name: 'ApplicationForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  appNo: undefined,
  institutionId: undefined,
  appType: undefined,
  licenseDeviceName: undefined,
  ladderConfigModel: undefined,
  configReason: undefined,
  appStatus: undefined,
  initialReviewResult: undefined,
  initialReviewOpinion: undefined,
  initialReviewerId: undefined,
  initialReviewTime: undefined,
  expertReviewResult: undefined,
  expertReviewOpinion: undefined,
  expertId: undefined,
  expertReviewTime: undefined,
  licenseNo: undefined,
  licenseValidDate: undefined,
})
const formRules = reactive({
  appNo: [{ required: true, message: '申请编号不能为空', trigger: 'blur' }],
  institutionId: [{ required: true, message: '机构ID（关联dept_id）不能为空', trigger: 'blur' }],
  appType: [{ required: true, message: '申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更不能为空', trigger: 'change' }],
  appStatus: [{ required: true, message: '申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成不能为空', trigger: 'blur' }],
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
      formData.value = await ApplicationApi.getApplication(id)
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
    const data = formData.value as unknown as Application
    if (formType.value === 'create') {
      await ApplicationApi.createApplication(data)
      message.success(t('common.createSuccess'))
    } else {
      await ApplicationApi.updateApplication(data)
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
    appNo: undefined,
    institutionId: undefined,
    appType: undefined,
    licenseDeviceName: undefined,
    ladderConfigModel: undefined,
    configReason: undefined,
    appStatus: undefined,
    initialReviewResult: undefined,
    initialReviewOpinion: undefined,
    initialReviewerId: undefined,
    initialReviewTime: undefined,
    expertReviewResult: undefined,
    expertReviewOpinion: undefined,
    expertId: undefined,
    expertReviewTime: undefined,
    licenseNo: undefined,
    licenseValidDate: undefined,
  }
  formRef.value?.resetFields()
}
</script>