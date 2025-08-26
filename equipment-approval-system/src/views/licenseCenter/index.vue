<template>
  <div class="license-center-box" v-loading="loading">
    <div class="statistics-row">
      <div class="left">
        <Icon icon="ph:farm-fill" :size="20" />
        <span class="title">许可证统计</span>
      </div>
      <div class="right">
        <div class="statistics-item" v-for="item in statisticsList" :key="item.key">
          <Icon class="statistics-icon" :icon="item.icon" />
          <span class="statistics-label">{{ item.label }}:</span>
          <span class="statistics-value">{{ item.value }}</span>
        </div>
      </div>
    </div>
    <div class="seach-row">
      <el-form v-model="paramsValue" inline label-suffix=":">
        <!-- 许可证类型 licenseType -->
        <el-form-item label="许可证类型">
          <el-select
            v-model="paramsValue.licenseType"
            placeholder="请选择 许可证类型"
            style="width: 130px"
          >
            <el-option
              v-for="item in licenseTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 设备类型 licenseDevice -->
        <el-form-item label="设备类型">
          <el-select
            v-model="paramsValue.licenseDevice"
            placeholder="请选择 设备类型"
            style="width: 130px"
          >
            <el-option
              v-for="item in licenseDeviceOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 阶梯配置 ladderConfig -->
        <el-form-item label="阶梯配置">
          <el-select
            v-model="paramsValue.ladderConfig"
            placeholder="请选择 阶梯配置"
            style="width: 130px"
          >
            <el-option
              v-for="item in ladderConfigOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 所属区域 area -->
        <el-form-item label="所属区域">
          <el-select v-model="paramsValue.area" placeholder="请选择 所属区域" style="width: 130px">
            <el-option
              v-for="item in areaOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 许可证名称 keyword -->
        <el-form-item>
          <el-input
            v-model="paramsValue.keyword"
            placeholder="搜索许可证编号、配置单位、设备名称"
            style="width: 230px"
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
        <!-- 序号 -->
        <el-table-column label="序号" type="index" width="54" align="center" :index="changeIndex" />
        <el-table-column
          label="许可证编号"
          prop="licenseNo"
          align="center"
          width="200"
          show-overflow-tooltip
        />
        <el-table-column label="配置单位" prop="configUnit" align="center" show-overflow-tooltip />
        <el-table-column label="设备名称" prop="deviceName" align="center" show-overflow-tooltip />
        <!-- 阶梯配置机型 -->
        <el-table-column
          label="阶梯配置机型"
          prop="ladderConfigModel"
          align="center"
          show-overflow-tooltip
        />
        <!-- 所属区域 -->
        <el-table-column label="所属区域" prop="areaName" align="center" />
        <!-- 正本发证日期	-->
        <el-table-column label="正本发证日期" prop="originalIssueDate" align="center" />
        <!-- 副本发证日期  -->
        <el-table-column label="副本发证日期" prop="copyIssueDate" align="center" />
        <!-- 许可证类型	 -->
        <el-table-column label="许可证类型" prop="licenseType" align="center">
          <template #default="scope">
            <el-tag :style="licenseTypeStyle(scope.row)" class="license-type-tag" round>
              {{
                licenseTypeOptions.find((item) => item.value === scope.row.licenseType)?.label ||
                '--'
              }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- 状态 -->
        <el-table-column label="状态" prop="status" align="center" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'" round>
              <Icon :icon="scope.row.status === '1' ? 'ep:select' : 'ep:close-bold'" />
              <span>
                {{ statusOptions.find((item) => item.value === scope.row.status)?.label || '--' }}
              </span>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="240">
          <template #default="scope">
            <el-button type="primary" size="small" @click.stop="openLicense(scope.row, 'A')">
              正本
            </el-button>
            <el-button type="primary" size="small" @click.stop="openLicense(scope.row, 'B')">
              副本
            </el-button>
            <el-button type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 弹窗 - 许可证 -->
    <Dialog v-model="dialogVisible" v-bind="dialogBind">
      <div class="dialog-licence-content">
        <div class="row" v-if="isLicense">
          <el-button size="small" type="success" :icon="Printer" @click.stop="printFn">
            打印
          </el-button>
          <el-button size="small" type="warning" :icon="Download" @click.stop="downloadFn">
            下载
          </el-button>
        </div>
        <component :is="dialogComponent" v-bind="dialogComponentProps" ref="dialogComponentRef" />
      </div>
    </Dialog>
  </div>
</template>

<script setup lang="ts" name="LicenseCenter">
import License from '../Processing/components/license.vue'
import { Search, RefreshRight, Printer, Download } from '@element-plus/icons-vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'

// 许可证类型
const licenseTypeOptions = computed<DictDataType[]>(() => getDictOptions('license_type'))
// 设备类型
const licenseDeviceOptions = computed<DictDataType[]>(() =>
  getDictOptions('biz_main_equipment_type')
)
// 阶梯配置
const ladderConfigOptions = computed<DictDataType[]>(() =>
  getDictOptions('biz_ladder_config_model')
)
// 所属区域
const areaOptions = computed<DictDataType[]>(() => getDictOptions('biz_area_list'))
// 状态
const statusOptions = reactive([
  { label: '正常', value: '1' },
  { label: '已注销', value: '2' }
])
// 统计数据
interface StatisticsType {
  label: string
  value: string
  key: string
  icon: string
}
// 申请证书 | 补办证书 | 变更证书 | 总计
let statisticsList = reactive<StatisticsType[]>([
  { label: '申请证书', value: '239', key: 's-q-z-s', icon: 'ep:circle-plus-filled' },
  { label: '补办证书', value: '134', key: 'b-c-z-s', icon: 'ep:refresh-right' },
  { label: '变更证书', value: '24', key: 'g-c-z-s', icon: 'ep:edit' },
  { label: '总计', value: '407', key: 'z-z-s', icon: 'mage:star-fill' }
])
// 处理 许可证类型 列表样式
const licenseTypeStyle = (row) => {
  let rowDict = licenseTypeOptions.value.find((item) => item.value === row.licenseType)
  let cssClass = rowDict?.cssClass || ''
  return { '--color': cssClass.replace('c-', '#') }
}
let loading = ref<boolean>(false)
interface ParamsType {
  pageNum: number
  pageSize: number
  total: number
  keyword: string
  licenseType: string
  licenseDevice: string
  ladderConfig: string
  area: string
}
let paramsValue = reactive<ParamsType>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  licenseType: '',
  keyword: '',
  licenseDevice: '',
  ladderConfig: '',
  area: ''
})
const changeIndex = (index: number) => {
  return paramsValue.pageSize * (paramsValue.pageNum - 1) + index + 1
}
let tableData = ref<any[]>([])
const getList = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    tableData.value = [
      {
        id: 1,
        licenseNo: 'XK-YLQX-2023-0001',
        configUnit: '北京协和医院',
        deviceName: '64排螺旋CT',
        ladderConfigModel: 'Revolution CT',
        areaName: '北京市',
        originalIssueDate: '2023-01-15',
        copyIssueDate: '2023-01-16',
        licenseType: '1',
        status: '1'
      }
    ]
  }, 1000)
}
const resetSearch = () => {
  paramsValue = Object.assign(paramsValue, {
    pageNum: 1,
    keyword: '',
    licenseType: '',
    licenseDevice: '',
    ladderConfig: '',
    area: ''
  })
  getList()
}
const searchFn = () => {
  paramsValue.pageNum = 1
  getList()
}

