import request from '@/config/axios'

/** 补充设备信息请求 */
export interface AtgCompleteDeviceInfoReq {
  id: number
  licenseDeviceName?: string
  ladderConfigModel?: string
  equipmentConfigAddress?: string
  contactPerson?: string
  contactPhone?: string
}

// 高效通办系统申请 API
export const AtgApplicationApi = {
  // 补充设备信息并生成许可证（一步完成）
  completeAndGenerateLicense: async (data: AtgCompleteDeviceInfoReq) => {
    return await request.post({ url: `/biz/atg-application/complete-and-generate`, data })
  }
}
