<template>
  <div class="history-detail-box" v-loading="loading">
    <div class="content-basis-msg">
      <div class="title-row title-flex">
        <div class="left">
          <el-icon><Management /></el-icon>
          <span>{{ basice.title }}</span>
        </div>
        <div class="right">
          <el-button type="primary" :icon="Back" @click="goBack">返回上一页</el-button>
        </div>
      </div>
      <div class="other-row">
        <div class="col">
          <span class="label">许可证编号：</span>
          <span class="value">{{ licenseNumber }}</span>
        </div>
        <div class="line"></div>
        <div class="col">
          <span class="label">设备名称：</span>
          <span class="value">{{ basice.deviceName }}</span>
        </div>
        <div class="line"></div>
        <div class="col">
          <span class="label">设备状态：</span>
          <span class="value">
            <el-tag type="info">{{ deviceStatus }}</el-tag>
          </span>
        </div>
      </div>
    </div>
    <div class="content-type-msg">
      <div class="type-tag-list">
        <div
          class="type-tag-item"
          :class="{ active: typeActive.value === item.value }"
          v-for="(item, i) in typeList"
          :key="i"
          @click.stop="handlerType(item)"
        >
          <Icon :icon="item.icon" />
          <span>{{ item.label }}</span>
        </div>
      </div>
      <div class="type-content-page">
        <transition name="fade" mode="out-in">
          <KeepAlive>
            <component
              ref="typeRef"
              :list="allLicenseData"
              :disabled="true"
              :is="typeActive.component"
              :key="typeActive.value"
            />
          </KeepAlive>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="HistoryDetail">
import { HistoryApi } from '@/api/biz/history'
import { Back, Management } from '@element-plus/icons-vue'
import OriginalInfo from './components/historyOriginal.vue'
import CopyInfo from './components/historyCopy.vue'

const router = useRouter()
const route = useRoute()

const goBack = () => {
  router.back()
}

const historyId = route.query.id
const {licenseNumber, deviceStatus} = route.query
let loading = ref(false)

// 基础数据
let basice = reactive({
  title: '',
  licenseNo: '',
  deviceName: '',
  deviceStatus: ''
})

let typeList = ref([
  {
    label: '正本信息',
    value: 'originalInfo',
    component: markRaw(OriginalInfo),
    icon: 'ep:checked'
  },
  {
    label: '副本信息',
    value: 'copyInfo',
    component: markRaw(CopyInfo),
    icon: 'ep:document-copy'
  }
])

let typeActive = ref<{ value: string; component: any }>({
  value: 'originalInfo',
  component: markRaw(OriginalInfo)
})

const handlerType = (item) => {
  if (!item.component) return
  typeActive.value.value = item.value
  typeActive.value.component = item.component
}

let allLicenseData = ref({})

const getDetailInfo = async () => {
  try {
    loading.value = true
    const [originalData, copyData] = await Promise.all([
      HistoryApi.getHistoryOriginal({ id: historyId }),
      HistoryApi.getHistoryCopy({ id: historyId })
    ])

    // 设置基础信息
    basice.title = originalData.configUnitName || copyData.configUnitName || ''
    basice.licenseNo = originalData.licenseNumber || copyData.licenseNumber || ''
    basice.deviceName = originalData.licenseDeviceName || copyData.licenseDeviceName || ''
    basice.deviceStatus = originalData.deviceStatus || copyData.deviceStatus || ''

    // 合并正本和副本数据
    allLicenseData.value = { 
      ...originalData, 
      ...copyData,
      code: originalData.licenseNumber || copyData.licenseNumber
    }

    loading.value = false
  } catch (err) {
    console.error('获取历史数据详情失败:', err)
    loading.value = false
  }
}

onMounted(() => {
  getDetailInfo()
})
</script>

<style lang="scss" scoped>
.history-detail-box {
  padding: 10px;

  .content-basis-msg {
    padding: 20px;
    margin-bottom: 20px;
    background-color: #fff;
    border: 1px solid rgb(226 232 240 / 80%);
    border-left: 4px solid #165dff;
    border-radius: 10px;
    box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);

    .title-row {
      display: flex;
      height: 32px;
      margin-bottom: 10px;
      line-height: 1;
      align-items: center;

      .el-icon {
        font-size: 24px;
        color: #165dff;
      }

      span {
        margin-left: 6px;
        font-size: 24px;
        font-weight: 600;
        color: #165dff;
      }

      &.left {
        display: flex;
        justify-content: center;
      }

      &.title-flex {
        justify-content: space-between;
      }
    }

    .other-row {
      display: flex;
      align-items: center;

      .col {
        font-size: 14px;
        color: #64748b;

        .label {
          margin-right: 6px;
        }
      }

      .line {
        width: 1px;
        height: 14px;
        margin: 0 12px;
        background-color: #e2e8f0;
      }
    }
  }

  .content-type-msg {
    padding: 4px;
    background-color: #fff;
    border: 1px solid rgb(226 232 240 / 80%);
    border-radius: 10px;
    box-shadow: 0 2px 4px 0 rgb(0 0 0 / 10%);

    .type-tag-list {
      display: flex;
      align-items: center;
      overflow: auto;

      .type-tag-item {
        display: flex;
        height: 44px;
        padding: 4px 14px;
        font-size: 14px;
        color: #64748b;
        cursor: pointer;
        border-radius: 8px;
        transition: all 0.3s ease;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        span {
          margin-left: 4px;
        }

        &.active {
          color: #fff;
          background-image: linear-gradient(135deg, #165dff, #3b82f6);
        }

        &:hover {
          color: #fff;
          background-image: linear-gradient(135deg, #165dff, #3b82f6);
        }

        & + .type-tag-item {
          margin-left: 12px;
        }
      }
    }

    .type-content-page {
      min-height: 320px;
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
