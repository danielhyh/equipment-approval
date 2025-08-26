import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** 申请资料信息 */
export interface ApplicationMaterial {
          id: number; // 主键ID
          applicationId?: number; // 申请ID
          materialType?: number; // 资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表
          materialName?: string; // 资料名称
          filePath?: string; // 文件路径
          fileSize: number; // 文件大小(字节)
          uploadTime?: string | Dayjs; // 上传时间
  }

// 申请资料 API
export const ApplicationMaterialApi = {
  // 查询申请资料分页
  getApplicationMaterialPage: async (params: any) => {
    return await request.get({ url: `/biz/application-material/page`, params })
  },

  // 查询申请资料详情
  getApplicationMaterial: async (id: number) => {
    return await request.get({ url: `/biz/application-material/get?id=` + id })
  },

  // 新增申请资料
  createApplicationMaterial: async (data: ApplicationMaterial) => {
    return await request.post({ url: `/biz/application-material/create`, data })
  },

  // 修改申请资料
  updateApplicationMaterial: async (data: ApplicationMaterial) => {
    return await request.put({ url: `/biz/application-material/update`, data })
  },

  // 删除申请资料
  deleteApplicationMaterial: async (id: number) => {
    return await request.delete({ url: `/biz/application-material/delete?id=` + id })
  },

  /** 批量删除申请资料 */
  deleteApplicationMaterialList: async (ids: number[]) => {
    return await request.delete({ url: `/biz/application-material/delete-list?ids=${ids.join(',')}` })
  },

  // 导出申请资料 Excel
  exportApplicationMaterial: async (params) => {
    return await request.download({ url: `/biz/application-material/export-excel`, params })
  },
  list: async (params) => {
    return await request.get({ url: `/biz/application-material/list`, params})
  },
}