let router = useRouter()
const handleDetail = (row) => {
  router.push({
    path: '/license-detail',
    query: {
      id: row.id
    }
  })
}

// 弹窗
let dialogVisible = ref(false)
let dialogBind = reactive({
  title: '许可证-正本',
  width: '320mm',
  maxHeight: '600px',
  scroll: true,
  fullscreen: true
})
let dialogComponent = ref(markRaw(License))
let dialogComponentProps = ref({})
let dialogComponentRef = ref<InstanceType<typeof License> | null>(null)
let isLicense = ref(true)
// 打开许可证弹窗
const openLicense = (row, type) => {
  console.log(row, type)

  dialogComponent.value = markRaw(License)
  dialogBind.width = '320mm'
  if (type === 'A') {
    dialogBind.title = '许可证-正本'
    dialogComponentProps.value = {
      licenceType: 'A',
      licenceSubtitle: 'A'
    }
  } else {
    dialogBind.title = '许可证-副本'
    dialogComponentProps.value = {
      licenceType: 'A',
      licenceSubtitle: 'B'
    }
  }
  dialogVisible.value = true
}
const printFn = () => {
  if (!dialogComponentRef.value) {
    return
  }
  dialogComponentRef.value?.print()
}
const downloadFn = () => {
  if (!dialogComponentRef.value) {
    return
  }
  dialogComponentRef.value?.download()
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.license-center-box {
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
      border: 1px solid rgba(8, 144, 178, 0.4);
      background-color: rgba(8, 144, 178, 0.2);
      font-size: 14px;
      color: #0891b2;
      &:deep(.statistics-icon) {
        font-size: 14px !important;
        vertical-align: baseline;
        margin-right: 4px;
      }
      span {
        font-weight: bold;
      }
      span + span {
        margin-left: 5px;
      }
      &:nth-of-type(2) {
        color: #d97706;
        background-color: rgba(217, 119, 6, 0.1);
        border-color: rgba(255, 193, 7, 0.6);
      }
      &:nth-of-type(3) {
        color: #16a34a;
        background-color: rgba(22, 163, 74, 0.1);
        border-color: rgba(22, 163, 74, 0.6);
      }
      &:nth-of-type(4) {
        color: rgb(66, 75, 248);
        background-color: rgba(66, 75, 248, 0.1);
        border-color: rgba(66, 75, 248, 0.6);
      }
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
.dialog-licence-content {
  .row {
    padding: 0 20px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }
}
</style>
