<template>
  <div v-loading="loading" class="overview-container">
    <!-- 顶部筛选栏 -->
    <div class="filter-bar">
      <div class="filter-title">数据概览</div>
      <el-button type="primary" size="small" :icon="Filter" @click="openDialog">筛选</el-button>
    </div>

    <!-- 核心指标卡片 -->
    <div class="core-metrics">
      <div class="metric-card" v-for="item in coreMetrics" :key="item.key">
        <div class="metric-icon" :style="{ background: item.color }">
          <Icon :icon="item.icon" :size="28" color="#fff" />
        </div>
        <div class="metric-content">
          <div class="metric-value">{{ item.value }}</div>
          <div class="metric-label">{{ item.label }}</div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 第一行：3个图表 -->
      <div class="chart-row row-3">
        <!-- 办件统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              办件统计分布
            </div>
          </div>
          <div ref="handingChartRef" class="chart-container"></div>
        </div>

        <!-- 许可证统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              许可证设备分类统计
            </div>
          </div>
          <div ref="licenseChartRef" class="chart-container"></div>
        </div>

        <!-- 专家统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              专家领域分布
            </div>
          </div>
          <div ref="expertChartRef" class="chart-container"></div>
        </div>
      </div>

      <!-- 第二行：2个图表 -->
      <div class="chart-row row-2">
        <!-- 历史数据统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              历史设备数据统计
            </div>
          </div>
          <div ref="historyChartRef" class="chart-container"></div>
        </div>

        <!-- 公告统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              公告统计概览
            </div>
          </div>
          <div ref="noticeChartRef" class="chart-container"></div>
        </div>
      </div>

      <!-- 第三行：2个图表 -->
      <div class="chart-row row-2">
        <!-- 设备生产企业统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              设备生产企业分类统计
            </div>
          </div>
          <div ref="deviceChartRef" class="chart-container"></div>
        </div>

        <!-- 医疗机构统计 -->
        <div class="chart-card">
          <div class="chart-header">
            <div class="chart-title">
              <span class="title-icon"></span>
              医疗机构设备配置统计
            </div>
          </div>
          <div ref="hospitalChartRef" class="chart-container"></div>
        </div>
      </div>
    </div>

    <!-- 筛选弹窗 -->
    <Dialog v-model:model-value="filterVisible" v-bind="filterDialogProp">
      <el-form v-model="filterParams" label-position="top">
        <el-form-item label="年份" prop="fullYears">
          <el-date-picker
            v-model="filterParams.fullYears"
            type="year"
            format="YYYY年"
            value-format="YYYY"
            placeholder="请选择年份"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="区域" prop="region">
          <el-select
            v-model="filterParams.region"
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
      </el-form>
      <template #footer>
        <div style="display: flex; justify-content: center; align-items: center">
          <el-button type="info" @click="filterVisible = false">取消</el-button>
          <el-button type="primary" @click="handleFilter">筛选</el-button>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
import { Filter } from '@element-plus/icons-vue'
import { AnalysisApi } from '@/api/biz/analysis'
import * as echarts from 'echarts'

interface DictDataTypeT extends DictDataType {
  value: string | number
}

let regionOptions = computed<DictDataTypeT[]>(() => {
  return getDictOptions('biz_area_list')
})

// 核心指标数据
const coreMetrics = reactive([
  {
    label: '办件总量',
    value: 0,
    key: 'total_count',
    icon: 'mdi:file-document-multiple',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    label: '许可证总量',
    value: 0,
    key: 'license_total',
    icon: 'mdi:certificate',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    label: '历史数据总量',
    value: 0,
    key: 'history_total',
    icon: 'mdi:database',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    label: '专家总数',
    value: 0,
    key: 'expert_total',
    icon: 'mdi:account-group',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  }
])

// ECharts 实例引用
const handingChartRef = ref<HTMLElement>()
const licenseChartRef = ref<HTMLElement>()
const historyChartRef = ref<HTMLElement>()
const expertChartRef = ref<HTMLElement>()
const noticeChartRef = ref<HTMLElement>()
const deviceChartRef = ref<HTMLElement>()
const hospitalChartRef = ref<HTMLElement>()

