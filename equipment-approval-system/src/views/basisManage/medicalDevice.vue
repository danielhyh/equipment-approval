<template>
  <div class="medical-device-page" v-loading="loading">
    <div class="statistics-row">
      <div class="left">
        <Icon icon="ph:farm-fill" :size="20" />
        <span class="title">甲类大型医用设备录入</span>
      </div>
      <div class="right">
        <el-button type="primary" :icon="CirclePlusFilled" @click.stop="addFn"
          >医用设备录入</el-button
        >
      </div>
    </div>
    <div class="seach-row">
      <el-form v-model="paramsValue" inline label-suffix=":">
        <el-form-item>
          <el-input
            v-model="paramsValue.keyword"
            placeholder="请输入 配置单位名称/信用代码等进行查询"
            style="width: 300px"
          />
        </el-form-item>
        <!-- 搜索按钮 重置按钮 -->
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchFn">搜索</el-button>
          <el-button type="info" :icon="RefreshRight" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="tableData" row-key="id" style="min-height: 570px">
        <el-table-column label="序号" type="index" width="54" align="center" :index="changeIndex" />
        <!-- 配置单位名称 -->
        <el-table-column label="配置单位名称" prop="unitName" align="center" />
        <!-- 社会统一信用代码 -->
        <el-table-column label="社会统一信用代码" prop="creditCode" align="center" />
        <!-- 法人代表 -->
        <el-table-column label="法人代表" prop="legalPerson" align="center" />
        <!-- 许可设备名称 -->
        <el-table-column label="许可设备名称" prop="deviceName" align="center" />
        <!-- 生产企业 -->
        <el-table-column label="生产企业" prop="productionCompany" align="center" />
        <!-- 具体型号 -->
        <el-table-column label="具体型号" prop="model" align="center" />
        <!-- 装机日期 -->
        <el-table-column label="装机日期" prop="installDate" align="center" />
        <!-- 操作 -->
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editFn(scope.row)">编辑</el-button>
            <el-button type="primary" size="small" @click="viewFn(scope.row)">查看</el-button>
            <el-button type="danger" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <Pagination
        class="pagination-container"
        :total="paramsValue.total"
        v-model:page="paramsValue.pageNum"
        v-model:limit="paramsValue.pageSize"
        @pagination="getList"
      />
    </div>

    <!-- 弹窗 -->
    <Dialog v-model="dialogVisible" v-bind="dialogBind">
      <AddMedicalDevice v-bind="dialogComponentProps" />
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { CirclePlusFilled, Search, RefreshRight } from '@element-plus/icons-vue'
import AddMedicalDevice from './components/addMedicalDevice.vue'
let loading = ref(false)
interface ParamsType {
  pageNum: number
  pageSize: number
  total: number
  keyword: string
}
let paramsValue = reactive<ParamsType>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  keyword: ''
})
const changeIndex = (index: number) => {
  return paramsValue.pageSize * (paramsValue.pageNum - 1) + index + 1
}
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

let tableData = ref<any[]>([])
const getList = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    tableData.value = [
      {
        id: 1,
        unitName: '单位1',
        creditCode: '123456789012345678',
        legalPerson: '张三',
        ownershipNature: '国有企业',
        contactPerson: '李四',
        contactPhone: '13800000000',
        deviceName: '设备1',
        deviceConfigAddress: '地址1',
        productionCompany: '企业1',
        model: '型号1',
        installDate: '2023-01-01',
        purchasePrice: 10000,
        remark: '备注1'
      }
    ]
  }, 1000)
}
const resetSearch = () => {
  paramsValue = Object.assign(paramsValue, {
    pageNum: 1,
    keyword: ''
  })
  getList()
}
const searchFn = () => {
  paramsValue.pageNum = 1
  getList()
}

// 弹窗
let dialogVisible = ref(false)
let dialogBind = reactive({
  title: '查看-',
  width: '940px',
  maxHeight: '600px',
  scroll: true,
  fullscreen: true
})
let dialogComponentProps = reactive({
  row: {},
  type: 'view'
})

const addFn = () => {
  dialogBind.title = '医用设备录入'
  dialogComponentProps.row = {}
  dialogComponentProps.type = 'add'
  dialogVisible.value = true
}
const editFn = (row) => {
  dialogBind.title = '医用设备-编辑'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'edit'
  dialogVisible.value = true
}
const viewFn = (row) => {
  dialogBind.title = '医用设备-查看'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'view'
  dialogVisible.value = true
}
onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.medical-device-page {
  border-radius: 10px;
  background-color: #fff;
  padding: 10px;
  .statistics-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e5e5e5;
    .left {
      display: flex;
      align-items: center;
      font-size: 20px;
      font-weight: bold;
      color: #1081f2;
      .title {
        margin-left: 4px;
      }
    }
    .right {
      display: flex;
      align-items: center;
    }
    .statistics-item {
      display: flex;
      align-items: center;
      padding: 5px 14px;
      border-radius: 6px;
      box-sizing: border-box;
    }
    .statistics-item + .statistics-item {
      margin-left: 10px;
    }
  }
  .seach-row {
    .el-form {
      .el-form-item {
        margin-bottom: 10px;
        margin-right: 10px;
      }
    }
  }
  .table-container {
    &:deep(.el-table) {
      .el-table__cell {
        .el-tag__content {
          display: flex;
          align-items: center;
          .el-icon {
            margin-right: 4px;
          }
        }
        .license-type-tag {
          color: var(--color);
          background-color: rgba(var(--color), 0.3);
        }
      }
    }
    .pagination-container {
      float: none;
      justify-content: flex-end;
    }
    &::after {
      content: '';
      clear: both;
    }
  }
}
</style>
