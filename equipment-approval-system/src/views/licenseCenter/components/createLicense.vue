<template>
  <Dialog
    class="custom-dialog"
    v-model="dialogVisible"
    v-bind="dialogBind"
    @closed="closeAddLicense"
  >
    <el-form
      :model="formData"
      :rules="rules"
      ref="formRef"
      label-position="top"
      class="el-form-grid"
      :disabled="loading"
    >
      <!-- 正本信息 -->
      <div class="title-group">正本信息</div>
      <el-form-item label="配置单位名称" prop="originalLicense.institutionId">
        <el-select
          v-model="formData.originalLicense.institutionId"
          placeholder="请选择配置单位名称"
          clearable
          @change="changeConfigUnit"
          :disabled="isEdit"
        >
          <el-option
            v-for="item in configUnitOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="统一社会信用代码" prop="originalLicense.unifiedSocialCreditCode">
        <el-input
          v-model="formData.originalLicense.unifiedSocialCreditCode"
          placeholder="请输入统一社会信用代码"
          show-word-limit
          maxlength="18"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="法定代表人" prop="originalLicense.legalPerson">
        <el-input
          v-model="formData.originalLicense.legalPerson"
          placeholder="请输入法定代表人"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="配置设备名称" prop="originalLicense.licenseDeviceName">
        <el-select
          v-model="formData.originalLicense.licenseDeviceName"
          clearable
          :disabled="isEdit"
        >
          <el-option
            v-for="item in licenseDeviceOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所有制性质" prop="originalLicense.ownershipNature">
        <el-select
          v-model="formData.originalLicense.ownershipNature"
          placeholder="请选择 所有制性质"
          :disabled="isEdit"
          clearable
        >
          <el-option
            v-for="item in ownershipNatureOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="阶梯配置机型" prop="originalLicense.ladderConfigModel">
        <el-select
          v-model="formData.originalLicense.ladderConfigModel"
          :disabled="isEdit"
          clearable
        >
          <el-option
            v-for="item in ladderConfigModelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备配置地址" prop="originalLicense.equipmentConfigAddress">
        <el-input
          v-model="formData.originalLicense.equipmentConfigAddress"
          placeholder="请输入设备配置地址"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="详细地址" prop="originalLicense.detailedAddress">
        <el-input
          v-model="formData.originalLicense.detailedAddress"
          placeholder="请输入 详细地址"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="发证机关" prop="originalLicense.issuingAuthority">
        <el-input
          v-model="formData.originalLicense.issuingAuthority"
          placeholder="请输入 发证机关"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="发证日期" prop="originalLicense.issueDate">
        <el-date-picker
          v-model="formData.originalLicense.issueDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择发证日期"
          :disabled="isEdit"
          clearable
        />
      </el-form-item>
      <el-form-item label="许可证编号" prop="originalLicense.licenseNo">
        <el-input
          v-model="formData.originalLicense.licenseNo"
          placeholder="提交后自动生成"
          disabled
        />
      </el-form-item>
      <!-- 副本信息 -->
      <div class="title-group">副本信息</div>
      <el-form-item label="生产企业" prop="duplicateLicense.productionEnterprise">
        <el-input
          v-model="formData.duplicateLicense.productionEnterprise"
          placeholder="请输入生产企业"
          clearable
        />
      </el-form-item>
      <el-form-item label="具体型号" prop="duplicateLicense.specificModel">
        <el-input
          v-model="formData.duplicateLicense.specificModel"
          placeholder="请输入具体型号"
          clearable
        />
      </el-form-item>
      <el-form-item label="产品序列号" prop="duplicateLicense.productSerialNo">
        <el-input
          v-model="formData.duplicateLicense.productSerialNo"
          placeholder="请输入产品序列号"
          clearable
        />
      </el-form-item>
      <el-form-item label="安装日期" prop="duplicateLicense.installationDate">
        <el-date-picker
          v-model="formData.duplicateLicense.installationDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择安装日期"
          clearable
        />
      </el-form-item>
      <el-form-item label="信息提交日期" prop="duplicateLicense.infoSubmitDate">
        <el-date-picker
          v-model="formData.duplicateLicense.infoSubmitDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择信息提交日期"
          clearable
        />
      </el-form-item>
      <el-form-item label="副本发证机关" prop="duplicateLicense.duplicateIssuingAuthority">
        <el-input
          v-model="formData.duplicateLicense.duplicateIssuingAuthority"
          placeholder="请输入副本发证机关"
          clearable
        />
      </el-form-item>
      <el-form-item label="副本发证日期" prop="duplicateLicense.duplicateIssueDate">
        <el-date-picker
          v-model="formData.duplicateLicense.duplicateIssueDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择副本发证日期"
          clearable
        />
      </el-form-item>
      <el-form-item label="备注" prop="duplicateLicense.remark">
        <el-input v-model="formData.duplicateLicense.remark" placeholder="请输入备注" clearable />
      </el-form-item>
      <!-- 其他信息 -->
      <div class="title-group">其他信息</div>
      <el-form-item label="采购价格" prop="duplicateLicense.purchasePrice">
        <el-input
          v-model="formData.duplicateLicense.purchasePrice"
          placeholder="请输入采购价格"
          type="number"
          :min="0"
          clearable
        >
          <template #append>
            <span>￥/元</span>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item
        label="设备特殊说明"
        prop="duplicateLicense.specialDescription"
        class="form-item-row"
      >
        <el-input
          v-model="formData.duplicateLicense.specialDescription"
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 6 }"
          clearable
        />
      </el-form-item>
      <el-form-item label="设备使用人员" class="form-item-row">
        <el-table
          :data="formData.duplicateLicense.equipmentUsers"
          style="width: 100%"
          border
          stripe
        >
          <el-table-column label="身份证号码" prop="IdCard" align="center" />
          <el-table-column label="姓名" prop="name" align="center" />
          <!-- 性别 -->
          <el-table-column label="性别" prop="gender" align="center">
            <template #default="{ row }">
              {{ row.gender === '1' ? '男' : '女' }}
            </template>
          </el-table-column>
          <!-- 出生日期 -->
          <el-table-column label="出生日期" prop="birthDate" align="center" />
          <!-- 职称 -->
          <el-table-column label="职称" prop="title" align="center" />
          <!-- 联系电话 -->
          <el-table-column label="联系电话" prop="phoneNumber" align="center" />
          <!-- 操作 -->
          <el-table-column label="操作" fixed="right" width="100" align="center">
            <template #default="scope">
              <el-button type="danger" link size="small" @click="handleDeleteUser(scope.$index)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" @click="handleAddUser" style="margin-top: 10px; width: 100%">
          添加用户
        </el-button>
      </el-form-item>
    </el-form>
    <template #footer>
      <div
        class="dialog-footer"
        style="display: flex; align-items: center; justify-content: center"
      >
        <el-button type="info" @click="closeAddLicense" :disabled="loading">取消</el-button>
        <el-button type="primary" @click="submit" :loading="loading">提交</el-button>
      </div>
    </template>

    <Dialog v-model="userDialog" v-bind="userDialogBind">
      <el-form
        :model="userForm"
        :rules="userFormRules"
        label-position="top"
        class="grid-form-2"
        ref="userFormRef"
      >
        <el-form-item label="身份证号码" prop="IdCard">
          <el-input
            v-model="userForm.IdCard"
            placeholder="请输入身份证号码"
            clearable
            @change="handleIdCardBlur"
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="userForm.gender" placeholder="请选择性别" clearable>
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker
            v-model="userForm.birthDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择出生日期"
          />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="userForm.title" placeholder="请输入职称" clearable />
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNumber">
          <el-input v-model="userForm.phoneNumber" placeholder="请输入联系电话" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <div
          class="dialog-footer"
          style="display: flex; align-items: center; justify-content: center"
        >
          <el-button type="primary" @click="closeAddUser">取消</el-button>
          <el-button type="primary" @click="submitUser">提交</el-button>
        </div>
      </template>
    </Dialog>
  </Dialog>
