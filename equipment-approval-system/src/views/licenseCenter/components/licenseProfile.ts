export interface licenseProfileType {
  licenceType: string // 证书类型 A B
  licenceSubtitle: string // 证书副标题 A B
  code?: string | null | undefined // 证书编号
  licenseData?: (string | null | undefined)[] // 证书内容 string  每项数据值
  stampUit?: string | null | undefined // 签发单位
  stampDate?: string | null | undefined // 签发日期
  seal?: string | null | undefined // 印章
}
export interface originalProfile {
  code?: string | null | undefined
  configUnitName?: string | null
  unifiedSocialCreditCode?: string | null
  legalPerson?: string | null
  licenseDeviceName?: string | null
  ownershipNature?: string | null
  ladderConfigModel?: string | null
  equipmentConfigAddress?: string | null
  detailedAddress?: string | null
  issuingAuthority?: string | null
  issueDate?: string | null
}
export interface copyProfile extends originalProfile {
  productionEnterprise?: string | null
  infoSubmitDate?: string | null
  specificModel?: string | null
  productSerialNo?: string | null
  installationDate?: string | null
  remark?: string | null
  duplicateIssuingAuthority?: string | null
  duplicateIssueDate?: string | null
}

// 时间格式化
export const dayTimeFormate = (time) => {
  if (!time) return ''
  return time.replace(/(\d{4})-(\d{2})-(\d{2})/, '$1 年 $2 月 $3 日')
}
