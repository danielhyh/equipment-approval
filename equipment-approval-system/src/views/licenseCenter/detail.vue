<template>
  <div class="license-detail-box" v-loading="loading">
    <!-- <div class="header-row">
      <div class="left">
        <el-icon><Monitor /></el-icon>
        <span>许可证详情 - {{ title }}</span>
      </div>
    </div> -->
    <div class="content-page">
      <div class="scroll-content">
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
            <div class="col"
              ><span class="label">申请编号：</span>
              <span class="value">{{ basice.code }}</span>
            </div>
            <div class="line"></div>
            <div class="col">
              <span class="label">设备名称:</span>
              <span class="value">{{ basice.deviceName }}</span>
            </div>
            <div class="line"></div>
            <div class="col">
              <span class="label">验收状态:</span>
              <span class="value"
                ><el-tag
                  :type="
                    basice.status === '通过'
                      ? 'success'
                      : basice.status === '不通过'
                        ? 'danger'
                        : basice.status === '驳回整改'
                          ? 'warning'
                          : 'info'
                  "
                >
                  {{ basice.status || '待验收' }}
                </el-tag>
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
    </div>
  </div>
</template>

<script setup lang="ts" name="LicenseCenterDetail">
import { ApplicationApi } from '@/api/biz/application'
import { LicenseApi } from '@/api/biz/license'
import { Back, Monitor, Management } from '@element-plus/icons-vue'
import BasicInfo from './components/basis.vue'
import Business from './components/business.vue'
import UseInfo from './components/useInfo.vue'
import Material from './components/material.vue'
import Preliminary from './components/preliminary.vue'
import Expert from './components/expert.vue'
import CopyExpert from './components/copyExpert.vue'
import Original from './components/original.vue'
import Copy from './components/copy.vue'
import OtherMsg from './components/otherMsg.vue'
import history from './components/history.vue'
import QrCode from './components/licenseCode.vue'
import { useApplicationDataStore } from '@/store/applicationData'
import { useTagsViewStore } from '@/store/modules/tagsView'
const router = useRouter()
const route = useRoute()
const tagsViewStore = useTagsViewStore()
const goBack = () => {
  // 关闭当前标签页并返回
  tagsViewStore.delView(route)
  router.back()
}
let licenseId = route.query.id
const licenseCode = route.query.licenseCode
const originalId = route.query.originalId
const duplicateId = route.query.duplicateId
const licenseType = route.query.licenseType
let loading = ref(false)
provide('licenseId', licenseId)
provide('originalId', originalId)
provide('duplicateId', duplicateId)
let title = ref('陕西省大型医用设备在线审批归档系统')
// 基础数据
let basice = reactive({
  title: '西安交通大学第一附属医院',
  code: 'DQ-2024-09-23-0931',
  // 设备名称
  deviceName: 'X线正电子发射断层扫描仪',
  status: ''
})

let typeList = computed(() => {
  switch (licenseType) {
    case '5':
      return [
        {
          label: '正本信息',
          value: 'originalInfo',
          component: markRaw(Original),
          icon: 'ep:checked'
        },
        {
          label: '副本信息',
          value: 'copyInfo',
          component: markRaw(Copy),
          icon: 'ep:document-copy'
        },
        {
          label: '验收材料列表',
          value: 'material',
          component: markRaw(Material),
          icon: 'ep:connection'
        },
        {
          label: '设备验收',
          value: 'copyInspection',
          component: markRaw(CopyExpert),
          icon: 'ep:connection'
        },
        {
          label: '其他信息',
          value: 'otherInfo',
          icon: 'ep:more-filled',
          component: markRaw(OtherMsg)
        },
        {
          label: '操作历史',
          value: 'operationHistory',
          icon: 'lucide:history',
          component: markRaw(history)
        }
      ]
    default:
      return [
        {
          label: '基本信息',
          value: 'basicInfo',
          component: markRaw(BasicInfo),
          icon: 'ep:info-filled'
        },
        {
          label: '业务信息',
          value: 'deviceInfo',
          component: markRaw(Business),
          icon: 'ep:management'
        },
        {
          label: '申请材料列表',
          value: 'useInfo',
          component: markRaw(UseInfo),
          icon: 'ep:connection'
        },
        {
          label: '验收材料列表',
          value: 'material',
          component: markRaw(Material),
          icon: 'ep:connection'
        },
        {
          label: '初步审核',
          value: 'firstAudit',
          component: markRaw(Preliminary),
          icon: 'ep:search'
        },
        {
          label: '专家审核',
          value: 'expertAudit',
          component: markRaw(Expert),
          icon: 'ep:avatar'
        },
        {
          label: '设备验收',
          value: 'copyInspection',
          component: markRaw(CopyExpert),
          icon: 'ep:connection'
        },
        {
          label: '正本信息',
          value: 'originalInfo',
          component: markRaw(Original),
          icon: 'ep:checked'
        },
        {
          label: '副本信息',
          value: 'copyInfo',
          component: markRaw(Copy),
          icon: 'ep:document-copy'
        },
        {
          label: '其他信息',
          value: 'otherInfo',
          icon: 'ep:more-filled',
          component: markRaw(OtherMsg)
        },
        {
          label: '二维码',
          value: 'qrcode',
          component: markRaw(QrCode),
          icon: 'ic:baseline-qrcode'
        },
        {
          label: '操作历史',
          value: 'operationHistory',
          icon: 'lucide:history',
          component: markRaw(history)
        }
      ]
  }
})
let typeActive = ref<{ value: string; component: any }>({
  value: 'basicInfo',
  component: markRaw(BasicInfo)
})
const handlerType = (item) => {
  if (!item.component) return
  typeActive.value.value = item.value
  typeActive.value.component = item.component
}
let applictionStore = useApplicationDataStore()
let allLicenseData = ref({})
const getBasisInfo = async () => {
  try {
    loading.value = true
    let response = await ApplicationApi.basicInfo(licenseId)
    basice.title = response.institutionName
    basice.code = response.appNo
    basice.deviceName = response.licenseDeviceName
    basice.status = response.status

    applictionStore.updateApplicationData(response)
    let response2 = await ApplicationApi.reviewDetail(licenseId)
    applictionStore.updateReviewDetails(response2)

    let responseAll = await Promise.all([
      LicenseApi.getLicenseOriginal({ id: Number(originalId) }),
      LicenseApi.getLicenseCopy({ id: Number(duplicateId) })
    ])
    allLicenseData.value = { ...responseAll[0], ...responseAll[1], code: licenseCode }
    loading.value = false

    // 初始化第一个选中模块
    typeActive.value.value = typeList.value[0].value
    typeActive.value.component = typeList.value[0].component
  } catch (err) {
    loading.value = false
  }
}
onMounted(() => {
  getBasisInfo()
})
</script>

<style lang="scss" scoped>
.license-detail-box {
  .header-row {
    display: flex;
    height: 54px;
    padding: 0 20px;
    background-image: linear-gradient(to right, #282ffc, #1c93f4);
    border-radius: 8px;
    justify-content: space-between;
    align-items: center;

    .left {
      display: flex;
      font-size: 18px;
      color: #fff;
      align-items: center;

      span {
        margin-left: 10px;
      }
    }
    // .right {
    // }
  }

  .content-page {
    height: calc(100% - 54px);
    overflow-y: auto;
    background-color: #f5f7fa;

    .scroll-content {
      padding: 10px 0;
      margin: 0 auto;

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
