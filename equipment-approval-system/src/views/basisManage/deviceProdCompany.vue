<template>
  <div class="device-prod-company-page" v-loading="loading">
    <div class="statistics-row">
      <div class="left">
        <Icon icon="ph:farm-fill" :size="20" />
        <span class="title">设备生产企业管理</span>
      </div>
      <div class="right">
        <el-button type="primary" :icon="CirclePlusFilled" @click.stop="addFn">
          新增生产企业
        </el-button>
      </div>
    </div>
    <div class="seach-row">
      <el-form v-model="paramsValue" inline label-suffix=":">
        <!-- 企业状态 -->
        <el-form-item label="企业状态">
          <el-select
            v-model="paramsValue.status"
            placeholder="请选择 企业状态"
            style="width: 180px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <!-- 设备类型 -->
        <el-form-item label="设备类型">
          <el-select
            v-model="paramsValue.mainEquipmentType"
            placeholder="请选择 设备类型"
            style="width: 180px"
          >
            <el-option
              v-for="item in licenseDeviceOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 设备生产企业名称 -->
        <el-form-item>
          <el-input
            v-model="paramsValue.keyword"
            placeholder="请输入 设备生产企业名称"
            style="width: 240px"
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
        <!-- 企业名称 -->
        <el-table-column label="企业名称" prop="companyName" align="center" />
        <!-- 简称 -->
        <!-- <el-table-column label="简称" prop="abbreviation" align="center" /> -->
        <!-- 主要设备类型 -->
        <el-table-column
          label="主要设备类型"
          prop="mainEquipmentType"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            {{ licenseDeiviceLabel(scope.row.mainEquipmentType) }}
          </template>
        </el-table-column>
        <!-- 注册日期 -->
        <el-table-column label="注册日期" prop="createTime" align="center" />
        <!-- 状态 -->
        <el-table-column label="状态" prop="status" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ statusType[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 备注 -->
        <el-table-column label="备注" prop="remarks" align="center" show-overflow-tooltip />
        <!-- 操作 -->
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="editFn(scope.row)">编辑</el-button>
            <el-button type="primary" size="small" @click="viewFn(scope.row)">查看</el-button>
            <el-button type="danger" size="small" @click="delFn(scope.row)">删除</el-button>
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
      <AddDevProCom ref="dialogComponentRef" v-bind="dialogComponentProps" />
      <template #footer v-if="dialogComponentProps.type !== 'view'">
        <el-button type="primary" @click.stop="submitFormFn"> 提交 </el-button>
        <el-button type="info" @click="dialogVisible = false"> 取消 </el-button>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { formatDate } from '@/utils/formatTime'
import { getproductionCompanyList, deleteproductionCompany } from '@/api/biz/basisManagement'
import { CirclePlusFilled, Search, RefreshRight } from '@element-plus/icons-vue'
import AddDevProCom from './components/addDevProCom.vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import { ElMessageBox } from 'element-plus'

// 设备类型
const licenseDeviceOptions = computed<DictDataType[]>(() =>
  getDictOptions('biz_main_equipment_type')
)
const licenseDeiviceLabel = (v) => {
  return licenseDeviceOptions.value.find((item) => item.value === v)?.label || '--'
}
let statusType = {
  1: '启用',
  0: '禁用'
}

let loading = ref(false)
interface ParamsType {
  pageNum: number
  pageSize: number
  total: number
  keyword: string
  // 状态
  status: string
  // 设备类型
  mainEquipmentType: string
}
let paramsValue = reactive<ParamsType>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  keyword: '',
  status: '',
  mainEquipmentType: ''
})
const changeIndex = (index: number) => {
  return paramsValue.pageSize * (paramsValue.pageNum - 1) + index + 1
}
interface tableDataType {
  id?: number | string
  companyName: string
  abbreviation?: string
  mainEquipmentType: string
  createTime: string
  status: string
  remark: string
}
let tableData = ref<tableDataType[]>([])
const getList = () => {
  loading.value = true
  let params = {
    pageNo: paramsValue.pageNum,
    pageSize: paramsValue.pageSize,
    companyName: paramsValue.keyword,
    status: paramsValue.status,
    mainEquipmentType: paramsValue.mainEquipmentType
  }
  getproductionCompanyList(params)
    .then((res) => {
      tableData.value = (res.list || []).map((item) => {
        return {
          ...item,
          createTime: formatDate(item.createTime, 'YYYY-MM-DD HH:mm:ss')
        }
      })
      paramsValue.total = res.total
    })
    .catch((err) => {
      tableData.value = []
    })
    .finally(() => {
      loading.value = false
    })
}
const resetSearch = () => {
  paramsValue = Object.assign(paramsValue, {
    pageNum: 1,
    keyword: '',
    status: '',
    mainEquipmentType: ''
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
  maxHeight: '500px',
  scroll: true,
  fullscreen: true,
  center: true
})
let dialogComponentProps = reactive({
  row: {},
  type: 'view'
})
let dialogComponentRef = ref<InstanceType<typeof AddDevProCom> | null>(null)

const addFn = () => {
  dialogBind.title = '新增设备生产企业'
  dialogComponentProps.row = {}
  dialogComponentProps.type = 'add'
  dialogVisible.value = true
}
const editFn = (row) => {
  dialogBind.title = '设备生产企业-编辑'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'edit'
  dialogVisible.value = true
}
const viewFn = (row) => {
  dialogBind.title = '设备生产企业-查看'
  dialogComponentProps.row = row
  dialogComponentProps.type = 'view'
  dialogVisible.value = true
}
const delFn = (row) => {
  ElMessageBox.confirm('确定删除 ' + row.companyName + ' 吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      // 发送删除请求
      return deleteproductionCompany(row.id)
    })
    .then(() => {
      // 删除成功后刷新列表
      ElMessage.success('删除成功')
      getList()
    })
    .catch((err) => {
      ElMessage.error(err.msg || '删除失败')
    })
}

const submitFormFn = async () => {
  let bool = await dialogComponentRef.value?.submitFormFn()
  if (bool) {
    dialogVisible.value = false
    getList()
  }
}
onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.device-prod-company-page {
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
