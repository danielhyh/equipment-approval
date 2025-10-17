<template>
  <div>
    <!-- 设备配置分布情况 -->
    <div>
      <div class="title-row" style="margin-bottom: 10px">
        <div class="left">
          <div class="title">
            <Icon
              icon="mdi:message-text-clock"
              color="#4245df"
              :size="18"
              style="margin-right: 5px"
            />
            设备配置分布情况
          </div>
        </div>
        <div class="right">
          <el-button type="primary" size="small" :icon="Filter" @click.stop="openDialog">
            筛选
          </el-button>
          <el-button type="success" size="small" v-if="false">
            <template #icon>
              <Icon
                icon="material-symbols:download-rounded"
                color="#fff"
                :size="18"
                style="margin-right: 5px"
              />
            </template>
            导出数据
          </el-button>
        </div>
      </div>
      <!-- 表格 -->
      <el-table
        :data="configScatterData"
        style="width: 100%"
        border
        stripe
        size="small"
        :style="{ 'border-radius': ' 10px' }"
        :header-cell-style="{ background: '#007bff', color: '#fff' }"
        :cell-class-name="cellClassNameFn"
        @cell-click="handleCellClick"
        class="custom-table-style"
      >
        <!-- 行政区域 -->
        <el-table-column prop="region" label="行政区域" align="center" fixed="left" />
        <!-- 合计 -->
        <el-table-column prop="total" label="合计" align="center" fixed="left" />
        <!-- 内窥镜手术器械控制系统 -->
        <el-table-column label="内窥镜手术器械控制系统" align="center">
          <el-table-column prop="endoGov" label="政府办医" align="center" />
          <el-table-column prop="endoSociety" label="社会办医" align="center" />
          <el-table-column prop="endoAccepted" label="已验收" align="center" />
          <el-table-column prop="endoNotaccepted" label="未验收" align="center" />
        </el-table-column>
        <!-- X线正电子发射断层扫描仪 -->
        <el-table-column label="X线正电子发射断层扫描仪" align="center">
          <el-table-column prop="petGov" label="政府办医" align="center" />
          <el-table-column prop="petSociety" label="社会办医" align="center" />
          <el-table-column prop="petAccepted" label="已验收" align="center" />
          <el-table-column prop="petNotaccepted" label="未验收" align="center" />
        </el-table-column>
        <!-- 64排及以上X线计算机断层扫描仪 -->
        <el-table-column label="64排及以上X线计算机断层扫描仪" align="center">
          <el-table-column prop="ct64Gov" label="政府办医" align="center" />
          <el-table-column prop="ct64Society" label="社会办医" align="center" />
          <el-table-column prop="ct64Accepted" label="已验收" align="center" />
          <el-table-column prop="ct64Notaccepted" label="未验收" align="center" />
        </el-table-column>
        <!-- 1.5T及以上磁共振成像系统 -->
        <el-table-column label="1.5T及以上磁共振成像系统" align="center">
          <el-table-column prop="mriGov" label="政府办医" align="center" />
          <el-table-column prop="mriSociety" label="社会办医" align="center" />
          <el-table-column prop="mriAccepted" label="已验收" align="center" />
          <el-table-column prop="mriNotaccepted" label="未验收" align="center" />
        </el-table-column>
        <!-- 直线加速器 -->
        <el-table-column label="直线加速器" align="center">
          <el-table-column prop="linacGov" label="政府办医" align="center" />
          <el-table-column prop="linacSociety" label="社会办医" align="center" />
          <el-table-column prop="linacAccepted" label="已验收" align="center" />
          <el-table-column prop="linacNotaccepted" label="未验收" align="center" />
        </el-table-column>
        <!-- 伽玛射线立体定向放射治疗系统 -->
        <el-table-column label="伽玛射线立体定向放射治疗系统" align="center">
          <el-table-column prop="gammaGov" label="政府办医" align="center" />
          <el-table-column prop="gammaSociety" label="社会办医" align="center" />
          <el-table-column prop="gammaAccepted" label="已验收" align="center" />
          <el-table-column prop="gammaNotaccepted" label="未验收" align="center" />
        </el-table-column>
      </el-table>
    </div>
    <!-- 设备详情 -->
    <div style="margin-top: 20px">
      <div class="title-row" style="margin-bottom: 10px">
        <div class="left">
          <div class="title">
            <Icon
              icon="mdi:message-text-clock"
              color="#4245df"
              :size="18"
              style="margin-right: 5px"
            />
            设备详细信息
          </div>
        </div>
      </div>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
        stripe
        size="small"
        :style="{ 'border-radius': ' 10px' }"
        :header-cell-style="{ background: '#007bff', color: '#fff' }"
        v-loading="loading"
      >
        <!-- 序号 -->
        <el-table-column type="index" label="序号" width="60" :index="customIndex" align="center" />
        <!-- 所属机构 -->
        <el-table-column prop="institutionName" label="所属机构" align="center" />
        <!-- 设备类型 -->
        <el-table-column prop="licenseDeviceName" label="设备类型" align="center" />
        <!-- 阶梯配置 -->
        <el-table-column prop="ladderConfigModel" label="阶梯配置" align="center" />
        <!-- 许可证编号 -->
        <el-table-column prop="licenseNo" label="许可证编号" align="center" />
        <!-- 发证日期 -->
        <el-table-column prop="issueDate" label="发证日期" align="center" />
        <!-- 装机日期 -->
        <el-table-column prop="installationDate" label="装机日期" align="center" />
        <!-- 生产企业 -->
        <el-table-column prop="productionEnterprise" label="生产企业" align="center" />
        <!-- 具体型号 -->
        <el-table-column prop="specificModel" label="具体型号" align="center" />
        <!-- 副本发证日期 -->
        <el-table-column prop="duplicateIssueDate" label="副本发证日期" align="center" />
        <!-- 状态 -->
        <el-table-column prop="status" label="状态" align="center">
          <template #default="{ row }">
            {{ row.status === 1 ? '有效' : row.status === 2 ? '已注销' : '已变更' }}
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        class="pagination-container"
        :total="queryParams.total"
        v-model:page="queryParams.pageNo"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
    <!-- 筛选弹窗 -->
    <Dialog v-model:model-value="filterVisible" v-bind="filterDialogProp">
      <el-form
        v-model="queryParams"
        label-position="top"
        label-suffix="："
        class="custom-form-grid"
      >
        <!-- 搜索许可证编号、医疗机构 -->
        <el-form-item label="搜索许可证编号、医疗机构" prop="keyword" class="form-row">
          <el-input
            v-model="queryParams.keyword"
            placeholder="请输入许可证编号或医疗机构"
            style="width: 100%"
            clearable
          />
        </el-form-item>
        <el-form-item label="统计年份" prop="year">
          <el-date-picker
            v-model="queryParams.year"
            type="year"
            format="YYYY年"
            value-format="YYYY"
            placeholder="请选择年份"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="行政区域" prop="region">
          <el-select
            v-model="queryParams.region"
            placeholder="请选择区域"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in regionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <!-- 阶梯配置 -->
        <el-form-item label="阶梯配置" prop="step_config">
          <el-select
            v-model="queryParams.step_config"
            placeholder="请选择阶梯配置"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in stepConfigOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
        <!-- 证书状态 -->
        <el-form-item label="证书状态" prop="license_status">
          <el-select
            v-model="queryParams.licenseStatus"
            placeholder="请选择证书状态"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="item in licenseStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- 验收状态 -->
        <el-form-item label="验收状态" prop="acceptanceStatus">
          <el-select
            v-model="queryParams.acceptanceStatus"
            placeholder="请选择验收状态"
            style="width: 100%"
            clearable
          >
            <el-option label="通过" :value="1" />
            <el-option label="未通过" :value="0" />
          </el-select>
        </el-form-item>
        <!-- 机构性质 -->
        <el-form-item label="机构性质" prop="institutionType">
          <el-select
            v-model="queryParams.institutionType"
            placeholder="请选择机构性质"
            style="width: 100%"
            clearable
          >
            <el-option label="社会办医" :value="1" />
            <el-option label="政府办医" :value="2" />
          </el-select>
        </el-form-item>
        <!-- 设备类型 -->
        <el-form-item label="设备类型" prop="device_type" class="form-row">
          <el-checkbox-group v-model="queryParams.device_type" style="width: 100%">
            <el-checkbox v-for="item in deviceTypeOptions" :key="item.value" :value="item.label">
              {{ item.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="display: flex; justify-content: center; align-items: center">
          <el-button type="info" @click="handleReset">重置</el-button>
          <el-button type="info" @click="filterVisible = false">取消</el-button>
          <el-button type="primary" @click="handleFilter">筛选</el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { AnalysisApi } from '@/api/biz/analysis'
import { Filter } from '@element-plus/icons-vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
interface DictDataTypeT extends DictDataType {
  value: string | number
}
// 行政区域
let regionOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_area_list')
})
// 阶梯配置
let stepConfigOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_ladder_config_model')
})
// 设备类型
let deviceTypeOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_main_equipment_type')
})
// 许可证状态
let licenseStatusOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_license_status')
})

