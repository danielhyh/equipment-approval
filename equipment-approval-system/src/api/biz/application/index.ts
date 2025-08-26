import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** 申请信息 */
export interface Application {
          id: number; // 主键ID
          appNo?: string; // 申请编号
          institutionId?: number; // 机构ID（关联dept_id）
          appType?: number; // 申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更
          licenseDeviceName: string; // 许可设备名称
          ladderConfigModel: string; // 阶梯配置机型
          configReason: string; // 配置理由
          appStatus?: number; // 申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成
          initialReviewResult: number; // 初审结果：1-通过，0-不通过
          initialReviewOpinion: string; // 初审意见
          initialReviewerId: number; // 初审人ID
          initialReviewTime: string | Dayjs; // 初审时间
          expertReviewResult: number; // 专家审核结果：1-通过，0-不通过
          expertReviewOpinion: string; // 专家审核意见
          expertId: number; // 专家ID
          expertReviewTime: string | Dayjs; // 专家审核时间
          licenseNo: string; // 许可证编号
          licenseValidDate: string | Dayjs; // 许可证有效期
  }

// 申请 API
export const ApplicationApi = {
  // 查询申请分页
  getApplicationPage: async (params: any) => {
    return await request.get({ url: `/biz/application/page`, params })
  },

  // 查询申请详情
  getApplication: async (id: number) => {
    return await request.get({ url: `/biz/application/get?id=` + id })
  },

  // 新增申请
  createApplication: async (data: Application) => {
    return await request.post({ url: `/biz/application/create`, data })
  },

  // 修改申请
  updateApplication: async (data: Application) => {
    return await request.put({ url: `/biz/application/update`, data })
  },

  // 删除申请
  deleteApplication: async (id: number) => {
    return await request.delete({ url: `/biz/application/delete?id=` + id })
  },

  /** 批量删除申请 */
  deleteApplicationList: async (ids: number[]) => {
    return await request.delete({ url: `/biz/application/delete-list?ids=${ids.join(',')}` })
  },

  // 导出申请 Excel
  exportApplication: async (params) => {
    return await request.download({ url: `/biz/application/export-excel`, params })
  },
  basicInfo: async (id) => {
    return await request.get({ url: `/biz/application/basicInfo/${id}` })
  },

  bizInfo: async (id) => {
    return await request.get({ url: `/biz/application/bizInfo/${id}` })
  },
  review: async (params) => {
    return await request.post({ url: `/biz/application/approval`, params })
  },
  generateLicense: async(id: number) => {
    return await request.get({ url: `/biz/application/generateLicense/${id}` })
  }
}