</template>

<script setup lang="ts">
import { LicenseApi } from '@/api/biz/license'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import { ElForm, ElMessage } from 'element-plus'
interface DictDataTypeT extends DictDataType {
  value: string | number
}
let dialogBind = reactive({
  title: '新增许可证',
  width: '980px',
  maxHeight: '600px',
  top: '80px',
  scroll: true,
  fullscreen: true
})
let dialogVisible = defineModel('visible', {
  default: false,
  type: Boolean
})
let editLicenseRow = inject<any>('editLicenseRow')
let isEdit = ref(false)
let loading = ref(false)
let $emtis = defineEmits(['success'])
let licenseDeviceOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_main_equipment_type')
})
let ownershipNatureOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_ownership_nature')
})
let ladderConfigModelOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_ladder_config_model')
})
let configUnitOptions = ref<{ label: string; value: any }[]>([])
let formRef = ref<InstanceType<typeof ElForm> | null>(null)
interface formDataFace {
  originalId?: number | null
  duplicateId?: number | null
  originalLicense: any
  duplicateLicense: any
}
let formData = reactive<formDataFace>({
  originalLicense: {
    configUnitName: '',
    institutionId: '', // 配置单位id
    unifiedSocialCreditCode: '',
    legalPerson: '',
    licenseDeviceName: '',
    ownershipNature: '',
    ladderConfigModel: '',
    equipmentConfigAddress: '',
    detailedAddress: '',
    issuingAuthority: '陕西省卫生健康委员会',
    issueDate: '',
    licenseNo: ''
  },
  duplicateLicense: {
    productionEnterprise: '',
    specificModel: '',
    productSerialNo: '',
    installationDate: '',
    infoSubmitDate: '',
    duplicateIssuingAuthority: '',
    duplicateIssueDate: '',
    remark: '', //备注
    // 其他信息----------
    purchasePrice: '', //采购价格
    specialDescription: '', //特殊描述
    equipmentUsers: [] //设备使用人员JSON
  },
  originalId: null,
  duplicateId: null
})
let rules = reactive({
  'originalLicense.institutionId': [
    { required: true, message: '请选择配置单位名称', trigger: 'change' }
  ],
  'originalLicense.unifiedSocialCreditCode': [
    { required: true, message: '请输入统一社会信用代码', trigger: 'blur' },
    {
      pattern: /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/,
      message: '请输入正确的统一社会信用代码',
      trigger: 'blur'
    }
  ],
  'originalLicense.legalPerson': [{ required: true, message: '请输入法人', trigger: 'blur' }],
  'originalLicense.licenseDeviceName': [{ required: true, message: '请设备名称', trigger: 'blur' }],
  'originalLicense.ownershipNature': [
    { required: true, message: '请输入所有权益性质', trigger: 'blur' }
  ],
  'originalLicense.ladderConfigModel': [
    { required: true, message: '请输入梯级配置模型', trigger: 'blur' }
  ],
  'originalLicense.equipmentConfigAddress': [
    { required: true, message: '请输入设备配置地址', trigger: 'blur' }
  ],
  'originalLicense.detailedAddress': [
    { required: true, message: '请输入详细地址', trigger: 'blur' }
  ],
  'originalLicense.issuingAuthority': [
    { required: true, message: '请输入发证机关', trigger: 'blur' }
  ],
  'originalLicense.issueDate': [{ required: true, message: '请输入发证日期', trigger: 'blur' }]
  // 'duplicateLicense.productionEnterprise': [
  //   { required: true, message: '请输入生产企业', trigger: 'blur' }
  // ],
  // 'duplicateLicense.specificModel': [
  //   { required: true, message: '请输入具体型号', trigger: 'blur' }
  // ],
  // 'duplicateLicense.productSerialNo': [
  //   { required: true, message: '请输入产品序列号', trigger: 'blur' }
  // ],
  // 'duplicateLicense.installationDate': [
  //   { required: true, message: '请输入安装日期', trigger: 'blur' }
  // ],
  // 'duplicateLicense.infoSubmitDate': [
  //   { required: true, message: '请输入信息提交日期', trigger: 'blur' }
  // ],
  // 'duplicateLicense.duplicateIssuingAuthority': [
  //   { required: true, message: '请输入副本发证机关', trigger: 'blur' }
  // ],
  // 'duplicateLicense.duplicateIssueDate': [
  //   { required: true, message: '请输入副本发证日期', trigger: 'blur' }
  // ]
})
const changeConfigUnit = (v) => {
  let eg: { value: any; label: string } = configUnitOptions.value.find(
    (item: { value: any; label: string }) => item.value === v
  ) || { label: '', value: '' }
  // 配置单位名称
  formData.originalLicense.configUnitName = eg.label || ''
}
watch(
  () => dialogVisible.value,
  (v) => {
    if (!v) return
    if (editLicenseRow.value) {
      dialogBind.title = '编辑许可证'
      isEdit.value = true
      getEditFn()
    } else {
      isEdit.value = false
      dialogBind.title = '新增许可证'
    }
  }
)
let userDialog = ref(false)
let userDialogBind = reactive({
  title: '新增设备使用人员',
  width: '500px',
  'append-to-body': true,
  'destroy-on-close': true,
  top: '70px'
})
interface userForm {
  IdCard: string
  name: string
  gender: string
  birthDate: string
  title: string
  phoneNumber: string
}
let userFormRef = ref<InstanceType<typeof ElForm> | null>(null)
let userForm = reactive<userForm>({
  IdCard: '',
  name: '',
  gender: '',
  birthDate: '',
  title: '',
  phoneNumber: ''
})
let userFormRules = {
  IdCard: [
    { required: true, message: '请输入身份证号码', trigger: ['blur'] },
    {
      pattern:
        /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/,
      message: '请输入正确的身份证号码',
      trigger: ['blur']
    }
  ],
  name: [{ required: true, message: '请输入姓名', trigger: ['blur'] }],
  gender: [{ required: true, message: '请选择性别', trigger: ['change'] }],
  birthDate: [{ required: true, message: '请输入出生日期', trigger: ['blur', 'change'] }],
  title: [{ required: true, message: '请输入职称', trigger: ['blur'] }],
  phoneNumber: [
    { required: true, message: '请输入联系电话', trigger: ['blur'] },
    {
      pattern: /^(?:(?:\+|00)86)?1[3-9]\d{9}$/,
      message: '请输入正确的联系电话',
      trigger: ['blur']
    }
  ]
}
const handleIdCardBlur = (v) => {
  userFormRef.value?.validateField('IdCard').then((bool) => {
    // 解析身份证号码
    if (bool) {
      let idCard = userForm.IdCard
      let birthDate = idCard.substring(6, 14)
      userForm.birthDate = birthDate.replace(/(\d{4})(\d{2})(\d{2})/, '$1-$2-$3')
    }
  })
}
const handleDeleteUser = (index) => {
  formData.duplicateLicense.equipmentUsers.splice(index, 1)
}
const handleAddUser = () => {
  userDialog.value = true
}
// 关闭新增设备使用人员弹窗
const closeAddUser = () => {
  userDialog.value = false
  Object.keys(userForm).forEach((key) => {
    userForm[key] = ''
  })
  // 重置表单
  userFormRef.value?.resetFields()
}

