<template>
  <div>
    <el-card class="card-box">
      <!-- 头部区域 -->
      <template #header>
        <div class="header-section">
          <h2 class="page-title">
            <Icon icon="fa-solid:chart-pie" :size="18" color="#165DFF" style="margin-right: 5px" />
            <span>设备汇总统计</span>
          </h2>
          <el-select v-model="selectedYear" class="year-selector" @change="getStatisticsData">
            <el-option
              v-for="item in selectYearList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
      </template>
      <!-- 统计卡片区域 -->
      <div class="stats-cards-container">
        <!-- 甲类大型医用设备卡片 -->
        <div class="equipment-type-card equipment-card-red">
          <div class="card-header">
            <h3 class="card-title c-ef4444">甲类大型医用设备</h3>
          </div>
          <div class="total-type-count c-ef4444">{{ equipmentATotal }} 台</div>
          <div class="equipment-list">
            <div class="equipment-item" v-for="item in equipmentA" :key="item.key">
              <span class="equipment-name">{{ item.label }}</span>
              <span class="equipment-count c-ef4444">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 乙类大型医用设备卡片 -->
        <div class="equipment-type-card equipment-card-cyan">
          <div class="card-header">
            <h3 class="card-title c-06b6d4">乙类大型医用设备</h3>
          </div>
          <div class="total-type-count c-06b6d4">{{ equipmentBTotal }} 台</div>
          <div class="equipment-list">
            <div class="equipment-item" v-for="item in equipmentB" :key="item.key">
              <span class="equipment-name">{{ item.label }}</span>
              <span class="equipment-count c-06b6d4">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 已办理许可证卡片 -->
        <div class="equipment-type-card equipment-card-green">
          <div class="card-header">
            <h3 class="card-title c-10b981">已办理许可证</h3>
          </div>
          <div class="total-type-count c-10b981">{{ licenseData.total }} 个</div>
          <div class="equipment-list">
            <div class="equipment-item">
              <span class="equipment-name">线上办理证书数量</span>
              <span class="equipment-count c-10b981">{{ licenseData.onlineCount }}</span>
            </div>
            <div class="equipment-item">
              <span class="equipment-name">线下办理证书数量</span>
              <span class="equipment-count c-10b981">{{ licenseData.offlineCount }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 初审列表 -->
    <el-card class="card-box">
      <template #header>
        <div class="header-section">
          <h2 class="page-title">
            <Icon icon="tabler:bell-filled" :size="18" color="#165DFF" style="margin-right: 5px" />
            <span>初审列表</span>
          </h2>
        </div>
      </template>

      <div class="review-table-box">
        <el-table :data="reviewList" style="width: 100%" stripe>
          <el-table-column type="index" label="序号" width="80" align="center" />
          <el-table-column
            prop="institutionName"
            label="配置单位"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column
            prop="deviceName"
            label="设备名称"
            min-width="180"
            show-overflow-tooltip
          />
          <el-table-column label="类型" width="150" align="center">
            <template #default="{ row }">
              <el-tag :type="getReviewTagType(row.appType)" size="small">
                {{ getReviewTypeText(row.appType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="申请时间" width="180" align="center" />
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleReview(row)">
                立即办理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {
  getEquipmentSummary,
  getProcessedLicenseSummary,
  getPreliminaryReviewList
} from '@/api/biz/home/index'
import { useApplicationDataStore } from '@/store/applicationData'
const applicationDataStore = useApplicationDataStore()

const router = useRouter()
// 年份选择器的响应式数据
const selectedYear = ref(new Date().getFullYear() + '')
interface SelectYearItem {
  label: string
  value: string
}
const selectYearList = computed<SelectYearItem[]>(() => {
  let currentYear = new Date().getFullYear()
  let arr: SelectYearItem[] = []
  for (let i = 0; i < 4; i++) {
    arr.push({
      label: currentYear - i + '年',
      value: String(currentYear - i)
    })
  }
  return arr
})

interface EquipmentItem {
  label: string
  value: number | string
  key?: string
}
// 甲类大型医疗设备 数据
let equipmentA = ref<EquipmentItem[]>([
  {
    label: '重离子质子放射治疗系统',
    value: 0,
    key: 'heavyIonProtonRtSystem'
  },
  {
    label: '高端放射治疗设备',
    value: 0,
    key: 'highEndRtEquipment'
  },
  {
    label: '首次配置的大型医疗器械',
    value: 0,
    key: 'firstTimeLargeMedicalDevice'
  }
])
let equipmentATotal = computed<number>(() => {
  return equipmentA.value.reduce((pre, cur) => pre + Number(cur.value), 0)
})
// 乙类大型医疗设备 数据
let equipmentB = ref<EquipmentItem[]>([
  {
    label: 'X线正电子发射断层扫描仪',
    value: 0,
    key: 'petCtScanner'
  },
  {
    label: '伽玛射线立体定向放射治疗系统',
    value: 0,
    key: 'gammaRayStereotacticRtSystem'
  },
  {
    label: '直线加速器',
    value: 0,
    key: 'linearAccelerator'
  },
  {
    label: '内窥镜手术器械控制系统',
    value: 0,
    key: 'endoscopicSurgicalSystem'
  },
  {
    label: '1.5T及以上磁共振成像系统',
    value: 0,
    key: 'mriSystem1_5tPlus'
  },
  {
    label: '64排及以上X线计算机断层扫描仪',
    value: 0,
    key: 'ctScanner64SlicePlus'
  }
])
let equipmentBTotal = computed<number>(() => {
  return equipmentB.value.reduce((pre, cur) => pre + Number(cur.value), 0)
})

// 已办理许可证数据
interface LicenseData {
  total: number | string
  onlineCount: number | string
  offlineCount: number | string
}
let licenseData = reactive<LicenseData>({
  total: 0,
  onlineCount: 0,
  offlineCount: 0
})

// 获取设备汇总统计数据
const getEquipmentSummaryData = async () => {
  try {
    const response = await getEquipmentSummary(Number(selectedYear.value))
    let res = response.list[0] || {}
    equipmentA.value.forEach((item: EquipmentItem) => {
      item.value = item.key ? res[item?.key] : 0
    })
    equipmentB.value.forEach((item: EquipmentItem) => {
      item.value = item.key ? res[item?.key] : 0
    })
  } catch (e) {
    console.log(e)
  }
}

// 获取已办理许可证统计数据
const getProcessedLicenseSummaryData = async () => {
  try {
    const response = await getProcessedLicenseSummary(Number(selectedYear.value))
    licenseData.total = response.total || 0
    licenseData.onlineCount = response.onlineCount || 0
    licenseData.offlineCount = response.offlineCount || 0
  } catch (e) {
    console.log(e)
  }
}

// 统一获取统计数据
const getStatisticsData = () => {
  getEquipmentSummaryData()
  getProcessedLicenseSummaryData()
}
getStatisticsData()

// 初审列表相关
interface ReviewItem {
  appType: string
  institutionName: string
  createTime: string
  deviceName: string
}
let reviewList = ref<ReviewItem[]>([])

// 时间戳转换为日期格式
const formatTimestamp = (timestamp: number | string): string => {
  if (!timestamp) return '--'
  const date = new Date(Number(timestamp))
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const getPreliminaryReviewListData = async () => {
  try {
    const res = await getPreliminaryReviewList()
    reviewList.value = res.map((item) => {
      const appTypeStr = String(item.app_type)
      return {
        appType: appTypeStr,
        institutionName: item.institution_name || '--',
        createTime: formatTimestamp(item.create_time),
        deviceName: appTypeStr === '4' ? '基本信息变更' : item.license_device_name || '--'
      }
    })
  } catch (e) {
    console.log(e)
  }
}
getPreliminaryReviewListData()

// 跳转到办件中心
// const handleReview = (item: ReviewItem) => {
//   applicationDataStore.updateProcessingType(getProcessType(item.appType))
//   router.push({
//     path: '/processing'
//   })
// }
const handleReview = (item: ReviewItem) => {
  applicationDataStore.updateProcessingType(getProcessType(item.appType))

  // 根据类型跳转到不同页面
  if (item.appType === '1') {
    router.push({
      path: '/processing',
      query: {
        type: 'apply' // 证书申请类型
      }
    })
  } else if (item.appType === '2') {
    router.push({
      path: '/processing',
      query: {
        type: 'reissue' // 证书补办类型
      }
    })
  } else if (item.appType === '3') {
    router.push({
      path: '/processing',
      query: {
        type: 'change' // 证书变更类型
      }
    })
  } else if (item.appType === '4') {
    router.push({
      path: '/processing',
      query: {
        type: 'basicInfoChange' // 基本信息变更类型
      }
    })
  }
}

const getProcessType = (appType: string) => {
  switch (appType) {
    case '1':
      return 'apply'
    case '2':
      return 'reissue'
    case '3':
      return 'change'
    case '4':
      return 'basicInfoChange'
    default:
      return 'apply'
  }
}

// 获取类型文本
const getReviewTypeText = (appType: string): string => {
  switch (appType) {
    case '1':
      return '乙类许可证申请'
    case '2':
      return '乙类许可证补办'
    case '3':
      return '乙类许可证变更'
    case '4':
      return '基本信息变更'
    default:
      return '乙类许可证申请'
  }
}

// 获取标签类型
const getReviewTagType = (appType: string): string => {
  switch (appType) {
    case '1':
      return 'primary'
    case '2':
      return 'success'
    case '3':
      return 'warning'
    case '4':
      return 'danger'
    default:
      return 'primary'
  }
}
</script>

<style lang="scss" scoped>
@use './index.scss';

.c-10b981 {
  color: #10b981;
}

.review-table-box {
  min-height: 300px;
}

// 甲类设备卡片 - 红色系
.equipment-card-red {
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #fee2e2 0%, #fef2f2 100%);
  border: 2px solid #fecaca;
  box-shadow: 0 4px 12px rgb(239 68 68 / 15%);

  &::before {
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    pointer-events: none;
    background: radial-gradient(circle, rgb(239 68 68 / 8%) 0%, transparent 70%);
    content: '';
  }

  .card-title,
  .total-type-count,
  .equipment-count {
    font-weight: 600;
    color: #dc2626 !important;
  }

  .equipment-item {
    background: rgb(255 255 255 / 60%);
  }
}

// 乙类设备卡片 - 青色系
.equipment-card-cyan {
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #cffafe 0%, #f0fdfa 100%);
  border: 2px solid #a5f3fc;
  box-shadow: 0 4px 12px rgb(6 182 212 / 15%);

  &::before {
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    pointer-events: none;
    background: radial-gradient(circle, rgb(6 182 212 / 8%) 0%, transparent 70%);
    content: '';
  }

  .card-title,
  .total-type-count {
    font-weight: 600;
    color: #0891b2 !important;
  }

  .equipment-count {
    font-weight: 600;
    color: #0891b2 !important;
  }

  .equipment-item {
    background: rgb(255 255 255 / 60%);
  }
}

// 已办理许可证卡片 - 绿色系
.equipment-card-green {
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #d1fae5 0%, #f0fdf4 100%);
  border: 2px solid #a7f3d0;
  box-shadow: 0 4px 12px rgb(16 185 129 / 15%);

  &::before {
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    pointer-events: none;
    background: radial-gradient(circle, rgb(16 185 129 / 8%) 0%, transparent 70%);
    content: '';
  }

  .card-title,
  .total-type-count,
  .equipment-count {
    font-weight: 600;
    color: #059669 !important;
  }

  .equipment-item {
    background: rgb(255 255 255 / 60%);
  }
}

// 增强所有设备卡片的交互效果
.equipment-type-card {
  transition: all 0.3s ease;

  // &:hover {
  //   transform: translateY(-4px);
  //   box-shadow: 0 8px 24px rgb(0 0 0 / 12%);
  // }

  .equipment-item {
    position: relative;
    z-index: 1;
    transition: all 0.2s ease;
    backdrop-filter: blur(10px);

    &:hover {
      background: rgb(255 255 255 / 90%);
      transform: translateX(4px);
    }
  }
}
</style>
