import request from '@/config/axios'

/** 历史数据 */
export interface HistoryData {
  id: string // 主键ID
  licenseNumber?: string // 许可证编号
  institutionName?: string // 配置单位
  licenseDeviceName?: string // 设备名称
  ladderConfigModel?: string // 阶梯配置机型
  region?: string // 所属区域
  originalIssueDate?: string // 正本发证日期
  copyIssueDate?: string // 副本发证日期
  deviceStatus?: string // 设备状态
  originalEntryStatus?: string // 正本录入状态
  copyEntryStatus?: string // 副本录入状态
}

// 历史数据 API
export const HistoryApi = {
  // 查询历史数据分页
  getHistoryPage: async (params: any) => {
    return await request.get({ url: `/biz/history/page`, params })
  },
  // 获取历史数据正本信息
  getHistoryOriginal: async (params: { id: string | number }) => {
    return await request.get({ url: `/biz/history/getOriginal`, params })
  },
  // 获取历史数据副本信息
  getHistoryCopy: async (params: { id: string | number }) => {
    return await request.get({ url: `/biz/history/getDuplicate`, params })
  }
}
