<template>
  <div class="content-business-msg-page">
    <el-form :model="basicData" label-position="top" inline>
      <el-form-item
        v-for="(item, i) in basicMsg"
        :class="{ 'el-form-item--span-2': item.key === 'specialInstructions' }"
        :key="i"
        :label="item.label"
        :prop="item.key"
      >
        <el-input
          v-model="item.value"
          placeholder="请输入"
          disabled
          v-if="item.key !== 'specialInstructions'"
        />
        <el-input
          v-model="item.value"
          :rows="3"
          type="textarea"
          placeholder="请输入"
          disabled
          v-else
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts" name="Business">
// 业务信息
import { onMounted, ref } from 'vue'
import { ApplicationApi } from '@/api/biz/application'
import { useRoute } from 'vue-router'

const route = useRoute()
const { id } = route.query

let props = defineProps({
  list: {
    type: Object || null,
    default: () => {}
  }
})
let basicMsg = ref([
  { label: '许可设备名称', key: 'licenseDeviceName', value: '' },
  { label: '阶梯配置机型', key: 'ladderConfigModel', value: '' },
  { label: '设备配置地址', key: 'equipmentConfigAddress', value: '' },
  { label: '生产企业', key: 'productionEnterprise', value: '' },
  { label: '具体型号', key: 'specificModel', value: '' },
  { label: '产品序列号', key: 'serialNumber', value: '' },
  { label: '装机日期', key: 'installationDate', value: '' },
  { label: '采购价格', key: 'purchasePrice', value: '' },
  { label: '设备特殊说明', key: 'specialDescription', value: '' }
])
let basicData = ref({})

const getBizInfo = async () => {
  var res = await ApplicationApi.bizInfo(id)
  basicData.value = res
  basicMsg.value.forEach((item) => {
    item.value = res[item.key]
  })
}
onMounted(
  getBizInfo(),
  () => {
  // if (!!props.list) {
  //   basicMsg.value.forEach((item) => {
  //     item.value = props.list[item.key]
  //     basicData.value[item.key] = props.list[item.key]
  //   })
  // }
})
</script>

<style lang="scss" scoped>
.content-business-msg-page {
  padding: 20px;
  background-color: #fff;
  &:deep(.el-form) {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-gap: 20px;
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
