<template>
  <div class="process-other">
    <div class="header-row">
      <div class="left">
        <el-icon><Monitor /></el-icon>
        <span>办件详情 - 陕西省大型医用设备在线审批归档系统</span>
      </div>
      <div class="right">
        <el-button type="primary" :icon="Back" @click="goBack">返回办件中心</el-button>
      </div>
    </div>
    <div class="content-page">
      <div class="scroll-content">
        <div class="content-basis-msg">
          <div class="title-row">
            <el-icon><Management /></el-icon>
            <span>西安交通大学第一附属医院</span>
          </div>
          <div class="other-row">
            <div class="col"
              ><span class="label">申请编号：</span>
              <span class="value">SQ-2024-09-23-0931</span>
            </div>
            <div class="line"></div>
            <div class="col">
              <span class="label">设备名称:</span>
              <span class="value">X线正电子发射断层扫描仪</span>
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
              <el-icon><component :is="item.icon" class="el-icon" /></el-icon>
              <span>{{ item.label }}</span>
            </div>
          </div>
          <div class="type-content-page">
            <transition name="fade" mode="out-in">
              <component :is="typeActive.component" :key="typeActive.value" />
            </transition>
          </div>
          <div class="handler-list">
            <el-button size="default" type="success" :icon="Check">提交</el-button>
            <el-button size="default" type="primary" :icon="Checked">暂存</el-button>
            <el-button size="default" type="info" :icon="Back">返回</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts" name="ProcessOther">
import BasicInfo from '@/views/Processing/components/basis.vue'
import {
  Back,
  Monitor,
  Management,
  InfoFilled,
  Connection,
  Search,
  Avatar,
  Check,
  Checked
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { markRaw } from 'vue'

const router = useRouter()
let props = defineProps({
  id: { type: String, default: '' },
  handler: { type: String, default: 'view' }
})
// 返回上一页
const goBack = () => {
  router.back()
}

// 基础数据
let basice = reactive({
  title: '西安交通大学第一附属医院',
  code: 'DQ-2024-09-23-0931',
  // 设备名称
  deviceName: 'X线正电子发射断层扫描仪'
})
let typeList = ref([
  {
    label: '基本信息',
    value: 'basicInfo',
    component: markRaw(BasicInfo),
    icon: markRaw(InfoFilled)
  },
  { label: '业务信息', value: 'deviceInfo', component: 'DeviceInfo', icon: markRaw(Management) },
  { label: '材料列表', value: 'useInfo', component: 'UseInfo', icon: markRaw(Connection) },
  { label: '初步审核', value: 'firstAudit', component: 'UseInfo', icon: markRaw(Search) },
  { label: '专家审核', value: 'expertAudit', component: 'UseInfo', icon: markRaw(Avatar) }
])
let typeActive = ref({ value: 'basicInfo', component: 'BasicInfo' })
const handlerType = (item) => {
  typeActive.value.value = item.value
  typeActive.value.component = item.component
}
</script>

<style lang="scss" scoped>
.process-other {
  .header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    background-image: linear-gradient(to right, #282ffc, #1c93f4);
    height: 54px;
    .left {
      display: flex;
      align-items: center;
      color: #fff;
      font-size: 18px;
      span {
        margin-left: 10px;
      }
    }
    .right {
    }
  }
  .content-page {
    height: calc(100vh - 54px);
    overflow-y: auto;
    background-color: #f5f7fa;
    .scroll-content {
      max-width: 1240px;
      margin: 0 auto;
      padding: 20px;
      .content-basis-msg {
        padding: 20px;
        border-radius: 10px;
        border: 1px solid rgba(226, 232, 240, 0.8);
        border-left: 4px solid #165dff;
        background-color: #fff;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        margin-bottom: 20px;
        .title-row {
          height: 32px;
          line-height: 1;
          margin-bottom: 10px;
          display: flex;
          align-items: center;
          .el-icon {
            color: #165dff;
            font-size: 24px;
          }
          span {
            color: #165dff;
            font-size: 24px;
            font-weight: 600;
            margin-left: 6px;
          }
        }
        .other-row {
          display: flex;
          align-items: center;
          .col {
            color: #64748b;
            font-size: 14px;
            .label {
              margin-right: 6px;
            }
          }
          .line {
            width: 1px;
            height: 14px;
            background-color: #e2e8f0;
            margin: 0 12px;
          }
        }
      }
      .content-type-msg {
        border: 1px solid rgba(226, 232, 240, 0.8);
        padding: 4px;
        border-radius: 10px;
        background-color: #fff;
        box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
        .type-tag-list {
          display: flex;
          align-items: center;
          .type-tag-item {
            height: 44px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: #64748b;
            border-radius: 8px;
            padding: 4px 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            span {
              margin-left: 4px;
            }
            &.active {
              background-image: linear-gradient(135deg, #165dff, #3b82f6);
              color: #fff;
            }
            &:hover {
              background-image: linear-gradient(135deg, #165dff, #3b82f6);
              color: #fff;
            }
            & + .type-tag-item {
              margin-left: 12px;
            }
          }
        }
        .type-content-page {
          min-height: 320px;
        }
        .handler-list {
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 10px;
        }
      }
    }
  }
}
</style>