// 提交新增设备使用人员表单
const submitUser = () => {
  userFormRef.value?.validate((valid: boolean) => {
    if (valid) {
      // 将新用户信息添加到设备使用人员列表
      formData.duplicateLicense.equipmentUsers.push(JSON.parse(JSON.stringify(userForm)))
      // 关闭弹窗
      closeAddUser()
    } else {
      console.log('表单验证失败')
      // 移除返回 false，避免类型不匹配问题
    }
  })
}
const closeAddLicense = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
}
const submit = async () => {
  try {
    loading.value = true
    await formRef.value?.validate()
    let params = JSON.parse(JSON.stringify(formData))
    // params.duplicateLicense.equipmentUsers = params.duplicateLicense.equipmentUsers.length
    //   ? JSON.stringify(params.duplicateLicense.equipmentUsers)
    //   : []
    params.originalLicense.status = 1 // 状态 1-启用 2-禁用
    // 校验副本信息是否填写
    let duplicateLicenseBool = checkDuplicateLicense()
    if (!duplicateLicenseBool) {
      delete params.duplicateLicense
    }

    params.originalId = params.originalId ? params.originalId : null
    params.duplicateId = params.duplicateId ? params.duplicateId : null
    await LicenseApi.addOfflineLicense(params)

    ElMessage.success('提交成功')
    $emtis('success')
    dialogVisible.value = false
  } catch (err) {
    console.log(err)
    ElMessage.error('提交失败')
  } finally {
    loading.value = false
  }
}
// 获取 编辑信息
const getEditFn = async () => {
  try {
    loading.value = true
    let response = await LicenseApi.getOfflineLicense({
      oid: editLicenseRow.value.originalId,
      did: editLicenseRow.value.duplicateId
    })
    Object.keys(formData).forEach((key1) => {
      if (key1 === 'originalId' || key1 === 'duplicateId') {
        formData[key1] = response[key1]
        return
      }
      Object.keys(formData[key1]).forEach((key2) => {
        if (!response[key1]) {
          return
        }
        if (key2 === 'equipmentUsers') {
          // formData[key1][key2] = isJsonString(response[key1][key2])
          //   ? JSON.parse(response[key1][key2])
          //   : []
          formData[key1][key2] = response[key1][key2] || []
          return
        }
        formData[key1][key2] = response[key1][key2] || ''
      })
    })
  } catch (err) {
    console.log(err)
    ElMessage.error('获取编辑信息失败')
    dialogVisible.value = false
  } finally {
    loading.value = false
  }
}
// 校验是否是 JSON 字符串
const isJsonString = (str) => {
  if (!str || typeof str !== 'string') {
    return false
  }
  if (str === '{}' || str === '[]') {
    return false
  }
  try {
    JSON.parse(str)
  } catch (e) {
    return false
  }
  return true
}
// 检测副本信息是否填写
const checkDuplicateLicense = () => {
  let bool = false
  Object.keys(formData.duplicateLicense).forEach((key) => {
    if (
      typeof formData.duplicateLicense[key] === 'string' &&
      formData.duplicateLicense[key].trim() !== ''
    ) {
      bool = true
      return
    }
    if (Array.isArray(formData.duplicateLicense[key]) && formData.duplicateLicense[key].length) {
      bool = true
      return
    }
  })
  return bool
}

onMounted(async () => {
  try {
    loading.value = true
    // 获取配置单位列表
    let response = await LicenseApi.getConfigUnitList()
    configUnitOptions.value = (response || []).map((item) => ({
      label: item.institutionName,
      value: item.id * 1
    }))
  } catch (err) {
    ElMessage.error('获取配置单位列表失败')
  } finally {
    loading.value = false
  }
})
</script>

<style lang="scss" scoped>
.el-form-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-gap: 20px;
  &:deep(.el-form-item) {
    margin: 0;
    .el-input,
    .el-select,
    .el-date-editor {
      width: 100% !important;
    }
  }
  .form-item-row {
    grid-column: 1/ 5;
  }
  .title-group {
    grid-column: 1/ 5;
    font-size: 14px;
    line-height: 30px;
    font-weight: bold;
    background-color: #e4efff;
    padding: 0 10px;
    border-radius: 10px 10px 0 0;
    border-bottom: 1px solid #e4e7ed;
  }
}
.grid-form-2 {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px 15px;
  &:deep(.el-form-item) {
    margin: 0;
  }
}
</style>
