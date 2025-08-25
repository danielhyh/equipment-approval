import { defineStore } from 'pinia'


export const useApplicationDataStore = defineStore("appDataStore", {
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
      }
    }),
    actions: {
      updateApplicationData(newData: Partial<typeof this.basicInfo>) {
        this.basicInfo = { ...this.basicInfo, ...newData }
      }
    },
})