let handingChart: echarts.ECharts | null = null
let licenseChart: echarts.ECharts | null = null
let historyChart: echarts.ECharts | null = null
let expertChart: echarts.ECharts | null = null
let noticeChart: echarts.ECharts | null = null
let deviceChart: echarts.ECharts | null = null
let hospitalChart: echarts.ECharts | null = null

// 数据存储
const chartData = reactive({
  handing: [] as any[],
  license: [] as any[],
  history: [] as any[],
  expert: [] as any[],
  notice: [] as any[],
  device: [] as any[],
  hospital: [] as any[]
})

let loading = ref(false)

// 初始化所有图表
const initCharts = () => {
  nextTick(() => {
    initHandingChart()
    initLicenseChart()
    initHistoryChart()
    initExpertChart()
    initNoticeChart()
    initDeviceChart()
    initHospitalChart()
  })
}

// 办件统计图表 - 饼图
const initHandingChart = () => {
  if (!handingChartRef.value) return
  handingChart = echarts.init(handingChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
      textStyle: { fontSize: 12 }
    },
    legend: {
      bottom: '3%',
      left: 'center',
      itemWidth: 12,
      itemHeight: 12,
      textStyle: { fontSize: 11 }
    },
    color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'],
    series: [
      {
        name: '办件统计',
        type: 'pie',
        radius: ['35%', '65%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 8,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: chartData.handing
      }
    ]
  }

  handingChart.setOption(option)
}

// 许可证统计图表 - 柱状图
const initLicenseChart = () => {
  if (!licenseChartRef.value) return
  licenseChart = echarts.init(licenseChartRef.value)

  const categories = chartData.license.map((item) => item.name)
  const values = chartData.license.map((item) => item.value)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      textStyle: { fontSize: 12 }
    },
    grid: {
      left: '8%',
      right: '4%',
      bottom: '12%',
      top: '8%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
        rotate: 25,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: { fontSize: 11 }
    },
    series: [
      {
        name: '数量',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        },
        barWidth: '35%'
      }
    ]
  }

  licenseChart.setOption(option)
}

// 历史数据统计图表 - 横向柱状图
const initHistoryChart = () => {
  if (!historyChartRef.value) return
  historyChart = echarts.init(historyChartRef.value)

  const categories = chartData.history.map((item) => item.name)
  const values = chartData.history.map((item) => item.value)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      textStyle: { fontSize: 12 }
    },
    grid: {
      left: '5%',
      right: '8%',
      bottom: '3%',
      top: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      axisLabel: { fontSize: 11 }
    },
    yAxis: {
      type: 'category',
      data: categories,
      axisLabel: { fontSize: 11 }
    },
    series: [
      {
        name: '数量',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
            { offset: 0, color: '#a18cd1' },
            { offset: 1, color: '#fbc2eb' }
          ])
        },
        barWidth: '45%',
        label: {
          show: true,
          position: 'right',
          formatter: '{c}',
          fontSize: 11
        }
      }
    ]
  }

  historyChart.setOption(option)
}

// 专家统计图表 - 玫瑰图
const initExpertChart = () => {
  if (!expertChartRef.value) return
  expertChart = echarts.init(expertChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)',
      textStyle: { fontSize: 12 }
    },
    legend: {
      bottom: '3%',
      left: 'center',
      itemWidth: 12,
      itemHeight: 12,
      textStyle: { fontSize: 11 }
    },
    color: ['#ff6b6b', '#4ecdc4', '#45b7d1', '#f9ca24', '#6c5ce7', '#a29bfe'],
    series: [
      {
        name: '专家分布',
        type: 'pie',
        radius: [15, 85],
        center: ['50%', '45%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 6
        },
        label: {
          fontSize: 11
        },
        data: chartData.expert
      }
    ]
  }

  expertChart.setOption(option)
}

// 公告统计图表 - 环形图
const initNoticeChart = () => {
  if (!noticeChartRef.value) return
  noticeChart = echarts.init(noticeChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      textStyle: { fontSize: 12 }
    },
    legend: {
      bottom: '3%',
      left: 'center',
      itemWidth: 12,
      itemHeight: 12,
      textStyle: { fontSize: 11 }
    },
    color: ['#36a3f7', '#34bfa3', '#f4516c', '#ffb822'],
    series: [
      {
        name: '公告统计',
        type: 'pie',
        radius: ['40%', '65%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        label: {
          show: true,
          formatter: '{b}\n{c}',
          fontSize: 11
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 14,
            fontWeight: 'bold'
          }
        },
        data: chartData.notice
      }
    ]
  }

  noticeChart.setOption(option)
}

