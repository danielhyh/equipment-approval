import request from '@/config/axios'

export const AnalysisApi = {
  // 统计办件数量 {status:1<int> 未审批}
  getApplicationCount: async (params) => {
    return await request.get({ url: `/biz/statistics/application-summary`, params })
  }
}
