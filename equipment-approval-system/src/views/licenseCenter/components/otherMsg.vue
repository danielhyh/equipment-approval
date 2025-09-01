<template>
  <div class="other-msg-page" ref="contentMsgPageRef">
    <div class="title-row">
      <Icon icon="mingcute:group-3-fill" />
      <span>设备使用人员</span>
    </div>
    <!-- 设备使用人员 -->
    <el-table
      :data="devUsePersonData"
      style="width: 100%"
      class="dev-use-person-table"
      header-row-class-name="dev-heade-row"
    >
      <el-table-column
        v-for="item in devUsePersonColumns"
        :key="item.prop"
        :label="item.label"
        :prop="item.prop"
        align="center"
      />
    </el-table>

    <div class="title-row">
      <Icon icon="mdi:address-marker" />
      <span>正本悬挂位置</span>
    </div>
    <!-- 正本悬挂位置 -->
    <License v-bind="licenseProps" ref="licenseRef" />
    <div class="title-row">
      <Icon icon="typcn:chart-line" />
      <span>设备使用情况</span>
    </div>
    <!-- 设备使用情况 -->
    <div class="remark-row"> {{ devuseRemark }}</div>

    <div class="title-row">
      <Icon icon="fa-solid:tools" />
      <span>检查保养情况</span>
    </div>
    <!-- 检查保养情况 -->
    <div class="remark-row"> {{ checkRemark }}</div>
  </div>
</template>

<script setup lang="ts" name="OtherMsg">
import { onMounted, onBeforeUnmount } from 'vue'
import License from '@/views/Processing/components/license.vue'
// 设备使用人员
let devUsePersonColumns = ref([
  { label: '身份证号', prop: 'idCard' },
  { label: '姓名', prop: 'name' },
  { label: '性别', prop: 'gender' },
  { label: '出生日期', prop: 'birthDate' },
  { label: '职称', prop: 'title' },
  { label: '联系电话', prop: 'phone' }
])
let devUsePersonData = ref([
  {
    idCard: '44030219900101001X',
    name: '张三',
    gender: '男',
    birthDate: '1990-01-01',
    title: '工程师',
    phone: '13800000000'
  },
  {
    idCard: '44030219900101002X',
    name: '李四',
    gender: '女',
    birthDate: '1990-01-01',
    title: '工程师',
    phone: '13800000000'
  },
  {
    idCard: '44030219900101003X',
    name: '王五',
    gender: '男',
    birthDate: '1990-01-01',
    title: '工程师',
    phone: '13800000000'
  }
])
// 正本悬挂位置
let licenseProps = ref({})
// 设备使用情况 备注
let devuseRemark = ref('')
// 检查保养情况 备注
let checkRemark = ref('')

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
  window.addEventListener('resize', licenseDomResize)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', licenseDomResize)
})
</script>

<style lang="scss" scoped>
.other-msg-page {
  padding: 16px;
  .title-row {
    display: flex;
    align-items: center;
    padding: 5px 0px;
    border-bottom: 2px solid rgba(22, 93, 255, 0.1);
    font-size: 16px;
    font-weight: 600;
    color: #165dff;
    margin-bottom: 12px;
    &:deep(.el-icon) {
      font-size: 22px !important;
      span {
        font-size: 22px !important;
      }

      margin-right: 6px;
    }
  }
  &:deep(.dev-use-person-table) {
    margin-bottom: 20px;
    .dev-heade-row {
      th.el-table__cell {
        background-image: linear-gradient(135deg, #f8fafc, #f1f5f9);
        .cell {
          color: #333;
        }
      }
    }
  }
  .remark-row {
    padding: 4px 10px;
    background-color: #f8fafc;
    border-radius: 4px;
    border: 1px solid #e2e8f0;
    margin-bottom: 16px;
    min-height: 120px;
    color: #000;
    font-size: 14px;
    line-height: 22px;
  }
}
</style>
