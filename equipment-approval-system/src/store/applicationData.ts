import { defineStore } from 'pinia'

export const useApplicationDataStore = defineStore('appDataStore', {
  state: () => ({
    basicInfo: {
      id: null,
      appNo: '',
      institutionName: '',
      licenseDeviceName: '',
      unifiedSocialCreditCode: '',
      legalPerson: '',
      contactPerson: '',
      contactPhone: '',
      ownershipNature: '',
      createTime: null,
      detailedAddress: ''
    },
    reviewDetails: {
      initialReviewResult: '', //初审结果 1 通过 ，0 不通过 string
      initialReviewOpinion: '', //初审意见
      expertReviewResult: '', // 专家审核结果 1 通过 ，0 不通过 string
      expertReviewOpinion: '', // 专家审核意见
      expertId: '', // 专家ID
      expertAttachments: '', //专家审核附件
      licenseNo: '', // 许可证编号
      licenseGenerateDate: '' // 许可证生成日期
    }
  }),
  getters: {
    getApplicationData: (state) => state.basicInfo,
    getReviewDetails: (state) => state.reviewDetails
  },
  actions: {
    updateApplicationData(newData: Partial<typeof this.basicInfo>) {
      this.basicInfo = { ...this.basicInfo, ...newData }
    },
    updateReviewDetails(newData: Partial<typeof this.reviewDetails>) {
      if (!newData) {
        Object.keys(this.reviewDetails).forEach((key) => {
          this.reviewDetails[key] = ''
        })
        return
      }
      this.reviewDetails = Object.assign(this.reviewDetails, newData)
    }
  }
})
