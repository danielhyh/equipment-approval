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
        <el-select
          v-model="basicData[item.key]"
          placeholder="请选择"
          v-if="item.key === 'institutionType'"
          disabled
        >
          <el-option
            v-for="option in institutionTypeOptions"
            :key="option.value"
            :label="option.label"
            :value="Number(option.value)"
          />
        </el-select>
        <el-input v-model="basicData[item.key]" placeholder="请输入" disabled v-else />
        <!-- <el-input
          v-model="basicData[item.key]"
          :rows="3"
          type="textarea"
          placeholder="请输入"
          disabled
        /> -->
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts" name="BasisTwo">
import { getDictOptions } from '@/utils/dict'
import type { DictDataType } from '@/utils/dict'
// 基本信息
import { useApplicationDataStore } from '@/store/applicationData'
const appData = useApplicationDataStore()
let props = defineProps({
  list: {
    type: Object || null,
    default: () => {}
  }
})
interface DictDataTypeT extends DictDataType {
  value: string | number
}
let institutionTypeOptions = computed<DictDataTypeT[]>(() => getDictOptions('biz_institution_type'))
let basicMsg = ref([
  { label: '申请编号', value: '', key: 'appNo' },
  { label: '机构名称', value: '', key: 'institutionName' },
  { label: '统一社会信用代码', value: '', key: 'unifiedSocialCreditCode' },
  { label: '法定代表人', value: '', key: 'legalPerson' },
  { label: '联系人', value: '', key: 'contactPerson' },
  { label: '联系电话', value: '', key: 'contactPhone' },
  { label: '所有制性质', value: '', key: 'ownershipNature' },
  { label: '申请日期', value: '', key: 'createTime' },
  { label: '注册地址', value: '', key: 'detailedAddress' },
  { label: '机构性质', value: '', key: 'institutionType' },
  { label: '上级机构', value: '', key: 'superiorInstitution' },
  { label: '卫生机构级别', value: '', key: 'institutionLevel' },
  { label: '所属区域', value: '', key: 'region' }
])
let basicData = ref({})
watch(
  () => appData.basicInfo,
  (newVal) => {
    if (!newVal) return
    basicMsg.value.forEach((item) => {
      item.value = appData.basicInfo[item.key]
      basicData.value[item.key] = appData.basicInfo[item.key]
    })
  },
  {
    immediate: true,
    deep: true
  }
)
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
      .el-select__selected-item {
        color: #000;
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
