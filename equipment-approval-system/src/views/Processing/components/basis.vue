<template>
  <div class="content-basis-msg-page">
    <el-form :model="basicData" label-position="top" inline>
      <el-form-item
        v-for="(item, i) in basicMsg"
        :class="{ 'el-form-item--span-2': item.key === 'address' }"
        :key="i"
        :label="item.label"
        :prop="item.key"
      >
        <el-input
          v-model="item.value"
          placeholder="请输入"
          disabled
          v-if="item.key !== 'address'"
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

<script setup lang="ts" name="Basis">
// 基本信息
import { ref, onMounted } from 'vue'
let props = defineProps({
  list: {
    type: Object || null,
    default: () => {}
  }
})
let basicMsg = ref([
  { label: '申请编号', value: '', key: 'applyNo' },
  { label: '配置单位名称', value: '', key: 'configUnitName' },
  { label: '统一社会信用代码', value: '', key: 'creditCode' },
  { label: '法定代表人', value: '', key: 'legalPerson' },
  { label: '联系人', value: '', key: 'contactPerson' },
  { label: '联系电话', value: '', key: 'contactPhone' },
  { label: '所有制性质', value: '', key: 'ownershipNature' },
  { label: '申请日期', value: '', key: 'applyDate' },
  { label: '详细地址', value: '', key: 'address' }
])
let basicData = ref({})
onMounted(() => {
  if (!!props.list) {
    basicMsg.value.forEach((item) => {
      item.value = props.list[item.key]
      basicData.value[item.key] = props.list[item.key]
    })
  }
})
</script>

<style lang="scss" scoped>
.content-basis-msg-page {
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
