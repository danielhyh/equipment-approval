<template>
  <div class="content-msg-page" ref="contentMsgPageRef">
    <el-form :model="basicData" label-position="top" inline>
      <el-form-item v-for="(item, i) in basicMsg" :key="i" :label="item.label" :prop="item.key">
        <el-input v-model="basicData[item.key]" placeholder="请输入" disabled />
      </el-form-item>
    </el-form>
    <License v-bind="licenseProps" ref="licenseRef" />
  </div>
</template>

<script setup lang="ts" name="Copy">
import License from '@/views/Processing/components/license.vue'
import type { licenseProfileType, copyProfile } from './licenseProfile'
import { dayTimeFormate } from './licenseProfile'
// 基本信息
let props = defineProps({
  list: {
    type: Object || null,
    default: () => {}
  }
})
let basicMsg = ref([
  // 生产企业
  { label: '生产企业', value: '', key: 'productionEnterprise' },
  // 具体型号
  { label: '具体型号', value: '', key: 'specificModel' },
  // 产品序列号
  { label: '产品序列号', value: '', key: 'productSerialNo' },
  // 装机日期
  { label: '装机日期', value: '', key: 'installationDate' },
  // 信息报送日期
  { label: '信息报送日期', value: '', key: 'infoSubmitDate' },
  // 副本发证机关
  { label: '副本发证机关', value: '', key: 'duplicateIssuingAuthority' },
  // 副本发证日期
  { label: '副本发证日期', value: '', key: 'duplicateIssueDate' },
  // 备注
  { label: '备注信息', value: '', key: 'remark' }
])
let basicData = ref({}) // 基本信息

let licenseProps = ref<licenseProfileType>({
  licenceType: 'B',
  licenceSubtitle: 'B'
})
const formateDialogLicense = (data: copyProfile) => {
  let arr: any[] = []
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
  licenseProps.value.code = data.code
  licenseProps.value.stampUit = data.duplicateIssuingAuthority
  licenseProps.value.stampDate = data.duplicateIssueDate
}
let contentMsgPageRef = ref<HTMLDivElement | null>(null)
let licenseRef = ref<InstanceType<typeof License> | null>(null)
const licenseDomResize = () => {
  if (licenseRef.value && contentMsgPageRef.value) {
    let radio = contentMsgPageRef.value.clientWidth / licenseRef.value.$el.clientWidth
    if (radio > 1) radio = 1
    licenseRef.value.$el.style.transform = `scale(${radio})`
    licenseRef.value.$el.style.transformOrigin = 'top left'
  }
}

onMounted(() => {
  if (!!props.list) {
    basicMsg.value.forEach((item) => {
      item.value = props.list[item.key]
      basicData.value[item.key] = props.list[item.key]
    })
    formateDialogLicense(props.list)
  }

  window.addEventListener('resize', licenseDomResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', licenseDomResize)
})
</script>

<style lang="scss" scoped>
.content-msg-page {
  padding: 20px;
  overflow: auto;
  background-color: #fff;
  &:deep(.el-form) {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-gap: 20px;
    margin-bottom: 10px;
    .el-form-item {
      margin: 0;
      .el-input,
      .el-textarea {
        .el-input__inner,
        .el-textarea__inner {
          --el-disabled-text-color: #000;
        }
        .el-input__inner::placeholder {
          -webkit-text-fill-color: #999;
        }
      }
      .el-form-item__label {
        font-weight: bold;
      }
    }
    .el-form-item--span-2 {
      grid-column: 1/3;
    }
  }
}
</style>