// 设备生产企业统计图表 - 柱状图
const initDeviceChart = () => {
  if (!deviceChartRef.value) return
  deviceChart = echarts.init(deviceChartRef.value)

  const categories = chartData.device.map((item) => item.name)
  const values = chartData.device.map((item) => item.value)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      textStyle: { fontSize: 12 }
    },
    grid: {
      left: '8%',
      right: '4%',
      bottom: '12%',
      top: '8%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
        rotate: 25,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: { fontSize: 11 }
    },
    series: [
      {
        name: '企业数量',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#fa709a' },
            { offset: 1, color: '#fee140' }
          ])
        },
        barWidth: '40%',
        label: {
          show: true,
          position: 'top',
          fontSize: 11
        }
      }
    ]
  }

  deviceChart.setOption(option)
}

// 医疗机构统计图表 - 柱状图
const initHospitalChart = () => {
  if (!hospitalChartRef.value) return
  hospitalChart = echarts.init(hospitalChartRef.value)

  const categories = chartData.hospital.map((item) => item.name)
  const values = chartData.hospital.map((item) => item.value)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      textStyle: { fontSize: 12 }
    },
    grid: {
      left: '8%',
      right: '4%',
      bottom: '12%',
      top: '8%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
        rotate: 25,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: { fontSize: 11 }
    },
    series: [
      {
        name: '机构数量',
        type: 'bar',
        data: values,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#30cfd0' },
            { offset: 1, color: '#330867' }
          ])
        },
        barWidth: '40%',
        label: {
          show: true,
          position: 'top',
          fontSize: 11
        }
      }
    ]
  }

  hospitalChart.setOption(option)
}

