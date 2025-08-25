import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** 专家扩展信息信息 */
export interface ExpertExt {
          id: number; // 主键ID
          userId?: number; // 关联用户ID
          name?: string; // 专家姓名
          gender: number; // 性别：1-男，2-女
          age: number; // 年龄
          unit: string; // 工作单位
          title: string; // 职称
          position: string;
          specialty: string; // 专业领域
          phone: string; // 联系电话
          email: string; // 邮箱
          qualificationCert: string; // 资质证书
          status?: number; // 状态：1-启用，0-禁用
  }

// 专家扩展信息 API
export const ExpertExtApi = {
  // 查询专家扩展信息分页
  getExpertExtPage: async (params: any) => {
    return await request.get({ url: `/biz/expert-ext/page`, params })
  },

  // 查询专家扩展信息详情
  getExpertExt: async (id: number) => {
    return await request.get({ url: `/biz/expert-ext/get?id=` + id })
  },

  // 新增专家扩展信息
  createExpertExt: async (data: ExpertExt) => {
    return await request.post({ url: `/biz/expert-ext/create`, data })
  },

  // 修改专家扩展信息
  updateExpertExt: async (data: ExpertExt) => {
    return await request.put({ url: `/biz/expert-ext/update`, data })
  },

  // 删除专家扩展信息
  deleteExpertExt: async (id: number) => {
    return await request.delete({ url: `/biz/expert-ext/delete?id=` + id })
  },

  /** 批量删除专家扩展信息 */
  deleteExpertExtList: async (ids: number[]) => {
    return await request.delete({ url: `/biz/expert-ext/delete-list?ids=${ids.join(',')}` })
  },

  // 导出专家扩展信息 Excel
  exportExpertExt: async (params) => {
    return await request.download({ url: `/biz/expert-ext/export-excel`, params })
  },
}
