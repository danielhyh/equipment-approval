<template>
  <div class="content-msg-page" ref="contentMsgPageRef">
    <el-form :model="basicData" label-position="top" inline>
      <el-form-item v-for="(item, i) in basicMsg" :key="i" :label="item.label" :prop="item.key">
        <el-input v-model="item.value" placeholder="请输入" disabled />
      </el-form-item>
    </el-form>
    <License v-bind="licenseProps" ref="licenseRef" />
  </div>
</template>

<script setup lang="ts" name="Original">
import License from '@/views/Processing/components/license.vue'
// 基本信息
let props = defineProps({
  list: {
    type: Object || null,
    default: () => {}
  }
})
let basicMsg = ref([
  // 配置单位名称
  { label: '配置单位名称', value: '', key: 'configUnitName' },
  // 统一社会信用代码
  { label: '统一社会信用代码', value: '', key: 'creditCode' },
  // 法定代表人
  { label: '法定代表人', value: '', key: 'legalPerson' },
  // 许可设备名称
  { label: '许可设备名称', value: '', key: 'deviceName' },
  // 所有制性质
  { label: '所有制性质', value: '', key: 'ownershipNature' },
  // 阶梯配置机型
  { label: '阶梯配置机型', value: '', key: 'modelName' },
  // 设备配置地址
  { label: '设备配置地址', value: '', key: 'deviceAddress' },
  // 详细地址
  { label: '详细地址', value: '', key: 'address' },
  // 发证机关
  { label: '发证机关', value: '', key: 'issuingAuthority' },
  // 发证日期
  { label: '发证日期', value: '', key: 'issuingDate' }
])
let basicData = ref({})

let licenseProps = ref({})
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
