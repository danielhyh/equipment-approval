<template>
  <div class="dialog-content-page" v-loading="loading">
    <el-form
      class="grid-form-style"
      :model="formData"
      :rules="rules"
      ref="formRef"
      label-position="top"
      :disabled="isView || loading"
    >
      <el-form-item label="配置单位名称" prop="configUnitName">
        <el-input v-model="formData.configUnitName" placeholder="请输入配置单位名称" />
      </el-form-item>
      <!-- 社会统一信用代码 -->
      <el-form-item label="社会统一信用代码" prop="unifiedSocialCreditCode">
        <el-input v-model="formData.unifiedSocialCreditCode" placeholder="请输入社会统一信用代码" />
      </el-form-item>
      <!-- 法人代表 -->
      <el-form-item label="法人代表（或主要负责人）" prop="legalPerson">
        <el-input v-model="formData.legalPerson" placeholder="请输入法人代表" />
      </el-form-item>
      <!-- 所有制性质 -->
      <el-form-item label="所有制性质" prop="ownershipNature">
        <el-input v-model="formData.ownershipNature" placeholder="请输入所有制性质" />
      </el-form-item>
      <!-- 联系人 -->
      <el-form-item label="联系人" prop="contactPerson">
        <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
      </el-form-item>
      <!-- 联系电话 -->
      <el-form-item label="联系人电话" prop="contactPhone">
        <el-input v-model="formData.contactPhone" placeholder="请输入联系电话" />
      </el-form-item>
      <!-- 许可设备名称 -->
      <el-form-item label="许可设备名称" prop="licenseDeviceName">
        <el-input v-model="formData.licenseDeviceName" placeholder="请输入许可设备名称" />
      </el-form-item>
      <!-- 设备配置地址 -->
      <el-form-item label="设备配置地址" prop="equipmentConfigAddress">
        <el-input v-model="formData.equipmentConfigAddress" placeholder="请输入设备配置地址" />
      </el-form-item>
      <!-- 生产企业 -->
      <el-form-item label="生产企业" prop="productionEnterprise">
        <el-input v-model="formData.productionEnterprise" placeholder="请输入生产企业" />
      </el-form-item>
      <!-- 具体型号 -->
      <el-form-item label="具体型号" prop="specificModel">
        <el-input v-model="formData.specificModel" placeholder="请输入具体型号" />
      </el-form-item>
      <!-- 装机日期 -->
      <el-form-item label="装机日期" prop="installationDate">
        <el-date-picker
          v-model="formData.installationDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择装机日期"
        />
      </el-form-item>
      <!-- 采购价格 -->
      <el-form-item label="采购价格(￥:元)" prop="purchasePrice">
        <el-input
          v-model="formData.purchasePrice"
          type="number"
          :min="0"
          placeholder="请输入采购价格"
        />
      </el-form-item>
      <!-- 状态 -->
      <el-form-item label="状态" prop="status">
        <el-switch
          v-model="formData.status"
          :active-value="1"
          :inactive-value="0"
          active-text="正常"
          inactive-text="停用"
        />
      </el-form-item>
      <!-- 特殊说明 -->
      <el-form-item label="特殊说明" prop="specialDescription" class="el-form-item--span-row">
        <el-input
          v-model="formData.specialDescription"
          type="textarea"
          :auto-size="{ minRows: 5 }"
          placeholder="请输入特殊说明"
        />
      </el-form-item>

      <div class="title-row">
        <div class="left"> <Icon icon="mingcute:group-3-fill" /> <span>设备使用人员</span></div>
        <el-button
          type="primary"
          :icon="CirclePlus"
          round
          size="small"
          v-if="!isView"
          @click.stop="addUsePerson"
        >
          添加
        </el-button>
      </div>

      <el-table :data="formData.equipmentUsers" style="width: 100%" class="el-form-item--span-row">
        <el-table-column label="姓名" prop="name" align="center" />
        <el-table-column label="联系电话" prop="phone" show-overflow-tooltip align="center" />
        <el-table-column label="身份证号" prop="idCard" show-overflow-tooltip align="center" />
        <el-table-column label="性别" prop="gender" align="center" />
        <el-table-column label="出生日期" prop="birthDate" show-overflow-tooltip align="center" />
        <el-table-column label="职称" prop="careerTitle" show-overflow-tooltip align="center" />
        <el-table-column label="操作" width="140" align="center" v-if="!isView">
          <template #default="scope">
            <!-- 编辑 -->
            <el-button type="primary" size="small" @click.stop="editUsePerson(scope.row)">
              编辑
            </el-button>
            <!-- 删除 -->
            <el-button type="danger" size="small" @click.stop="deleteUsePerson(scope.$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <Dialog v-model="dialogVisible" v-bind="dialogBind" @closed="resetUsePersonForm">
      <AddUsePerson ref="usePersonRef" />
      <template #footer>
        <div style="display: flex; justify-content: center">
          <el-button type="primary" @click="submitUsePerson"> 提交 </el-button>
          <el-button type="info" @click="resetUsePersonForm"> 取消 </el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts" name="AddMedicalDevice">
