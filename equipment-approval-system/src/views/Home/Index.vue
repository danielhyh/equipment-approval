<template>
  <div>
    <el-card class="card-box">
      <!-- 头部区域 -->
      <template #header>
        <div class="header-section">
          <h2 class="page-title">
            <Icon icon="fa-solid:chart-pie" :size="18" color="#165DFF" style="margin-right: 5px" />
            <span>设备汇总统计</span>
          </h2>
          <el-select v-model="selectedYear" class="year-selector" @change="getEquipmentSummaryData">
            <el-option
              v-for="item in selectYearList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </div>
      </template>
      <!-- 统计卡片区域 -->
      <div class="stats-cards-container">
        <!-- 设备总数卡片 -->
        <div class="total-equipment-card">
          <div class="card-content">
            <div class="total-count">{{ totalEquipment }}</div>
            <div class="count-label">设备总数量</div>
            <div class="growth-rate">同比增长: 18.3%</div>
          </div>
          <div class="logo">
            <Icon icon="fa-solid:procedures" :size="40" color="#fff" />
          </div>
        </div>

        <!-- 甲类大型医用设备卡片 -->
        <div class="equipment-type-card">
          <div class="card-header">
            <h3 class="card-title c-ef4444">甲类大型医用设备</h3>
          </div>
          <div class="total-type-count c-ef4444">{{ equipmentATotal }} 台</div>
          <div class="equipment-list">
            <div class="equipment-item" v-for="item in equipmentA" :key="item.key">
              <span class="equipment-name">{{ item.label }}</span>
              <span class="equipment-count c-ef4444">{{ item.value }}</span>
            </div>
          </div>
        </div>

        <!-- 乙类大型医用设备卡片 -->
        <div class="equipment-type-card">
          <div class="card-header">
            <h3 class="card-title c-06b6d4">乙类大型医用设备</h3>
          </div>
          <div class="total-type-count c-06b6d4">{{ equipmentBTotal }} 台</div>
          <div class="equipment-list">
            <div class="equipment-item" v-for="item in equipmentB" :key="item.key">
              <span class="equipment-name">{{ item.label }}</span>
              <span class="equipment-count c-ef4444">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 代办通知 -->
    <el-card class="card-box">
      <template #header>
        <div class="header-section">
          <h2 class="page-title">
            <Icon icon="tabler:bell-filled" :size="18" color="#165DFF" style="margin-right: 5px" />
            <span>待办通知</span>
          </h2>
          <div class="todo-tabs">
            <!-- 修改为仅展示的标签组 -->
            <div class="todo-type-badge-group">
              <div class="todo-type-badge" v-for="item in todoType" :key="item.key">
                <Icon :icon="item.icon" :size="18" color="#fff" style="margin-right: 5px" />
                <span>{{ item.name }}： {{ item.value || '--' }}</span>
              </div>
            </div>
          </div>
        </div>
      </template>

      <div class="todo-list-box" :class="{ empty: todoList.length === 0 }">
        <template v-if="todoList.length > 0">
          <div
            class="todo-item"
            :class="getTodoItemClass(item.status)"
            v-for="item in todoList"
            :key="item.id"
          >
            <!-- 顶部信息：医院名称和日期 -->
            <div class="todo-header">
              <!-- 状态标签 -->
              <div class="todo-tags">
                <span :class="getTodoTagClass(item.status)">
                  <Icon :icon="getTodoIcon(item.type)" :size="14" style="margin-right: 5px" />
                  {{ item.statusText }}
                </span>
              </div>

              <div class="todo-date">{{ item.date }}</div>
            </div>
            <div class="hospital-name">{{ item.hospitalName }}</div>
            <!-- 设备信息 -->
            <div class="equipment-info">{{ item.equipmentInfo }}</div>

            <!-- 截止信息或其他备注 -->
            <div class="todo-remark" v-if="item.remainingTime || item.reason">
              {{ item.remainingTime || item.reason }}
            </div>

            <!-- 底部操作按钮 -->
            <div class="todo-actions">
              <el-button type="primary" size="small" class="handle-btn" @click.stop="jumpTo(item)">
                立即办理
              </el-button>
            </div>
          </div>
        </template>
        <el-empty v-else :image-size="130" description="暂无待办通知" />
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { getEquipmentSummary, getTodoType, getTodoList } from '@/api/biz/home/index'

const router = useRouter()
// 年份选择器的响应式数据
const selectedYear = ref('2024')
interface SelectYearItem {
  label: string
  value: string
}
const selectYearList = computed<SelectYearItem[]>(() => {
  let currentYear = new Date().getFullYear()
  let arr: SelectYearItem[] = []
  for (let i = 0; i < 4; i++) {
    arr.push({
      label: currentYear - i + '年',
      value: String(currentYear - i)
    })
  }
  arr.push({
    label: '全部',
    value: 'all'
  })
  return arr
})

