type licenceDataType = {
  label: string
  subLabel?: string
  value?: string
}
// 正本
let ALicenceData: licenceDataType[] = [
  {
    label: '配置单位名称',
    value: '国家新能源科技有限公司'
  },
  {
    label: '统一社会信用代码',
    subLabel: '（或组织机构代码）',
    value: '913301067046373179'
  },
  {
    label: '法定代表人',
    subLabel: '（或主要负责人）',
    value: '韩歆毅'
  },
  {
    label: '许可设备名称',
    value: '磁共振成像设备'
  },
  {
    label: '所有制性质',
    value: '国有企业'
  },
  {
    label: '阶梯配置机型',
    value: 'MRI设备'
  },
  {
    label: '设备配置地址',
    value: '北京市海淀区长安路249号-1号楼-2单元-101室'
  }
]
// 副本
let BlicenceData: licenceDataType[] = []
