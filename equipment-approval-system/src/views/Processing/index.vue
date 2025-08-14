<template>
  <div class="processing-page">
    <div class="processing-page-header">
      <div class="left">
        <div class="title"> 办件中心 </div>
        <div class="subtitle"> 大型医用设备审批办件管理 </div>
      </div>
    </div>
    <div class="container-body" v-loading="loading">
      <div class="type-tabs-list">
        <div
          class="type-tabs-item"
          v-for="item in typeList"
          :key="item.type"
          :class="{ active: item.type === activeType }"
          @click="changeTypeFn(item)"
        >
          <span>{{ item.label }}</span>
          <i v-if="item.value">{{ item.value }}</i>
        </div>
      </div>
      <div class="search-list-container">
        <el-form :model="formData" ref="formSearchRef" inline label-suffix=":" size="default">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择状态" @change="getTableListFn">
              <el-option label="全部" value="-1" />
              <el-option
                v-for="dict in dictStatusList"
                :key="String(dict.value)"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设备类型" prop="equipment" v-show="activeType !== 'basicInfoChange'">
            <el-select
              v-model="formData.equipment"
              placeholder="请选择设备类型"
              @change="getTableListFn"
            >
              <el-option label="全部设备" value="-1" />
              <el-option
                v-for="dict in dictEquipmentList"
                :key="String(dict.value)"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item prop="keyWord">
            <el-input
              v-model="formData.keywords"
              placeholder="搜索申请单位或设备名称"
              clearable
              @blur="getTableListFn"
              @keyup.enter="getTableListFn"
            />
          </el-form-item>
        </el-form>
      </div>

      <div class="table-container">
        <el-table
          :data="tableData"
          :columns="tableConfig.columns"
          :pagination="page"
          :row-key="(row) => row.id"
          style="width: 100%; min-height: 500px"
          height="calc(100vh - 420px)"
          :size="'default'"
        >
          <el-table-column
            type="index"
            width="55"
            label="序号"
            :align="'center'"
            :index="changeIndex"
          />
          <el-table-column
            v-for="column in tableConfig.columns"
            :key="column.dataIndex"
            :label="column.title"
            :prop="column.dataIndex"
            :show-overflow-tooltip="false"
            :align="'center'"
          >
            <template #default="scope">
              <span
                v-if="column.dataIndex === 'status'"
                class="status-label"
                :class="statusMap(scope.row[column.dataIndex]).colorType"
              >
                {{ statusMap(scope.row[column.dataIndex]).label }}
              </span>
              <span v-else>{{ scope.row[column.dataIndex] }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="120" :align="'center'">
            <template #default="scope">
              <el-button size="small" class="btn view-btn" type="primary" :icon="View">
                查看
              </el-button>
              <el-button size="small" class="btn audit-btn" type="primary" :icon="Search">
                初步审核
              </el-button>
              <el-button size="small" class="btn expert-btn" type="primary" :icon="Avatar">
                专家审批
              </el-button>
              <el-button
                size="small"
                class="btn"
                type="primary"
                @click.stop="showLicenceFn(scope.row, 'A')"
              >
                正本
              </el-button>
              <el-button
                size="small"
                class="btn"
                type="primary"
                @click.stop="showLicenceFn(scope.row, 'B')"
              >
                副本
              </el-button>
              <!-- <el-dropdown placement="bottom" trigger="click">
                <el-button size="small" class="btn" type="primary"> 正本 </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="view">
                      <el-icon><View /></el-icon>
                      <span>查看</span>
                    </el-dropdown-item>
                    <el-dropdown-item command="download">
                      <el-icon><Download /></el-icon>
                      <span>下载</span>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-dropdown placement="bottom" trigger="click">
                <el-button size="small" class="btn" type="primary"> 副本 </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="view">
                      <el-icon><View /></el-icon>
                      <span>查看</span>
                    </el-dropdown-item>
                    <el-dropdown-item command="download">
                      <el-icon><Download /></el-icon>
                      <span>下载</span>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown> -->
            </template>
          </el-table-column>
        </el-table>

        <pagination
          class="pagination-style"
          :page="page.pageNo"
          :limit="page.pageSize"
          :total="page.total"
          @pagination="changePagination"
        />
      </div>
    </div>
    <Dialog v-model="liscenceVisible" v-bind="licenceDialogBind">
      <div class="dialog-licence-content">
        <div class="row">
          <el-button size="small" type="success" :icon="Printer" @click.stop="printFn">
            打印
          </el-button>
          <el-button size="small" type="warning" :icon="Download" @click.stop="downloadFn">
            下载
          </el-button>
        </div>
        <licence ref="licenceRef" v-bind="licenceData" />
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts" name="ProcessingCenter">
import { Dialog } from '@/components/Dialog/index'
import pagination from '@/components/Pagination/index.vue'
import licence from './components/licence.vue'
import { ref, computed, onMounted } from 'vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import type { FormInstance } from 'element-plus'
import { View, Search, Avatar, Download, Printer } from '@element-plus/icons-vue'
type TypeList = {
  label: string
  value: number
  type: string
  status?: string
  equipment?: string
}
let typeList = ref<TypeList[]>([
  {
    label: '证书申请',
    value: 0,
    type: 'apply',
    status: 'biz_app_status',
    equipment: 'biz_main_equipment_type'
  },
  {
    label: '证书补办',
    value: 233,
    type: 'reissue',
    status: 'biz_review_result',
    equipment: 'biz_main_equipment_type'
  },
  {
    label: '证书变更',
    value: 1,
    type: 'change',
    status: 'biz_review_result',
    equipment: 'biz_main_equipment_type'
  },
  { label: '基本信息变更', value: 3, type: 'basicInfoChange', status: 'biz_review_result' }
])
let activeType = ref<string>('apply')
let activeStatus = ref<string>('biz_app_status')
let activeEquipment = ref<string>('biz_main_equipment_type')

const changeTypeFn = (item: TypeList) => {
  activeType.value = item.type
  activeStatus.value = item.status || ''
  activeEquipment.value = item.equipment || ''
  // 切换类型时，重置表单
  resetForm()
  resetAllFn()
}
let tableConfig = computed(() => {
  switch (activeType.value) {
    case 'apply':
      return {
        columns: [
          { title: '申请单位', dataIndex: 'applyCompany', minWidth: '120' },
          { title: '设备名称', dataIndex: 'equipmentName', minWidth: '120' },
          { title: '阶梯配置机型', dataIndex: 'applyType', minWidth: '120' },
          { title: '申请日期', dataIndex: 'applyTime', minWidth: '120' },
          { title: '状态', dataIndex: 'status', minWidth: '120' },
          { title: '剩余时间', dataIndex: 'remainTime', minWidth: '120' }
        ]
      }
    case 'change':
      return {
        columns: [
          { title: '申请单位', dataIndex: 'applyCompany' },
          { title: '设备名称', dataIndex: 'equipmentName' },
          { title: '申请日期', dataIndex: 'applyTime' },
          { title: '状态', dataIndex: 'status' }
        ]
      }
    case 'reissue':
      return {
        columns: [
          { title: '申请单位', dataIndex: 'applyCompany' },
          { title: '设备名称', dataIndex: 'equipmentName' },
          { title: '阶梯配置机型', dataIndex: 'applyType' },
          { title: '申请日期', dataIndex: 'applyTime' },
          { title: '状态', dataIndex: 'status' },
          { title: '剩余时间', dataIndex: 'remainTime' }
        ]
      }
    case 'basicInfoChange':
      return {
        columns: [
          { title: '申请单位', dataIndex: 'applyCompany' },
          { title: '变更内容', dataIndex: 'changeContent' },
          { title: '申请日期', dataIndex: 'applyTime' },
          { title: '状态', dataIndex: 'status' }
        ]
      }
    default:
      return {
        columns: []
      }
  }
})
let dictStatusList = computed<DictDataType[]>(() => getDictOptions(activeStatus.value))
let dictEquipmentList = computed<DictDataType[]>(() => getDictOptions(activeEquipment.value))

type PageParam = {
  pageSize: number
  pageNo: number
  total: number
}
let page = ref<PageParam>({
  pageSize: 10,
  pageNo: 1,
  total: 100
})
const changeIndex = (index: number) => {
  return page.value.pageSize * (page.value.pageNo - 1) + index + 1
}
const changePagination = (pagination: { page: number; limit: number }) => {
  page.value.pageNo = pagination.page
  page.value.pageSize = pagination.limit
  getTableListFn()
}
const resetAllFn = () => {
  page.value.pageNo = 1
  page.value.total = 0
  getTableListFn()
}

type FormDataEnum = {
  status: string
  equipment?: string
  keywords: string
}
let formData = ref<FormDataEnum>({
  status: '-1',
  equipment: '-1',
  keywords: ''
})

let formSearchRef = ref<FormInstance | null>(null)
const resetForm = () => {
  formSearchRef.value?.resetFields()
  formData.value = {
    status: '-1',
    equipment: '-1',
    keywords: ''
  }
}

let tableData = ref<any[]>([])
let loading = ref<boolean>(false)

const statusMap = (status: number) => {
  return (
    dictStatusList.value.find((item) => item.value === String(status)) || {
      label: '状态异常',
      colorType: 'danger'
    }
  )
}
const getTableListFn = () => {
  loading.value = true
  let params = {
    ...formData.value,
    pageSize: page.value.pageSize,
    pageNo: page.value.pageNo
  }
  if (params.status === '-1') {
    delete (params as { [key: string]: any }).status
  }
  if (params.equipment === '-1') {
    delete (params as { [key: string]: any }).equipment
  }
  // 模拟获取数据
  setTimeout(() => {
    // 假设获取到的数据
    tableData.value = [
      {
        id: '0-1',
        applyCompany: '公司A',
        equipmentName: '设备A',
        applyType: '类型A',
        applyTime: '2023-01-01',
        status: 0,
        remainTime: '2天'
      },
      {
        id: 1,
        applyCompany: '公司A',
        equipmentName: '设备A',
        applyType: '类型A',
        applyTime: '2023-01-01',
        status: 1,
        remainTime: '2天'
      },
      {
        id: 2,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 2,
        remainTime: '1天'
      },
      {
        id: 3,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 3,
        remainTime: '1天'
      },
      {
        id: 4,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 4,
        remainTime: '1天'
      },
      {
        id: 5,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 5,
        remainTime: '1天'
      },
      {
        id: 6,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 6,
        remainTime: '1天'
      },
      {
        id: 7,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 7,
        remainTime: '1天'
      },
      {
        id: 8,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 8,
        remainTime: '1天'
      },
      {
        id: 9,
        applyCompany: '公司B',
        equipmentName: '设备B',
        applyType: '类型B',
        applyTime: '2023-02-01',
        status: 9,
        remainTime: '1天'
      }
    ]
    page.value.total = tableData.value.length
    loading.value = false
  }, 1000)
}

// 正本|副本 弹窗展示
let liscenceVisible = ref<boolean>(false)
let licenceTitle = ref('正本')
let licenceDialogBind = computed(() => {
  return {
    title: '许可证-' + licenceTitle.value,
    width: '320mm',
    maxHeight: '600px',
    scroll: true,
    fullscreen: true
  }
})
let licenceData = ref({})

let licenceRef = ref<InstanceType<typeof licence> | null>(null)
const showLicenceFn = (row, type) => {
  console.log('row', row)
  licenceTitle.value = type === 'A' ? '正本' : '副本'
  liscenceVisible.value = true
}
const printFn = () => {
  if (!licenceRef.value) {
    return
  }
  licenceRef.value?.print()
}
const downloadFn = () => {
  if (!licenceRef.value) {
    return
  }
  licenceRef.value?.download()
}
onMounted(() => {
  // 在组件挂载时可以进行一些初始化操作
  console.log('Processing Center mounted')
  getTableListFn()
})
</script>

<style lang="scss" scoped>
@keyframes fadeInMove {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-2px);
  }
  100% {
    transform: translateY(0px);
  }
}
.processing-page {
  border-radius: 20px 20px 0 0;
  background-color: #fff;
}
.processing-page-header {
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  // background-image: linear-gradient(to bottom, #409eff, #a8d9ff);
  border-radius: 20px 20px 0 0;
  background-color: #fff;
  border-bottom: 2px solid #e5e5e5;
  .left {
    .title {
      font-size: 20px;
      color: #333;
      font-weight: bold;
    }
    .subtitle {
      font-size: 14px;
      color: #333;
      margin-top: 5px;
    }
  }
}
.container-body {
  .type-tabs-list {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px;
    .type-tabs-item {
      position: relative;
      padding: 10px 14px;
      border-radius: 8px;
      cursor: pointer;
      user-select: none;
      span {
        font-size: 14px;
        color: #666;
        line-height: 20px;
        font-weight: 500;
        transition: all 0.3s ease;
      }
      i {
        font-size: 12px;
        color: #fff;
        line-height: 18px;
        font-weight: 600;
        font-style: normal;
        padding: 0 6px;
        position: absolute;
        display: block;
        top: 0;
        right: 0;
        transform: translate(40%, -10%);
        background-color: rgb(211, 7, 7);
        border-radius: 10px;
        z-index: 222;
        transition: all 0.3s ease;
      }
      &:hover {
        background-color: #eeeeee;
      }
      &.active {
        background-image: linear-gradient(to right, #4885f5, #74b3fa);
        span {
          color: #fff;
        }
        i {
          background-color: rgb(12, 203, 34);
        }
      }
    }
  }
  .search-list-container {
    padding: 5px 20px;
    .el-form {
      .el-form-item {
        margin-bottom: 0;
        margin-right: 10px;
        .el-select {
          width: 180px;
        }
        .el-input {
          width: 200px;
        }
      }
    }
  }
  .table-container {
    width: 100%;
    padding: 10px;
    .el-table {
      .cell {
      }
      .status-label {
        --color: rgb(37, 84, 237);
        --color-rab: 37, 84, 237;
        --color-opacity: 0.1;
        display: inline-block;
        padding: 2px 14px;
        border-radius: 12px;
        font-size: 12px;
        line-height: 18px;
        color: var(--color);
        border: 1px solid var(--color);
        background-color: rgba(var(--color-rab), var(--color-opacity));
        user-select: none;
        &.info {
          --color: rgb(249, 130, 11);
          --color-rab: 249, 130, 11;
        }
        &.primary {
          --color: #409eff;
          --color-rab: 64, 158, 255;
        }
        &.success {
          --color: #67c23a;
          --color-rab: 103, 194, 58;
        }
        &.danger {
          --color: #f56c6c;
          --color-rab: 245, 108, 108;
        }
        &.warning {
          --color: #e6a23c;
          --color-rab: 230, 162, 60;
        }
        &.primary {
          --color: #409eff;
          --color-rab: 64, 158, 255;
        }
      }
      .btn {
        border-radius: 10px;
        color: #fff;
        font-size: 12px;
        background-image: linear-gradient(135deg, #06b6d4, #0891b2);
        border: none;
        margin-top: 4px;
        &:hover {
          animation: fadeInMove 0.3s ease;
        }
        &.view-btn {
          background-image: linear-gradient(135deg, #165dff, #3b82f6);
        }
        &.audit-btn {
          background-image: linear-gradient(135deg, #22c55e, #16a34a);
        }
        &.expert-btn {
          background-image: linear-gradient(135deg, #f59e0b, #d97706);
        }
      }
      .el-dropdown {
        margin-left: 12px;
      }
    }
    .pagination-style {
      position: relative;
      float: none;
      justify-content: flex-end;
    }
  }
}

.dialog-licence-content {
  .row {
    padding: 0 20px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
}
</style>
