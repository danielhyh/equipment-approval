import request from '@/config/axios'

interface pagePamamsType {
  pageSize: number
  pageNum: number
  licenseType?: string // 许可证类型
  deviceType?: string // 设备类型
  ladderConfigModel?: string // 梯形配置模型
  region?: string // 区域
  keywords?: string // 搜索关键词
}
export const LicenseApi = {
  /* 许可证中心分页查询 */
  getLicensePage: async (params: pagePamamsType) => {
    return await request.get({ url: `/biz/license/page`, params })
  },
  /* 许可证中心正本查询 */
  getLicenseOriginal: async (params: { id: number }) => {
    return await request.get({ url: `/biz/license/getOriginalById`, params })
  },
  /* 许可证中心副本查询 */
  getLicenseCopy: async (params: { id: number }) => {
    return await request.get({ url: `/biz/license/getDuplicateById`, params })
  },
  // 新增线下许可证
  addOfflineLicense: async (data: any) => {
    return await request.post({ url: `/biz/license/offline-process`, data })
  },
  // 获取线下许可证详情 {oid:'',did:''}
  getOfflineLicense: async (params: any) => {
    return await request.get({ url: '/biz/license/get-license', params })
  },
  // 获取配置单位
  getConfigUnitList: async () => {
    return await request.get({ url: `/biz/institution-ext/list` })
  },
  // 针对副本进行审核回显 {id:副本id}
  auditCopyLicenseDetail: async (params: any) => {
    return await request.get({ url: `/biz/license/approval-details`, params })
  },
  // 针对副本进行审核 {id:副本id,reviewResult:审核结果,reviewOpinion:审核意见,expertIds:专家id列表——逗号隔开的字符串,expertAttachments:专家审核附件path 逗号分隔}
  auditCopyLicense: async (data: any) => {
    return await request.post({ url: `/biz/license/approval`, data })
  },
  // 历史记录
  getLicenseHistory: async (params: { id: number }) => {
    return await request.get({ url: `/biz/log/list`, params })
  },
  // 获取补充信息列表
  getSupplementaryInfoList: async (params: { applicationId: number }) => {
    return await request.get({ url: `/biz/supplementary-info/list`, params })
  },
  // 获取许可证统计
  getLicenseStatistics: async () => {
    return await request.get({ url: `/biz/statistics/application-summary`, status: 5 })
  }
}
