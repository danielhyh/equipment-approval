<template>
  <div class="dialog-content-page">
    <el-form
      class="grid-form-style"
      :model="formData"
      :rules="rules"
      ref="formRef"
      label-position="top"
      :disabled="isView"
    >
      <el-form-item label="配置单位名称" prop="unitName">
        <el-input v-model="formData.unitName" placeholder="请输入配置单位名称" />
      </el-form-item>
      <!-- 社会统一信用代码 -->
      <el-form-item label="社会统一信用代码" prop="creditCode">
        <el-input v-model="formData.creditCode" placeholder="请输入社会统一信用代码" />
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
      <el-form-item label="许可设备名称" prop="deviceName">
        <el-input v-model="formData.deviceName" placeholder="请输入许可设备名称" />
      </el-form-item>
      <!-- 设备配置地址 -->
      <el-form-item label="设备配置地址" prop="deviceConfigAddress">
        <el-input v-model="formData.deviceConfigAddress" placeholder="请输入设备配置地址" />
      </el-form-item>
      <!-- 生产企业 -->
      <el-form-item label="生产企业" prop="productionCompany">
        <el-input v-model="formData.productionCompany" placeholder="请输入生产企业" />
      </el-form-item>
      <!-- 具体型号 -->
      <el-form-item label="具体型号" prop="model">
        <el-input v-model="formData.model" placeholder="请输入具体型号" />
      </el-form-item>
      <!-- 装机日期 -->
      <el-form-item label="装机日期" prop="installDate">
        <el-date-picker
          v-model="formData.installDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择装机日期"
        />
      </el-form-item>
      <!-- 采购价格 -->
      <el-form-item label="采购价格" prop="purchasePrice">
        <el-input v-model="formData.purchasePrice" placeholder="请输入采购价格" />
      </el-form-item>
      <!-- 特殊说明 -->
      <el-form-item label="特殊说明" prop="remark" class="el-form-item--span-row">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :auto-size="{ minRows: 5 }"
          placeholder="请输入特殊说明"
        />
      </el-form-item>

      <div class="title-row">
        <div class="left"> <Icon icon="mingcute:group-3-fill" /> <span>设备使用人员</span></div>
        <el-button type="primary" :icon="CirclePlus" round size="small" v-if="!isView">
          添加
        </el-button>
      </div>

      <el-table :data="formData.usePersonList" style="width: 100%" class="el-form-item--span-row">
        <el-table-column label="姓名" prop="name" align="center" />
        <el-table-column label="联系电话" prop="phone" show-overflow-tooltip align="center" />
        <el-table-column label="身份证号" prop="idCard" show-overflow-tooltip align="center" />
        <el-table-column label="性别" prop="gender" align="center" />
        <el-table-column label="出生日期" prop="birthDate" show-overflow-tooltip align="center" />
        <el-table-column label="职称" prop="careerTitle" show-overflow-tooltip align="center" />
        <el-table-column label="操作" width="140" align="center" v-if="!isView">
          <template #default>
            <!-- 编辑 -->
            <el-button type="primary" size="small"> 编辑</el-button>
            <!-- 删除 -->
            <el-button type="danger" size="small"> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script setup lang="ts" name="AddMedicalDevice">
import { CirclePlus } from '@element-plus/icons-vue'
let props = defineProps({
  row: {
    type: Object,
    default: () => ({})
  },
  type: { type: String, default: 'view' }
})
let isView = computed(() => props.type === 'view')
let isEdit = computed(() => props.type === 'edit')
// 使用人员
interface UsePersonType {
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
  unitName: string
  creditCode: string
  legalPerson: string
  // 所有制性质
  ownershipNature: string
  // 联系人
  contactPerson: string
  // 联系电话
  contactPhone: string

  deviceName: string
  // 设备配置地址
  deviceConfigAddress: string
  productionCompany: string
  model: string
  installDate: string
  // 采购价格
  purchasePrice: number
  //特殊说明
  remark: string
  usePersonList: UsePersonType[]
}

let formData = reactive<TableDataType>({
  unitName: '',
  creditCode: '',
  legalPerson: '',
  ownershipNature: '',
  contactPerson: '',
  contactPhone: '',
  deviceName: '',
  deviceConfigAddress: '',
  productionCompany: '',
  model: '',
  installDate: '',
  purchasePrice: 0,
  remark: '',
  usePersonList: [
    {
      name: '张三',
      phone: '13800000000',
      idCard: '44030019900101001X',
      gender: '男',
      birthDate: '1990-01-01',
      contactPhone: '13800000000',
      careerTitle: '医生'
    }
  ]
})

let rules = ref({
  unitName: [{ required: true, message: '请输入配置单位名称', trigger: 'blur' }]
})

onMounted(() => {
  if (isView || isEdit) {
    formData = Object.assign(formData, props.row)
  }
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
