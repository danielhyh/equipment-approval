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
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <!-- 阶梯配置 ladderConfig -->
        <el-form-item label="阶梯配置">
          <el-select
            v-model="paramsValue.ladderConfigModel"
            placeholder="请选择 阶梯配置"
            style="width: 130px"
          >
            <el-option
              v-for="item in ladderConfigOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <!-- 所属区域 area -->
        <el-form-item label="所属区域">
          <el-select v-model="paramsValue.area" placeholder="请选择 所属区域" style="width: 130px">
            <el-option
              v-for="item in areaOptions"
              :key="item.label"
              :label="item.label"
              :value="item.label"
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
      <el-button type="primary" :icon="VideoPlay" @click.stop="addLicense">新增许可证</el-button>
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
        <el-table-column label="许可证类型" prop="licenseType" align="center" width="130">
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
        <el-table-column label="状态" prop="status" align="center" width="120">
          <template #default="scope">
            <el-tag
              v-if="scope.row.status === '通过'"
              class="status-tag status-passed"
              size="small"
              round
            >
              <Icon icon="ep:select" :size="12" />
              <span style="font-size: 12px">通过</span>
            </el-tag>
            <el-tag
              v-else-if="scope.row.status === '驳回整改'"
              class="status-tag status-reject"
              size="small"
              round
            >
              <Icon icon="ep:refresh-right" :size="12" />
              <span style="font-size: 12px">驳回整改</span>
            </el-tag>
            <el-tag
              v-else-if="scope.row.status === '不通过' || scope.row.status === '未通过'"
              class="status-tag status-failed"
              size="small"
              round
            >
              <Icon icon="ep:close-bold" :size="12" />
              <span style="font-size: 12px">{{ scope.row.status }}</span>
            </el-tag>
            <el-tag v-else-if="scope.row.status === null" type="info" round size="small">
              待验收
            </el-tag>
            <el-tag v-else style="font-size: 12px"> {{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <!-- 验收 -->
        <el-table-column label="验收" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.acceptanceStatus !== 1 && !!row.duplicateId && row.status === null"
              type="warning"
              size="small"
              @click="row.isUpload ? copyInspectionFn(row) : null"
              :disabled="!row.isUpload"
            >
              {{ row.isUpload ? '设备验收' : '设备未验收' }}
            </el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <!-- 许可证 -->
        <el-table-column label="许可证" align="center" width="140">
          <template #default="scope">
            <el-dropdown
              style="margin-right: 8px; vertical-align: middle"
              trigger="click"
              @command="(command) => openLicense(scope.row, 'A', command)"
            >
              <el-button type="primary" size="small"> 正本 </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">查看</el-dropdown-item>
                  <el-dropdown-item command="print">打印预览</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-dropdown
              style="margin-right: 8px; vertical-align: middle"
              trigger="click"
              @command="(command) => openLicense(scope.row, 'B', command)"
            >
              <el-button type="primary" size="small"> 副本 </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">查看</el-dropdown-item>
                  <el-dropdown-item command="print">打印预览</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
            <el-button
              v-show="
                scope.row.licenseType === '5' && scope.row.status !== '通过' && !scope.row.isUpload
              "
              type="primary"
              size="small"
              @click.stop="editLicense(scope.row)"
            >
              副本填报
            </el-button>
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
    <!-- 弹窗 新增许可证 -->
    <CreateLicense v-model:visible="addLicenseVisible" @success="getList" />
  </div>
</template>

<script setup lang="ts" name="LicenseCenter">
import { VideoPlay } from '@element-plus/icons-vue'
import { LicenseApi } from '@/api/biz/license'
import License from '../Processing/components/license.vue'
import CreateLicense from './components/createLicense.vue'
import { Search, RefreshRight, Printer, Download } from '@element-plus/icons-vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import type { licenseProfileType, copyProfile } from './components/licenseProfile'
import { dayTimeFormate } from './components/licenseProfile'
interface DictDataTypeT extends DictDataType {
  value: string | number
}
// 许可证类型
const licenseTypeOptions = computed<DictDataTypeT[]>(() => getDictOptions('license_type'))
// 设备类型
const licenseDeviceOptions = computed<DictDataTypeT[]>(() =>
  getDictOptions('biz_main_equipment_type')
)
// 阶梯配置
const ladderConfigOptions = computed<DictDataTypeT[]>(() =>
  getDictOptions('biz_ladder_config_model')
)
// 所属区域
const areaOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_area_list'))
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
  { label: '申请证书', value: '0', key: 'cert_apply_count', icon: 'ep:circle-plus-filled' },
  { label: '线下办理', value: '0', key: 'offline_count', icon: 'ep:circle-plus-filled' },
  { label: '补办证书', value: '0', key: 'cert_renew_count', icon: 'ep:refresh-right' },
  { label: '变更证书', value: '0', key: 'cert_change_count', icon: 'ep:edit' },
  { label: '总计', value: '0', key: 'total_count', icon: 'mage:star-fill' }
])
const getStatisticsFn = async () => {
  LicenseApi.getLicenseStatistics()
    .then((res) => {
      statisticsList.forEach((item) => {
        item.value = res[item.key] || '0'
      })
    })
    .catch(() => {
      statisticsList.forEach((item) => {
        item.value = '0'
      })
    })
}
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
  ladderConfigModel: string
  area: string
}
let paramsValue = reactive<ParamsType>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  licenseType: '',
  keyword: '',
  licenseDevice: '',
  ladderConfigModel: '',
  area: ''
})
const changeIndex = (index: number) => {
  return paramsValue.pageSize * (paramsValue.pageNum - 1) + index + 1
}
let tableData = ref<any[]>([])
const getList = () => {
  loading.value = true
  let params = {
    pageNum: paramsValue.pageNum,
    pageSize: paramsValue.pageSize,
    licenseType: paramsValue.licenseType,
    keywords: paramsValue.keyword,
    deviceType: paramsValue.licenseDevice,
    ladderConfigModel: paramsValue.ladderConfigModel,
    region: paramsValue.area
  }
  LicenseApi.getLicensePage(params)
    .then((res) => {
      let { list, total } = res
      tableData.value = list.map((eg) => {
        return {
          id: eg.appId,
          licenseNo: eg.licenseNo,
          configUnit: eg.configUnitName,
          deviceName: eg.licenseDeviceName,
          ladderConfigModel: eg.ladderConfigModel,
          areaName: eg.region,
          originalIssueDate: eg.originalIssuanceDate,
          copyIssueDate: eg.duplicateIssueDate,
          licenseType: eg.licenseType,
          status: eg.status,
          originalId: eg.originalId,
          duplicateId: eg.duplicateId,
          acceptanceStatus: eg.acceptanceStatus, // 设备验收状态 1已验收 0未验收
          isUpload: eg.isUpload // 是否上传验收资料 1已上传 0未上传
        }
      })
      paramsValue.total = total
    })
    .catch(() => {
      tableData.value = []
      paramsValue.total = 0
    })
    .finally(() => {
      loading.value = false
    })
}
const resetSearch = () => {
  paramsValue = Object.assign(paramsValue, {
    pageNum: 1,
    keyword: '',
    licenseType: '',
    licenseDevice: '',
    ladderConfigModel: '',
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
      id: row.id,
      originalId: row.originalId,
      duplicateId: row.duplicateId,
      licenseCode: row.licenseNo,
      licenseType: row.licenseType
    }
  })
}
const copyInspectionFn = (row) => {
  router.push({
    path: '/copy-inspection',
    query: {
      id: row.id,
      originalId: row.originalId,
      duplicateId: row.duplicateId,
      licenseCode: row.licenseNo,
      page: 'copyInspection'
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
let dialogComponentProps = ref<licenseProfileType>({
  licenceType: '',
  licenceSubtitle: '',
  code: '',
  licenseData: [],
  stampUit: '',
  stampDate: '',
  seal: '',
  originalId: '',
  duplicateId: '',
  preview: false,
  copyAccepted: false
})
let dialogComponentRef = ref<InstanceType<typeof License> | null>(null)
let isLicense = ref(true)
// 打开许可证弹窗
const openLicense = async (row, type, command) => {
  loading.value = true
  let originalParam = { id: row.originalId }
  let copyParam = { id: row.duplicateId }

  if (type === 'A') {
    dialogBind.title = '许可证-正本'
    dialogComponentProps.value = { licenceType: 'B', licenceSubtitle: 'A' }
  } else {
    dialogBind.title = '许可证-副本'
    dialogComponentProps.value = { licenceType: 'B', licenceSubtitle: 'B' }
  }
  if (command === 'print') {
    dialogComponentProps.value.preview = true
  } else {
    dialogComponentProps.value.preview = false
  }
  dialogComponentProps.value.originalId = String(row.originalId)
  dialogComponentProps.value.duplicateId = String(row.duplicateId)
  dialogComponentProps.value.code = row.licenseNo
  // 设备验收状态 1已验收 0未验收
  dialogComponentProps.value.copyAccepted = row.acceptanceStatus === 1
  if (row.acceptanceStatus !== 1) {
    dialogComponentProps.value.duplicateId = ''
  }

  dialogComponent.value = markRaw(License)
  dialogBind.width = '320mm'
  try {
    let response =
      type === 'A'
        ? await Promise.all([LicenseApi.getLicenseOriginal(originalParam)])
        : !!dialogComponentProps.value.duplicateId
          ? await Promise.all([
              LicenseApi.getLicenseOriginal(originalParam),
              LicenseApi.getLicenseCopy(copyParam)
            ])
          : await Promise.all([LicenseApi.getLicenseOriginal(originalParam)])
    let result: any = null
    if (type === 'B') {
      if (row.duplicateId) {
        result = formateDialogLicense({ ...response[0], ...response[1] }, type)
      } else {
        result = formateDialogLicense({ ...response[0] }, type)
      }
    } else {
      result = formateDialogLicense({ ...response[0] }, type)
    }
    dialogComponentProps.value.licenseData = result
    dialogVisible.value = true
  } catch (err) {
    loading.value = false
    ElMessage.error(`获取 ${dialogBind.title} 失败`)
  }
  loading.value = false
}
const formateDialogLicense = (data: copyProfile, type: string) => {
  let arr: (string | null | undefined)[] = []
  if (type === 'A') {
    arr.push(data.configUnitName)
    arr.push(data.unifiedSocialCreditCode)
    arr.push(data.legalPerson)
    arr.push(data.licenseDeviceName)
    arr.push(data.ownershipNature)
    arr.push(data.ladderConfigModel)
    arr.push(data.equipmentConfigAddress)
    dialogComponentProps.value.stampUit = data.issuingAuthority
    dialogComponentProps.value.stampDate = data.issueDate
    return arr
  }
  arr.push(data.configUnitName)
  arr.push(data.productionEnterprise)
  arr.push(data.legalPerson)
  arr.push(data.specificModel)
  arr.push(data.ownershipNature)
  arr.push(data.productSerialNo)
  arr.push(data.equipmentConfigAddress)
  arr.push(dayTimeFormate(data.installationDate))
  arr.push(data.unifiedSocialCreditCode)
  arr.push(dayTimeFormate(data.infoSubmitDate))
  arr.push(data.licenseDeviceName)
  arr.push(data.remark)
  arr.push(data.ladderConfigModel)
  dialogComponentProps.value.stampUit = data.duplicateIssuingAuthority
  dialogComponentProps.value.stampDate = data.duplicateIssueDate
  return arr
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

let editLicenseRow = ref<any>(null)
provide('editLicenseRow', editLicenseRow)
// 新增许可证弹窗
const addLicenseVisible = ref(false)
// 新增许可证
const addLicense = () => {
  editLicenseRow.value = null
  addLicenseVisible.value = true
}
// 编辑许可证
const editLicense = (row) => {
  editLicenseRow.value = row
  addLicenseVisible.value = true
}

onMounted(() => {
  getList()
  getStatisticsFn()
})
</script>

<style lang="scss" scoped>
.license-center-box {
  padding: 10px;
  background-color: #fff;
  border-radius: 10px;

  .statistics-row {
    display: flex;
    padding-bottom: 10px;
    margin-bottom: 20px;
    border-bottom: 1px solid #e5e5e5;
    align-items: center;
    justify-content: space-between;

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
      padding: 5px 14px;
      font-size: 14px;
      color: #0891b2;
      background-color: rgb(8 144 178 / 20%);
      border: 1px solid rgb(8 144 178 / 40%);
      border-radius: 6px;
      box-sizing: border-box;
      align-items: center;

      &:deep(.statistics-icon) {
        margin-right: 4px;
        font-size: 14px !important;
        vertical-align: baseline;
      }

      span {
        font-weight: bold;
      }

      span + span {
        margin-left: 5px;
      }

      &:nth-of-type(2) {
        color: #d97706;
        background-color: rgb(217 119 6 / 10%);
        border-color: rgb(255 193 7 / 60%);
      }

      &:nth-of-type(3) {
        color: #16a34a;
        background-color: rgb(22 163 74 / 10%);
        border-color: rgb(22 163 74 / 60%);
      }

      &:nth-of-type(4) {
        color: rgb(66 75 248);
        background-color: rgb(66 75 248 / 10%);
        border-color: rgb(66 75 248 / 60%);
      }
    }

    .statistics-item + .statistics-item {
      margin-left: 10px;
    }
  }

  .seach-row {
    display: flex;
    justify-content: space-between;

    .el-form {
      .el-form-item {
        margin-right: 10px;
        margin-bottom: 10px;
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
            // margin-right: 4px;
          }
        }

        .license-type-tag {
          color: var(--color);
          background-color: rgba(var(--color), 0.3);
        }

        .status-tag {
          padding: 4px 12px;
          font-size: 12px;
          font-weight: 500;
          border: none;

          .el-tag__content {
            display: flex;
            align-items: center;
          }

          &.status-passed {
            color: #059669;
            background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
            border: 1px solid #6ee7b7;
          }

          &.status-reject {
            color: #d97706;
            background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
            border: 1px solid #fcd34d;
          }

          &.status-failed {
            color: #dc2626;
            background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
            border: 1px solid #fca5a5;
          }

          &.status-empty {
            min-width: 50px;
            padding: 4px 16px;
            font-size: 16px;
            font-weight: 600;
            color: #64748b;
            background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
            border: 1px solid #cbd5e1;
          }
        }
      }
    }

    .pagination-container {
      float: none;
      justify-content: flex-end;
    }

    &::after {
      clear: both;
      content: '';
    }
  }
}

.dialog-licence-content {
  .row {
    display: flex;
    padding: 0 20px;
    align-items: center;
    justify-content: flex-end;
  }
}
</style>
