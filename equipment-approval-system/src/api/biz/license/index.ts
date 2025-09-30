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
  }
}
