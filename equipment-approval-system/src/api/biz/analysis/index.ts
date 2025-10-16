import request from '@/config/axios'

export const AnalysisApi = {
  // 统计办件数量 {status:1<int> 未审批}
  getApplicationCount: async (params) => {
    return await request.get({ url: `/biz/statistics/application-summary`, params })
  },
  // ----------------------------------------------------------------------------------------
  // 统计概览 ----------
  /**
   * 办件统计汇总
   * @params {region:'',year:int }
   */
  getApplicationSummary: async (params) => {
    return await request.get({ url: `/biz/statistics/application-summary`, params })
  },
  /**
   * 许可证统计汇总
   * @params {region:'',year:int}
   */
  getLicenseSummary: async (params) => {
    return await request.get({ url: `/biz/statistics/license-summary`, params })
  },
  /**
   * 历史数据统计汇总
   * @params {year:int}
   */
  getHistorySummary: async (params) => {
    return await request.get({ url: `/biz/statistics/history-summary`, params })
  },
  /**
   * 专家统计汇总
   * @params {region:'',year:int}
   */
  getExpertSummary: async () => {
    return await request.get({ url: `/biz/statistics/expert-summary` })
  },
  /**
   * 公告统计汇总
   * @params {region:'',year:int}
   */
  getNoticeSummary: async (params) => {
    return await request.get({ url: `/biz/statistics/notice-summary`, params })
  },
  /**
   * 设备生产企业汇总
   * @params {region:'',year:int}
   */
  getEquipmentCompanySummary: async (params) => {
    return await request.get({
      url: `/biz/statistics/equipment-manufacturer-summary`,
      params
    })
  },
  /**
   * 医疗机构汇总
   * @params {region:'',year:int}
   */
  getHealthcareSummary: async (params) => {
    return await request.get({
      url: `/biz/statistics/medical-institution-summary`,
      params
    })
  },

  // 设备拥有量统计 -----------------------------------------
  /**
   *  设备拥有量-区域分布情况(覆盖8个地市)
   */
  getRegionDevice: async () => {
    return await request.get({ url: `/biz/statistics/equipment-statistics-area` })
  },
  /**
   * 设备详细信息
   * @params {pageNo:int,pageSize:int,keywords:string,year:int,region:string,ladderConfigModel:string,status:证书状态，deviceTypes:[]}
   * @returns {total:int,list:[]}
   */
  getDeviceDetail: async (params) => {
    return await request.get({
      url: `/biz/statistics/equipment-statistics-detail`,
      params
    })
  },
  /**
   * 年度递增分量及总量
   */
  getAnnualAmount: async () => {
    return await request.get({
      url: `/biz/statistics/annual-incremental`
    })
  },
  /**
   * 阶梯分布配置
   */
  getLadderConfig: async () => {
    return await request.get({
      url: `/biz/statistics/ladder-config-distribution`
    })
  },
  /**
   * 配置分布情况
   */
  getConfigDistribution: async () => {
    return await request.get({
      url: `/biz/statistics/medical-device-distribution`
    })
  }
}