// 设备信息
let loading = ref(false)
let tableData = ref([])
interface queryParamsType {
  pageNo: number
  pageSize: number
  total: number
  // 搜索关键词
  keyword: string
  // 统计年份
  year: string
  // 行政区划
  region: string
  // 阶梯配置
  step_config: string
  // 证书状态
  licenseStatus: string
  // 设备类型
  device_type: string[]
  // 验收状态
  acceptanceStatus: string | number
  // 机构性质
  institutionType: string | number
}
let queryParams = reactive<queryParamsType>({
  pageNo: 1,
  pageSize: 10,
  total: 0,
  // 搜索关键词
  keyword: '',
  // 统计年份
  year: '',
  // 行政区划
  region: '',
  // 阶梯配置
  step_config: '',
  // 证书状态
  licenseStatus: '',
  // 设备类型
  device_type: [],
  // 验收状态
  acceptanceStatus: '',
  // 机构性质
  institutionType: ''
})
const customIndex = (index: number) => {
  return (queryParams.pageNo - 1) * queryParams.pageSize + index + 1
}
const getList = async () => {
  let params = {
    pageNo: queryParams.pageNo,
    pageSize: queryParams.pageSize,
    // 搜索关键词
    keywords: queryParams.keyword,
    // 统计年份
    year: queryParams.year,
    // 行政区划
    region: queryParams.region,
    // 阶梯配置
    ladderConfigModel: queryParams.step_config,
    // 证书状态
    status: queryParams.licenseStatus,
    // 设备类型
    deviceTypes: queryParams.device_type.join(','),
    // 验收状态
    acceptanceStatus: queryParams.acceptanceStatus,
    // 机构性质
    institutionType: queryParams.institutionType
  }
  try {
    loading.value = true
    const response = await AnalysisApi.getDeviceDetail(params)
    tableData.value = response.list || []
    queryParams.total = response.total || 0
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}
// 设备配置分布情况列表
let configScatterData = ref([])
const getConfigScatterFn = async () => {
  try {
    loading.value = true
    const response = await AnalysisApi.getConfigDistribution()
    configScatterData.value = response || []
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}
let cellActive = reactive<{ rowIndex: number | null; columnIndex: number | null }>({
  rowIndex: null,
  columnIndex: null
})
const cellClassNameFn = ({ columnIndex, rowIndex, column, row }) => {
  if (row.rowIndex === undefined || column.columnIndex === undefined) {
    row.rowIndex = rowIndex
    column.columnIndex = columnIndex
  }

  if (columnIndex === 0) return ''
  if (row.rowIndex === cellActive.rowIndex && column.columnIndex === cellActive.columnIndex) {
    return 'cell-active'
  }
  return ''
}
const handleCellClick = (row, column, cell, event) => {
  if (row.rowIndex === cellActive.rowIndex && column.columnIndex === cellActive.columnIndex) {
    cellActive.rowIndex = null
    cellActive.columnIndex = null
    handleReset()
    return
  }
  cellActive.rowIndex = row.rowIndex
  cellActive.columnIndex = column.columnIndex
  if (column.columnIndex === 0) return
  handleCellClickFilter(column, row)
}
// 处理单元格点击 应作出的筛选内容
const handleCellClickFilter = (column, row) => {
  onlyReset()
  console.log(row)
  let { property } = column
  // 'endoNotaccepted' 里面 取出 大写字母前的字符串
  let prefixProperty = property.match(/^([^A-Z]+)/)?.[1] || property
  let suffixProperty = property.match(/[A-Z].*/)?.[0] || property

  switch (prefixProperty) {
    case 'endo': // 内窥镜手术器械控制系统
      queryParams.device_type.push('内窥镜手术器械控制系统')
      break
    case 'pet': // X线正电子发射断层扫描仪
      queryParams.device_type.push('X线正电子发射断层扫描仪')
      break
    case 'ct64': // 64排及以上X线计算机断层扫描仪
      queryParams.device_type.push('64排及以上X线计算机断层扫描仪')
      break
    case 'mri': // 1.5T及以上磁共振成像系统
      queryParams.device_type.push('1.5T及以上磁共振成像系统')
      break
    case 'linac': // 直线加速器
      queryParams.device_type.push('直线加速器')
      break
    case 'gamma': // 伽玛射线立体定向放射治疗系统
      queryParams.device_type.push('伽玛射线立体定向放射治疗系统')
      break
  }
  switch (suffixProperty) {
    case 'Gov': // 政府办医
      queryParams.institutionType = 2
      break
    case 'Society': // 社会办医
      queryParams.institutionType = 1
      break
    case 'Accepted': // 已验收
      queryParams.acceptanceStatus = 1
      break
    case 'NotAccepted': // 未验收
      queryParams.acceptanceStatus = 0
      break
  }
  queryParams.region = row.region
  getList()
}
// 弹窗
let filterVisible = ref(false)
let filterDialogProp = reactive({
  title: '高级筛选',
  width: '800px'
})
// 单纯重置筛选
const onlyReset = () => {
  queryParams.keyword = ''
  queryParams.year = ''
  queryParams.region = ''
  queryParams.step_config = ''
  queryParams.licenseStatus = ''
  queryParams.device_type = []
  // 验收状态
  queryParams.acceptanceStatus = ''
  // 机构性质
  queryParams.institutionType = ''

  queryParams.pageNo = 1
  queryParams.total = 0
}
// 重置筛选
const handleReset = () => {
  onlyReset()
  getList()
}
// 打开弹窗
const openDialog = () => {
  filterVisible.value = true
}
// 处理筛选
const handleFilter = () => {
  filterVisible.value = false
  getList()
}
onMounted(() => {
  getConfigScatterFn()
  getList()
})
</script>

<style lang="scss" scoped>
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  .left {
    .title {
      display: flex;
      align-items: center;
      line-height: 20px;
      font-size: 16px;
      font-weight: bold;
      color: #333;
      border-left: 4px solid #4245df;
      padding-left: 10px;
    }
  }
}
.content-row {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
  .item-box {
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 5px;
    border: 1px solid #e5e5e5;
    padding: 10px;
    border-radius: 8px;
    font-size: 14px;
    .label {
      font-weight: bold;
      color: #333;
    }
    .value {
      color: #066de3;
      font-weight: bold;
      margin-right: 5px;
    }
    .unit {
      font-size: 12px;
    }
  }
}
.custom-form-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-gap: 10px;
  &:deep(.el-form-item) {
    margin-bottom: 0;
    .el-form-item__label {
      font-size: 14px;
      margin-bottom: 0;
      line-height: 32px;
      font-weight: bold;
      color: #333;
    }
  }
  .form-row {
    grid-column: 1 / 5;
  }
  .el-checkbox-group {
    background-color: #f9f9f9;
    border-radius: 10px;
    padding: 0px 30px;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-gap: 10px;
  }
}
.custom-table-style {
  &:deep(.el-table__inner-wrapper) {
    .cell-active {
      background-image: linear-gradient(270deg, #9354f7 0%, #7327ec6a 100%) !important;
      color: #fff;
    }
  }
}
</style>
