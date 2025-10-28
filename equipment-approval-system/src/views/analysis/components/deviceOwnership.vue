<template>
  <div>
    <!-- 区域覆盖 -->
    <div class="region-coverage">
      <div class="title-row">
        <div class="left">
          <div class="title">
            <Icon
              icon="material-symbols:map-pin-review-rounded"
              color="#4245df"
              :size="18"
              style="margin-right: 5px"
            />
            区域分布情况(覆盖14个地市)
          </div>
        </div>
      </div>
      <div class="content-row">
        <div class="item-box" v-for="item in regionData" :key="item.key">
          <div class="label">{{ item.label }}</div>
          <div class="value-unit">
            <span class="value">{{ item.value }}</span>
            <span class="unit">台</span>
          </div>
        </div>
      </div>
    </div>
    <!-- 设备详细信息 -->
    <div class="device-info">
      <div class="title-row" style="margin-bottom: 10px">
        <div class="left">
          <div class="title">
            <Icon
              icon="material-symbols:map-pin-review-rounded"
              color="#4245df"
              :size="18"
              style="margin-right: 5px"
            />
            设备详细信息
          </div>
        </div>
        <div class="right">
          <el-button type="primary" size="small" :icon="Filter" @click.stop="openDialog">
            筛选
          </el-button>
          <el-button type="success" size="small" @click.stop="exportExcelFn">
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
        <el-table-column prop="region" label="所属区域" align="center"/>
        <!-- 所属机构 -->
        <el-table-column prop="institutionName" label="医疗机构" align="center" />
        <!-- 医疗机构 -->
        <!-- <el-table-column prop="hospital_name" label="医疗机构" align="center" /> -->
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

        <!-- 统计年份 -->
        <!-- <el-table-column prop="year" label="统计年份" align="center" /> -->
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
let regionData = ref<{ key: string | number; label: string; value: number }[]>([
  {
    key: 'xiaan',
    label: '西安市',
    value: 0
  },
  // 宝鸡市
  {
    key: 'baoji',
    label: '宝鸡市',
    value: 0
  },
  // 汉中市
  {
    key: 'hanzhong',
    label: '汉中市',
    value: 0
  },
  // 咸阳市
  {
    key: 'sanya',
    label: '咸阳市',
    value: 0
  },
  // 渭南市
  {
    key: 'weinan',
    label: '渭南市',
    value: 0
  },
  // 延安市
  {
    key: 'yianan',
    label: '延安市',
    value: 0
  },
  // 安康市
  {
    key: 'ankang',
    label: '安康市',
    value: 0
  },
  // 榆林市
  {
    key: 'yulin',
    label: '榆林市',
    value: 0
  }
])
regionData.value = regionOptions.value.map((item) => ({
  key: item.value,
  label: item.label,
  value: 0
}))
const getTotalData = async () => {
  try {
    const response = await AnalysisApi.getRegionDevice()
    if (Array.isArray(response) && response.length > 0) {
      response.forEach((item) => {
        const index = regionData.value.findIndex(
          (regionItem: any) => regionItem.label === item.region
        )
        if (index !== -1) {
          regionData.value[index].value = item.total
        }
      })
    }
  } catch (err) {
    console.log(err)
  }
}
let loading = ref(false)
let tableData = ref([])
let queryParams = reactive({
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
  device_type: []
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
    deviceTypes: queryParams.device_type.join(',')
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

// 弹窗
let filterVisible = ref(false)
let filterDialogProp = reactive({
  title: '高级筛选',
  width: '800px'
})
// 重置筛选
const handleReset = () => {
  queryParams.keyword = ''
  queryParams.year = ''
  queryParams.region = ''
  queryParams.step_config = ''
  queryParams.licenseStatus = ''
  queryParams.device_type = []

  queryParams.pageNo = 1
  queryParams.total = 0
  // filterVisible.value = false
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
// 导出excel
const exportExcelFn = () => {
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
    deviceTypes: queryParams.device_type.join(',')
  }
  loading.value = true
  AnalysisApi.exportExcel(params)
    .then((res) => {
      const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
      const fileName = '设备详细信息.xlsx'
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = fileName
      link.click()
      URL.revokeObjectURL(link.href)
    })
    .finally(() => {
      loading.value = false
    })
}
onMounted(() => {
  getTotalData()
  getList()
})
</script>

<style lang="scss" scoped>
.device-info::after {
  content: '';
  display: block;
  clear: both;
}
.region-coverage {
  background-color: #fff;
  padding: 10px;
  border-radius: 10px;
  margin-bottom: 10px;
}
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
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 20px;
  margin-top: 20px;
  .item-box {
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
    .value-unit {
      flex-shrink: 0;
    }
    .value {
      flex-shrink: 0;
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
</style>