import { createEquipment, editEquipment } from '@/api/biz/basisManagement'
import { ElMessageBox } from 'element-plus'
import { CirclePlus } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import AddUsePerson from './addUsePerson.vue'

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
interface TableDataType {
  id?: number
  configUnitName: string
  unifiedSocialCreditCode: string
  legalPerson: string
  // 所有制性质
  ownershipNature: string
  // 联系人
  contactPerson: string
  // 联系电话
  contactPhone: string

  licenseDeviceName: string
  // 设备配置地址
  equipmentConfigAddress: string
  productionEnterprise: string
  specificModel: string
  installationDate: string
  // 采购价格
  purchasePrice: number
  //特殊说明
  specialDescription: string
  equipmentUsers: UsePersonType[]
  status: number
}
let formData = reactive<TableDataType>({
  configUnitName: '',
  unifiedSocialCreditCode: '',
  legalPerson: '',
  ownershipNature: '',
  contactPerson: '',
  contactPhone: '',
  licenseDeviceName: '',
  equipmentConfigAddress: '',
  productionEnterprise: '',
  specificModel: '',
  installationDate: '',
  purchasePrice: 0,
  specialDescription: '',
  status: 1,
  equipmentUsers: [
    // {
    //   name: '张三',
    //   phone: '13800000000',
    //   idCard: '44030019900101001X',
    //   gender: '男',
    //   birthDate: '1990-01-01',
    //   contactPhone: '13800000000',
    //   careerTitle: '医生'
    // }
  ]
})
let rules = ref({
  configUnitName: [{ required: true, message: '请输入配置单位名称', trigger: 'blur' }],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur'
    }
  ],
  installationDate: [{ required: true, message: '请选择装机日期', trigger: 'blur' }]
})
let formRef = ref<FormInstance | null>(null)
let submitFormFn = async () => {
  await formRef.value?.validate()
  try {
    if (isEdit.value) {
      loading.value = true
      await editEquipment({
        id: props.row.id,
        ...formData,
        equipmentUsers: JSON.stringify(formData.equipmentUsers)
      })
      ElMessage.success('编辑成功')
    } else {
      loading.value = true
      await createEquipment({
        ...formData,
        equipmentUsers: JSON.stringify(formData.equipmentUsers)
      })
      ElMessage.success('新增成功')
    }
    return true
  } catch (err) {
    return false
  } finally {
    loading.value = false
  }
}

let dialogVisible = ref(false)
let dialogBind = reactive({
  title: '新增用户',
  width: '450px',
  maxHeight: '530px',
  scroll: true,
  fullscreen: true
})
let usePersonRef = ref<InstanceType<typeof usePersonRef> | null>(null)
const addUsePerson = () => {
  dialogBind.title = '新增设备使用人员'
  dialogVisible.value = true
}
const submitUsePerson = () => {
  usePersonRef.value
    .submit()
    .then(({ value, isEditIndex }) => {
      if (isEditIndex) {
        formData.equipmentUsers.push(value)
      } else {
        formData.equipmentUsers.splice(isEditIndex, 1, value)
      }
      resetUsePersonForm()
    })
    .catch((err) => {
      console.log(err)
    })
}
const resetUsePersonForm = () => {
  usePersonRef.value.resetUserForm()
  dialogVisible.value = false
}
const editUsePerson = (row: UsePersonType) => {
  dialogBind.title = '编辑设备使用人员'
  dialogVisible.value = true
  nextTick(() => {
    usePersonRef.value && usePersonRef.value.editUserForm(row)
  })
}
const deleteUsePerson = ($index) => {
  ElMessageBox.confirm('确定删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    formData.equipmentUsers.splice($index, 1)
  })
}
onMounted(() => {
  if (isView || isEdit) {
    formData = Object.assign(formData, props.row)
  }
})
defineExpose({
  submitFormFn,
  loading
})
</script>

<style scoped lang="scss">
.dialog-content-page {
  width: 100%;
}
.grid-form-style {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  .el-form-item {
    margin: 0;
  }
  .el-form-item--span-row {
    grid-column: 1/5;
  }
  .title-row {
    grid-column: 1/5;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 5px 0px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    margin-bottom: 12px;
    .left {
      display: flex;
      align-items: center;

      &:deep(.el-icon) {
        font-size: 22px !important;
        margin-right: 6px;
        span {
          font-size: 22px !important;
        }
      }
    }
  }
}
</style>
