<template>
  <div class="other-msg-page" ref="contentMsgPageRef">
    <div class="title-row">
      <Icon icon="mingcute:group-3-fill" />
      <span>设备使用人员</span>
    </div>
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
    <div class="hanging-location">
      <el-image
        v-if="hangingLocation"
        :src="hangingLocation"
        fit="contain"
        preview-teleported
        :preview-src-list="[hangingLocation]"
      />
      <el-empty v-else :image-size="80" />
    </div>

    <div class="title-row">
      <Icon icon="typcn:chart-line" />
      <span>设备使用情况</span>
    </div>
    <div class="remark-row">{{ devuseRemark }}</div>

    <div class="title-row">
      <Icon icon="fa-solid:tools" />
      <span>检查保养情况</span>
    </div>
    <div class="remark-row">{{ checkRemark }}</div>
  </div>
</template>

<script setup lang="ts" name="OtherMsg">
const props = defineProps({
  list: {
    type: Object,
    default: () => ({})
  },
  supplementaryList: {
    type: Array,
    default: () => []
  }
})

let devUsePersonColumns = ref([
  { label: '身份证号', prop: 'idCard' },
  { label: '姓名', prop: 'name' },
  { label: '性别', prop: 'gender' },
  { label: '出生日期', prop: 'birthDate' },
  { label: '职称', prop: 'title' },
  { label: '联系电话', prop: 'phone' }
])

const parseJsonList = (value: any) => {
  if (Array.isArray(value)) return value
  if (!value || typeof value !== 'string') return []
  try {
    const parsed = JSON.parse(value)
    return Array.isArray(parsed) ? parsed : []
  } catch {
    return []
  }
}

const getTimeValue = (item: any) => {
  const value = item?.createTime || item?.submitTime || ''
  const time = new Date(value).getTime()
  return Number.isNaN(time) ? 0 : time
}

const latestInfo = (infoType: number) => {
  return [...(props.supplementaryList as any[])]
    .filter((item) => item?.infoType === infoType)
    .sort((a, b) => getTimeValue(b) - getTimeValue(a) || (Number(b?.id) || 0) - (Number(a?.id) || 0))[0]
}

const formatGender = (value: any) => {
  if (value === '1') return '男'
  if (value === '2') return '女'
  return value || ''
}

const normalizeUsers = (users: any[]) => {
  return users.map((item) => ({
    idCard: item?.idCard || item?.IdCard || '',
    name: item?.name || '',
    gender: formatGender(item?.gender),
    birthDate: item?.birthDate || '',
    title: item?.title || item?.careerTitle || '',
    phone: item?.phone || item?.phoneNumber || item?.contactPhone || ''
  }))
}

let devUsePersonData = computed(() => {
  const supplementaryUsers = parseJsonList(latestInfo(4)?.infoContent)
  const users = supplementaryUsers.length ? supplementaryUsers : parseJsonList((props.list as any)?.equipmentUsers)
  return normalizeUsers(users)
})

let hangingLocation = computed(() => {
  const info = latestInfo(1)
  return info?.filePath || info?.infoContent || (props.list as any)?.originalPosition || ''
})

let devuseRemark = computed(() => latestInfo(2)?.infoContent || (props.list as any)?.remark || '')
let checkRemark = computed(() => latestInfo(3)?.infoContent || (props.list as any)?.specialDescription || '')

let contentMsgPageRef = ref<HTMLDivElement | null>(null)
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
  .hanging-location {
    min-height: 120px;
    margin-bottom: 16px;
    padding: 10px;
    background-color: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 4px;
    :deep(.el-image) {
      width: 100%;
      max-height: 420px;
      display: block;
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
    white-space: pre-wrap;
  }
}
</style>