// 更新图表数据
const updateChartsData = (data: any) => {
  // 更新核心指标
  coreMetrics[0].value = data.application?.total_count || 0
  coreMetrics[1].value = data.license?.total_count || 0
  coreMetrics[2].value = data.history?.total || 0
  coreMetrics[3].value = data.expert?.totalCount || 0

  // 办件统计数据
  chartData.handing = [
    { name: '证书申请', value: data.application?.cert_apply_count || 0 },
    { name: '证书补办', value: data.application?.cert_renew_count || 0 },
    { name: '证书变更', value: data.application?.cert_change_count || 0 },
    { name: '基本信息变更', value: data.application?.info_change_count || 0 },
    { name: '线下办理', value: data.application?.offline_count || 0 }
  ]

  // 许可证统计数据
  chartData.license = [
   {
      name: '1.5T及以上磁共振成像系统放射治疗类设备',
      value: data.device?.mriSystem1_5tPlus || 0
    },
    { name: 'X线正电子发射断层扫描仪', value: data.device?.petCtScanner || 0 },
    { name: '内窥镜手术器械控制系统', value: data.device?.endoscopicSurgicalSystem || 0 },
    {
      name: '64排及以上X线计算机断层扫描仪',
      value: data.device?.ctScanner64SlicePlus || 0
    },
    { name: '直线加速器', value: data.device?.linearAccelerator || 0 },
    { name: '伽玛射线立体定向放射治疗系统', value: data.device?.gammaRayStereotacticRtSystem || 0 }
  ]

  // 历史数据统计
  chartData.history = [
    { name: 'x线正电子发射断层扫描仪', value: data.history?.petCtScanner || 0 },
    { name: '内窥镜手术器械控制系统', value: data.history?.endoscopicSurgicalSystem || 0 },
    { name: '64排以上X线计算机断层扫描仪', value: data.history?.ctScanner64SlicePlus || 0 },
    { name: '1.5T及以上磁共振成像系统', value: data.history?.mriSystem1_5tPlus || 0 },
    { name: '直线加速器', value: data.history?.linearAccelerator || 0 },
    {
      name: '伽马射线立体定向放射治疗系统',
      value: data.history?.gammaRayStereotacticRtSystem || 0
    }
  ]

  // 专家统计数据
  chartData.expert = [
    { name: '放射影像', value: data.expert?.radiologyImaging || 0 },
    { name: '放射治疗', value: data.expert?.radiationTherapy || 0 },
    { name: '核医学', value: data.expert?.nuclearMedicine || 0 },
    { name: '卫生管理', value: data.expert?.healthManagement || 0 },
    { name: '医学设备与安全防护', value: data.expert?.medicalEquipmentSafety || 0 },
    { name: '医学智能工程', value: data.expert?.medicalIntelligentEngineering || 0 }
  ]

  // 公告统计数据
  chartData.notice = [
    { name: '已发布', value: data.notice?.publishedCount || 0 },
    { name: '未发布', value: data.notice?.draftCount || 0 },
    { name: '总浏览量', value: data.notice?.totalViews || 0 }
  ]

  // 设备生产企业数据
  chartData.device = [
    {
      name: '1.5T及以上磁共振成像系统放射治疗类设备',
      value: data.device?.mriSystem1_5tPlus || 0
    },
    { name: 'X线正电子发射断层扫描仪', value: data.device?.petCtScanner || 0 },
    { name: '内窥镜手术器械控制系统', value: data.device?.endoscopicSurgicalSystem || 0 },
    {
      name: '64排及以上X线计算机断层扫描仪',
      value: data.device?.ctScanner64SlicePlus || 0
    },
    { name: '直线加速器', value: data.device?.linearAccelerator || 0 },
    { name: '伽玛射线立体定向放射治疗系统', value: data.device?.gammaRayStereotacticRtSystem || 0 }
  ]

  // 医疗机构数据
  chartData.hospital = [
    {
      name: '1.5T及以上磁共振成像系统放射治疗类设备',
      value: data.device?.mriSystem1_5tPlus || 0
    },
    { name: 'X线正电子发射断层扫描仪', value: data.device?.petCtScanner || 0 },
    { name: '内窥镜手术器械控制系统', value: data.device?.endoscopicSurgicalSystem || 0 },
    {
      name: '64排及以上X线计算机断层扫描仪',
      value: data.device?.ctScanner64SlicePlus || 0
    },
    { name: '直线加速器', value: data.device?.linearAccelerator || 0 },
    { name: '伽玛射线立体定向放射治疗系统', value: data.device?.gammaRayStereotacticRtSystem || 0 }
  ]

  // 重新渲染所有图表
  handingChart?.setOption({ series: [{ data: chartData.handing }] })
  licenseChart?.setOption({
    xAxis: { data: chartData.license.map((item) => item.name) },
    series: [{ data: chartData.license.map((item) => item.value) }]
  })
  historyChart?.setOption({
    yAxis: { data: chartData.history.map((item) => item.name) },
    series: [{ data: chartData.history.map((item) => item.value) }]
  })
  expertChart?.setOption({ series: [{ data: chartData.expert }] })
  noticeChart?.setOption({ series: [{ data: chartData.notice }] })
  deviceChart?.setOption({
    xAxis: { data: chartData.device.map((item) => item.name) },
    series: [{ data: chartData.device.map((item) => item.value) }]
  })
  hospitalChart?.setOption({
    xAxis: { data: chartData.hospital.map((item) => item.name) },
    series: [{ data: chartData.hospital.map((item) => item.value) }]
  })
}

// 获取概览数据
const getOverviewData = async () => {
  try {
    loading.value = true
    let response = await Promise.all([
      AnalysisApi.getApplicationSummary({
        year: filterParams.fullYears
      }),
      AnalysisApi.getLicenseSummary({
        year: filterParams.fullYears
      }),
      AnalysisApi.getHistorySummary({
        year: filterParams.fullYears
      }),
      AnalysisApi.getExpertSummary(),
      AnalysisApi.getNoticeSummary({
        year: filterParams.fullYears
      }),
      AnalysisApi.getEquipmentCompanySummary({
        year: filterParams.fullYears
      }),
      AnalysisApi.getHealthcareSummary({
        year: filterParams.fullYears
      })
    ])

    const data = {
      application: response[0],
      license: response[1],
      history: response[2],
      expert: response[3],
      notice: response[4],
      device: response[5],
      hospital: response[6]
    }

    updateChartsData(data)
  } catch (err) {
    console.log(err)
  } finally {
    loading.value = false
  }
}

// 筛选弹窗
let filterParams = reactive({
  fullYears: '',
  region: ''
})
let filterVisible = ref(false)
let filterDialogProp = reactive({
  title: '筛选',
  width: '500px'
})

const openDialog = () => {
  filterVisible.value = true
}