interface EquipmentItem {
  label: string
  value: number | string
  key?: string
}
// 甲类大型医疗设备 数据
let equipmentA = ref<EquipmentItem[]>([
  {
    label: '重离子质子放射治疗系统',
    value: 12,
    key: 'a1'
  },
  {
    label: '高端放射治疗设备',
    value: 28,
    key: 'a2'
  },
  {
    label: '曾强型体外冲击波碎石机',
    value: 49,
    key: 'a3'
  }
])
let equipmentATotal = computed<number>(() => {
  return equipmentA.value.reduce((pre, cur) => pre + Number(cur.value), 0)
})
// 乙类大型医疗设备 数据
let equipmentB = ref<EquipmentItem[]>([
  {
    label: '医用电子直线加速器(含后装)',
    value: 45,
    key: 'b1'
  },
  {
    label: 'X线电子计算机断层扫描装置',
    value: 67,
    key: 'b2'
  },
  {
    label: '核医学治疗系统',
    value: 38,
    key: 'b3'
  },
  {
    label: '伽玛射线立体定向治疗设备',
    value: 52,
    key: 'b4'
  },
  {
    label: '高强度聚焦超声治疗系统',
    value: 45,
    key: 'b5'
  }
])
let equipmentBTotal = computed<number>(() => {
  return equipmentB.value.reduce((pre, cur) => pre + Number(cur.value), 0)
})
// 设备总数量
let totalEquipment = computed<number>(() => {
  return equipmentATotal.value + equipmentBTotal.value
})

const getEquipmentSummaryData = async () => {
  try {
    const res = await getEquipmentSummary(Number(selectedYear.value))
    console.log(res)
  } catch (e) {
    console.log(e)
  }
}
getEquipmentSummaryData()
// 待办通知相关代码
// 待办事项类型定义 - 重新定义数据结构
interface TodoItem {
  id: string
  hospitalName: string
  date: string
  equipmentInfo: string
  type: 'certApply' | 'certReissue' | 'certChange' | 'locationChange'
  status: 'primary' | 'success' | 'warning' | 'danger'
  statusText: string
  appType?: string
  remainingTime?: string
  reason?: string
}
interface TodoType {
  icon: string
  bColor?: string
  name: string
  key: string
  value: string
}
// 待办类型数据
let todoType = reactive<TodoType[]>([
  { icon: 'zondicons:add-solid', name: '证书申请', key: 'apply', value: '' },
  { icon: 'fa-solid:redo', name: '证书补办', key: 'renew', value: '' },
  { icon: 'mingcute:edit-4-fill', name: '证书变更', key: 'change', value: '' },
  { icon: 'bxs:message-edit', name: '信息变更', key: 'infoChange', value: '' },
  { icon: 'uiw:tag', name: '总计', key: 'total', value: '' }
])
const getTodoTypeData = async () => {
  try {
    const res = await getTodoType()
    console.log(res)
  } catch (e) {
    console.log(e)
  }
}
getTodoTypeData()
// 待办事项列表数据
let todoList = ref<TodoItem[]>([
  // {
  //   id: 'todo1',
  //   hospitalName: '西安交通大学第一附属医院',
  //   date: '2024-12-20',
  //   equipmentInfo: '医用电子加速器增配申请（临床试用）',
  //   status: 'primary',
  //   statusText: '证书申请',
  //   remainingTime: '距离办理截止时间还剩3天',
  //   type: 'certApply'
  // }
])
const getTodoListData = async () => {
  try {
    const res = await getTodoList()
    todoList.value = res.data.map((item, index) => ({
      id: `todo_${index}`,
      hospitalName: item.title,
      date: item.publishTime || 'xxxx-xx-xx',
      equipmentInfo: item.content || '--',
      appType: item.appType,
      // remainingTime: '距离办理截止时间还剩3天',
      ...getTodoTypeStatus(item.appType)
    }))
  } catch (e) {
    console.log(e)
  }
}
getTodoListData()
// 跳转到办件中心
const jumpTo = (item) => {
  router.push({
    path: '/processing',
    query: {
      type: item.appType
    }
  })
}
// 根据数据返回状态、类型
const getTodoTypeStatus = (
  appType: string
): { status: string; type: string; statusText: string } => {
  switch (appType) {
    case '1':
      return { status: 'primary', type: 'certApply', statusText: '证书申请' }
    case '2':
      return { status: 'success', type: 'certReissue', statusText: '证书补办' }
    case '3':
      return { status: 'warning', type: 'certChange', statusText: '证书变更' }
    case '4':
      return { status: 'danger', type: 'locationChange', statusText: '信息变更' }
    default:
      return { status: 'primary', type: 'certApply', statusText: '证书申请' }
  }
}
// 根据状态获取标签样式类
const getTodoTagClass = (status: string) => {
  const tagClassMap: Record<string, string> = {
    primary: 'el-tag--primary',
    success: 'el-tag--success',
    warning: 'el-tag--warning',
    danger: 'el-tag--danger'
  }
  return `el-tag ${tagClassMap[status] || ''}`
}
// 根据状态获取卡片边框样式类
const getTodoItemClass = (status: string) => {
  const borderClassMap: Record<string, string> = {
    primary: 'todo-item-primary',
    success: 'todo-item-success',
    warning: 'todo-item-warning',
    danger: 'todo-item-danger'
  }
  return borderClassMap[status] || ''
}
// 根据状态配置icon
const getTodoIcon = (type: string) => {
  const iconMap: Record<string, string> = {
    certApply: 'zondicons:add-solid',
    certReissue: 'fa-solid:redo',
    certChange: 'mingcute:edit-4-fill',
    infoChange: 'bxs:message-edit',
    total: 'uiw:tag'
  }
  return iconMap[type] || 'uiw:tag'
}
</script>

<style lang="scss" scoped>
@use './index.scss';
</style>
