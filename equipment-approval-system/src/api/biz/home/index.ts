import request from '@/config/axios'

// 设备汇总统计
export function getEquipmentSummary(year: number) {
  return request.get({ url: `/biz/statistics/equipment-summary`, params: { year } })
}

// 待办通知 类型数据
export function getTodoType() {
  return request.get({ url: `/biz/notification/notification-summary` })
}
// 待办通知列表
export function getTodoList() {
  return request.get({ url: `/biz/notification/todo-notification` })
}
