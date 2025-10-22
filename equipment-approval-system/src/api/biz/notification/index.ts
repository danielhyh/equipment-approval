import request from '@/config/axios'

export interface NotificationVO {
  id: string | number
  title: string
  content: string
  publishTime: string
  status: 'published' | 'draft'
  views: number
  creator?: string
  createTime?: string
  updateTime?: string
}

export interface NotificationQuery {
  pageNo?: number
  pageSize?: number
  status?: 'published' | 'draft' | 'all'
  keyword?: string
  startTime?: string
  endTime?: string
}

export interface NotificationForm {
  id?: string | number
  title: string
  content: string
  status: 'published' | 'draft'
}

export const NotificationApi = {
  /**
   * 获取通知公告列表（分页）
   * @param params {pageNo, pageSize, status, keyword}
   * @returns {total: number, list: NotificationVO[]}
   */
  getNotificationList: async (params: NotificationQuery) => {
    return await request.get({ url: `/biz/notification/page`, params })
  },

  /**
   * 获取通知公告详情
   * @param id 通知ID
   * @returns NotificationVO
   */
  getNotificationById: async (id: string | number) => {
    return await request.get({ url: `/biz/notification/get`, params: { id } })
  },

  /**
   * 发布通知公告
   * @param data {title, content, status}
   * @returns 创建的通知ID
   */
  createNotification: async (data: NotificationForm) => {
    return await request.post({ url: `/biz/notification/create`, data })
  },

  /**
   * 更新通知公告
   * @param data {id, title, content, status}
   * @returns boolean
   */
  updateNotification: async (data: NotificationForm) => {
    return await request.post({ url: `/biz/notification/update-content`, data })
  },

  /**
   * 删除通知公告
   * @param id 通知ID
   * @returns boolean
   */
  deleteNotification: async (id: string | number) => {
    return await request.delete({ url: `/biz/notification/delete/${id}` })
  },

  /**
   * 发布草稿
   * @param id 通知ID
   * @returns boolean
   */
  publishDraft: async (data) => {
    return await request.post({ url: `/biz/notification/update-status`, data })
  },

  /**
   * 撤回通知
   * @param id 通知ID
   * @returns boolean
   */
  revokeNotification: async (data) => {
    return await request.put({ url: `/biz/notification/revoke`, data })
  },

  /**
   * 增加浏览量
   * @param id 通知ID
   * @returns boolean
   */
  incrementViews: async (id: string | number) => {
    return await request.put({ url: `/biz/notification/view`, data: { id } })
  },

  /**
   * 获取通知统计信息
   * @returns {total, published, draft, totalViews}
   */
  getNotificationStatistics: async () => {
    return await request.get({ url: `/biz/notification/statistics` })
  }
}