const handleFilter = () => {
  filterVisible.value = false
  getOverviewData()
}

// 窗口大小改变时重新调整图表
const handleResize = () => {
  handingChart?.resize()
  licenseChart?.resize()
  historyChart?.resize()
  expertChart?.resize()
  noticeChart?.resize()
  deviceChart?.resize()
  hospitalChart?.resize()
}

onMounted(() => {
  initCharts()
  getOverviewData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  handingChart?.dispose()
  licenseChart?.dispose()
  historyChart?.dispose()
  expertChart?.dispose()
  noticeChart?.dispose()
  deviceChart?.dispose()
  hospitalChart?.dispose()
})
</script>

<style lang="scss" scoped>
// 响应式布局
@media (width <= 1600px) {
  .core-metrics {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    .chart-row {
      &.row-3 {
        grid-template-columns: repeat(2, 1fr);

        .chart-card:last-child {
          grid-column: span 2;
        }
      }
    }
  }
}

@media (width <= 1200px) {
  .charts-grid {
    .chart-row {
      &.row-3,
      &.row-2 {
        grid-template-columns: 1fr;
      }

      .chart-card:last-child {
        grid-column: span 1;
      }
    }
  }
}

@media (width <= 768px) {
  .overview-container {
    padding: 8px;
  }

  .filter-bar {
    padding: 8px 12px;
    margin-bottom: 8px;

    .filter-title {
      font-size: 15px;
    }
  }

  .core-metrics {
    grid-template-columns: 1fr;
    gap: 8px;
    margin-bottom: 8px;

    .metric-card {
      padding: 12px;

      .metric-icon {
        width: 44px;
        height: 44px;
        margin-right: 10px;
      }

      .metric-content .metric-value {
        font-size: 20px;
      }

      .metric-content .metric-label {
        font-size: 12px;
      }
    }
  }

  .charts-grid {
    gap: 8px;

    .chart-card {
      padding: 12px;

      .chart-header {
        margin-bottom: 8px;

        .chart-title {
          font-size: 13px;
        }
      }

      .chart-container {
        height: 240px;
      }
    }
  }
}

.overview-container {
  min-height: 100%;
  padding: 12px;
  background: var(--app-content-bg-color);
}

.filter-bar {
  display: flex;
  padding: 10px 16px;
  margin-bottom: 12px;
  background: #fff;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgb(0 0 0 / 6%);
  justify-content: space-between;
  align-items: center;

  .filter-title {
    position: relative;
    padding-left: 12px;
    font-size: 17px;
    font-weight: 600;
    color: #333;

    &::before {
      position: absolute;
      top: 50%;
      left: 0;
      width: 3px;
      height: 16px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 2px;
      content: '';
      transform: translateY(-50%);
    }
  }
}

.core-metrics {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 12px;

  .metric-card {
    display: flex;
    padding: 14px 16px;
    cursor: default;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgb(0 0 0 / 6%);
    transition: all 0.25s ease;
    align-items: center;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgb(0 0 0 / 10%);
    }

    .metric-icon {
      display: flex;
      width: 52px;
      height: 52px;
      margin-right: 14px;
      border-radius: 10px;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .metric-content {
      flex: 1;
      min-width: 0;

      .metric-value {
        margin-bottom: 2px;
        font-size: 24px;
        font-weight: bold;
        line-height: 1.2;
        color: #333;
      }

      .metric-label {
        overflow: hidden;
        font-size: 13px;
        color: #666;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

.charts-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .chart-row {
    display: grid;
    gap: 12px;

    &.row-3 {
      grid-template-columns: repeat(3, 1fr);
    }

    &.row-2 {
      grid-template-columns: repeat(2, 1fr);
    }
  }

  .chart-card {
    padding: 14px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgb(0 0 0 / 6%);
    transition: all 0.25s ease;

    &:hover {
      box-shadow: 0 3px 12px rgb(0 0 0 / 10%);
    }

    .chart-header {
      margin-bottom: 10px;

      .chart-title {
        display: flex;
        align-items: center;
        font-size: 14px;
        font-weight: 600;
        color: #333;

        .title-icon {
          width: 3px;
          height: 14px;
          margin-right: 8px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          border-radius: 2px;
        }
      }
    }

    .chart-container {
      width: 100%;
      height: 260px;
    }
  }
}
</style>
