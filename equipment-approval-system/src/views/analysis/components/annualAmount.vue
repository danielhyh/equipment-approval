<template>
  <div>
    <!-- 年度递增情况 -->
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
            年度递增情况
          </div>
        </div>
      </div>
      <div class="content-row">
        <div class="item-ground" v-for="(item, index) in yearCountData" :key="index">
          <div class="item-box" v-for="ie in item" :key="ie.key + '_' + index">
            <span class="value">{{ ie.value }}</span>
            <div class="label">{{ ie.label }}</div>
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
          <el-button type="success" size="small">
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
        <el-table-column prop="index" label="序号" width="60" :index="customIndex" align="center" />
        <!-- 所属机构 -->
        <el-table-column prop="org_name" label="所属机构" align="center" />
        <!-- 医疗机构 -->
        <el-table-column prop="hospital_name" label="医疗机构" align="center" />
        <!-- 设备类型 -->
        <el-table-column prop="device_type" label="设备类型" align="center" />
        <!-- 阶梯配置 -->
        <el-table-column prop="step_config" label="阶梯配置" align="center" />
        <!-- 许可证编号 -->
        <el-table-column prop="license_no" label="许可证编号" align="center" />
        <!-- 发证日期 -->
        <el-table-column prop="issue_date" label="发证日期" align="center" />
        <!-- 装机日期 -->
        <el-table-column prop="install_date" label="装机日期" align="center" />
        <!-- 生产企业 -->
        <el-table-column prop="manufacturer" label="生产企业" align="center" />
        <!-- 具体型号 -->
        <el-table-column prop="model" label="具体型号" align="center" />
        <!-- 副本发证日期 -->
        <el-table-column prop="copy_issue_date" label="副本发证日期" align="center" />
        <!-- 状态 -->
        <el-table-column prop="status" label="状态" align="center" />
        <!-- 统计年份 -->
        <el-table-column prop="year" label="统计年份" align="center" />
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
              :value="item.value"
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
              :value="item.value"
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
            <el-checkbox
              v-for="item in deviceTypeOptions"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            >
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
let yearCountData = ref([
  [
    // 2025年总量
    {
      key: 'total_count',
      label: '2025年总量',
      value: 100
    },
    // 新增数量
    {
      key: 'new_count',
      label: '新增数量',
      value: 10
    },
    // 增长率
    {
      key: 'growth_rate',
      label: '增长率',
      value: '10%'
    }
  ],
  [
    // 2024年总量
    {
      key: 'total_count',
      label: '2024年总量',
      value: 100
    },
    // 新增数量
    {
      key: 'new_count',
      label: '新增数量',
      value: 10
    },
    // 增长率
    {
      key: 'growth_rate',
      label: '增长率',
      value: '10%'
    }
  ],
  [
    // 2023年总量
    {
      key: 'total_count',
      label: '2023年总量',
      value: 100
    },
    // 新增数量
    {
      key: 'new_count',
      label: '新增数量',
      value: 10
    },
    // 增长率
    {
      key: 'growth_rate',
      label: '增长率',
      value: '10%'
    }
  ],
  [
    // 2022年总量
    {
      key: 'total_count',
      label: '2022年总量',
      value: 100
    },
    // 新增数量
    {
      key: 'new_count',
      label: '新增数量',
      value: 10
    },
    // 增长率
    {
      key: 'growth_rate',
      label: '增长率',
      value: '10%'
    }
  ]
])
let loading = ref(false)
let tableData = ref([
  {
    index: 1,
    org_name: '陕西省西安市',
    hospital_name: '陕西省西安市医院',
    device_type: 'CT扫描器',
    step_config: '阶梯1',
    license_no: '123456789012345678',
    issue_date: '2023-01-01',
    install_date: '2023-02-01',
    manufacturer: '中国医疗设备有限公司',
    model: 'CT-123456',
    copy_issue_date: '2023-03-01',
    status: '正常',
    year: '2023'
  }
])
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
const getList = async () => {}

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
}
</script>

<style lang="scss" scoped>
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
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding: 5px;
  padding-top: 14px;
  .item-ground {
    display: flex;
    justify-content: space-around;
    align-items: flex-end;
    gap: 10px;
    box-shadow: 0 0 6px #dfdfdf;
    border-radius: 6px;
    flex-basis: calc(25% - 20px);
  }
  .item-box {
    gap: 5px;
    padding: 10px;
    border-radius: 8px;
    font-size: 14px;
    .label {
      font-weight: bold;
      color: #333;
      font-size: 12px;
      white-space: nowrap;
    }
    .value {
      color: #066de3;
      font-weight: bold;
      font-size: 18px;
      margin-right: 5px;
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
}
</style>
