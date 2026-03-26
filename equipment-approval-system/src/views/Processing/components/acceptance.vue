<template>
  <div class="acceptance-box" v-loading="loading">
    <div class="seach-row">
      <el-form :model="paramsValue" inline label-suffix=":" size="default">
        <el-form-item label="状态">
          <el-select v-model="paramsValue.licenseType" placeholder="请选择 许可证类型" style="width: 130px">
            <el-option v-for="item in licenseTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="paramsValue.licenseDevice" placeholder="请选择 设备类型" style="width: 130px">
            <el-option v-for="item in licenseDeviceOptions" :key="item.value" :label="item.label" :value="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item label="阶梯配置">
          <el-select v-model="paramsValue.ladderConfigModel" placeholder="请选择 阶梯配置" style="width: 130px">
            <el-option v-for="item in ladderConfigOptions" :key="item.value" :label="item.label" :value="item.label" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="paramsValue.keyword" placeholder="搜索许可证编号、配置单位、设备名称" style="width: 230px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="searchFn">搜索</el-button>
          <el-button type="info" :icon="RefreshRight" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table-container">
      <el-table :data="tableData" row-key="id" style="min-height: 570px">
        <el-table-column label="序号" type="index" width="54" align="center" :index="changeIndex" />
        <el-table-column label="许可证编号" prop="licenseNo" align="center" width="200" show-overflow-tooltip />
        <el-table-column label="配置单位" prop="configUnit" align="center" show-overflow-tooltip />
        <el-table-column label="设备名称" prop="deviceName" align="center" show-overflow-tooltip />
        <el-table-column label="阶梯配置机型" prop="ladderConfigModel" align="center" show-overflow-tooltip />
        <el-table-column label="所属区域" prop="areaName" align="center" />
        <el-table-column label="正本发证日期" prop="originalIssueDate" align="center" />
        <el-table-column label="副本发证日期" prop="copyIssueDate" align="center" />
        <el-table-column label="许可证类型" prop="licenseType" align="center" width="130">
          <template #default="scope">
            <el-tag :style="licenseTypeStyle(scope.row)" class="license-type-tag" round>
              {{ licenseTypeOptions.find((item) => item.value === scope.row.licenseType)?.label || '--' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" align="center" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.status === '通过'" class="status-tag status-passed" size="small" round>
              <Icon icon="ep:select" :size="12" />
              <span style="font-size: 12px">通过</span>
            </el-tag>
            <el-tag v-else-if="scope.row.status === '驳回整改'" class="status-tag status-reject" size="small" round>
              <Icon icon="ep:refresh-right" :size="12" />
              <span style="font-size: 12px">驳回整改</span>
            </el-tag>
            <el-tag v-else-if="scope.row.status === '不通过' || scope.row.status === '未通过'" class="status-tag status-failed" size="small" round>
              <Icon icon="ep:close-bold" :size="12" />
              <span style="font-size: 12px">{{ scope.row.status }}</span>
            </el-tag>
            <el-tag v-else-if="scope.row.status === null" type="info" round size="small">待验收</el-tag>
            <el-tag v-else style="font-size: 12px">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="140">
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
            <el-button v-if="row.acceptanceStatus === 1 || row.status" type="primary" size="small" @click="handleDetail(row)">详情</el-button>
            <span v-if="row.acceptanceStatus !== 1 && !row.duplicateId">-</span>
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
  </div>
</template>

<script setup lang="ts" name="AcceptanceList">
import { LicenseApi } from '@/api/biz/license'
import { Search, RefreshRight } from '@element-plus/icons-vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'

interface DictDataTypeT extends DictDataType {
  value: string | number
}
const licenseTypeOptions = computed<DictDataTypeT[]>(() => getDictOptions('license_type'))
const licenseDeviceOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_main_equipment_type'))
const ladderConfigOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_ladder_config_model'))

const licenseTypeStyle = (row) => {
  let rowDict = licenseTypeOptions.value.find((item) => item.value === row.licenseType)
  let cssClass = rowDict?.cssClass || ''
  return { '--color': cssClass.replace('c-', '#') }
}

let loading = ref<boolean>(false)
let paramsValue = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  licenseType: '',
  keyword: '',
  licenseDevice: '',
  ladderConfigModel: ''
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
    acceptanceStatus: '0'
  }
  LicenseApi.getLicensePage(params)
    .then((res) => {
      let { list, total } = res
      tableData.value = list.map((eg) => ({
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
        acceptanceStatus: eg.acceptanceStatus,
        isUpload: eg.isUpload
      }))
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
  Object.assign(paramsValue, {
    pageNum: 1,
    keyword: '',
    licenseType: '',
    licenseDevice: '',
    ladderConfigModel: ''
  })
  getList()
}
const searchFn = () => {
  paramsValue.pageNum = 1
  getList()
}

const router = useRouter()
const handleDetail = (row) => {
  router.push({
    path: '/license/detail',
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
    path: '/license/inspection',
    query: {
      id: row.id,
      originalId: row.originalId,
      duplicateId: row.duplicateId,
      licenseCode: row.licenseNo,
      page: 'copyInspection'
    }
  })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.acceptance-box {
  padding: 10px;

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
        .license-type-tag {
          color: var(--color);
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
</style>