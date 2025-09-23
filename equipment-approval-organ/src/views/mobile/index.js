// 正本信息
const originList = [
  // 证书编号 licenseNo
  { label: "证书编号", key: "licenseNo", value: "", icon: "pajamas:license-sm" },
  // 配置单位 configUnitName
  { label: "配置单位", key: "configUnitName", value: "", icon: "solar:hospital-bold" },
  // 统一社会信用代码 unifiedSocialCreditCode
  { label: "统一社会信用代码", key: "unifiedSocialCreditCode", value: "", icon: "radix-icons:id-card" },
  // 法定代表人 legalPerson
  { label: "法定代表人", key: "legalPerson", value: "", icon: "fontisto:person" },
  // 许可设备名称 licenseDeviceName
  { label: "许可设备名称", key: "licenseDeviceName", value: "", icon: "bi:device-ssd-fill" },
  // 阶梯配置机型 ladderConfigModel
  { label: "阶梯配置机型", key: "ladderConfigModel", value: "", icon: "file-icons:config-coffeescript" },
  // 所有制性质  ownershipNature
  { label: "所有制性质", key: "ownershipNature", value: "", icon: "fa7-solid:layer-group" },
  // 设备配置地址 equipmentConfigAddress
  { label: "设备配置地址", key: "equipmentConfigAddress", value: "", icon: "mdi:address-marker" },
  // 详细地址 detailedAddress
  { label: "详细地址", key: "detailedAddress", value: "", icon: "mdi:address-marker" },
  // 证书有效期 value:长期有效
  { label: "证书有效期", key: "", value: "长期有效", icon: "icon-park-solid:time" },
  // 生成时间 issueDate
  { label: "生成时间", key: "issueDate", value: "", icon: "icon-park-solid:time" },
  // 发证机关 issuingAuthority
  { label: "发证机关", key: "issuingAuthority", value: "", icon: "tdesign:seal-filled" },
];
// 设备 famicons:settings-sharp
// 副本信息
const copyList = [
  // 证书编号 licenseNo
  { label: "证书编号", key: "licenseNo", value: "", icon: "pajamas:license-sm" },
  // 配置单位 configUnitName
  { label: "配置单位", key: "configUnitName", value: "", icon: "solar:hospital-bold" },
  // 生成企业  productionEnterprise
  { label: "生成企业", key: "productionEnterprise", value: "", icon: "streamline:building-2-solid" },
  // 所有制性质 ownershipNature
  { label: "所有制性质", key: "ownershipNature", value: "", icon: "fa7-solid:layer-group" },
  // 法定代表人 legalPerson
  { label: "法定代表人", key: "legalPerson", value: "", icon: "fontisto:person" },
  // 设备型号  specificModel
  { label: "设备型号", key: "specificModel", value: "", icon: "bi:device-ssd-fill" },
  // 产品序列号 productSerialNo
  { label: "产品序列号", key: "productSerialNo", value: "", icon: "ix:machine-c" },
  // 设备配置地址 equipmentConfigAddress
  { label: "设备配置地址", key: "equipmentConfigAddress", value: "", icon: "mdi:address-marker" },
  // 统一社会信用代码 unifiedSocialCreditCode
  { label: "统一社会信用代码", key: "unifiedSocialCreditCode", value: "", icon: "radix-icons:id-card" },
  // 许可设备名称 licenseDeviceName
  { label: "许可设备名称", key: "licenseDeviceName", value: "", icon: "bi:device-ssd-fill" },
  // 阶梯配置机型 ladderConfigModel
  { label: "阶梯配置机型", key: "ladderConfigModel", value: "", icon: "file-icons:config-coffeescript" },
  // 装机时间 installationDate
  { label: "装机时间", key: "installationDate", value: "", icon: "icon-park-solid:time" },
  // 信息报送时间
  { label: "信息报送时间", key: "infoSubmitDate", value: "", icon: "icon-park-solid:time" },
  // 证书有效期 value:长期有效
  { label: "证书有效期", key: "", value: "长期有效", icon: "icon-park-solid:time" },
  // 生成时间 duplicateIssueDate
  { label: "生成时间", key: "duplicateIssueDate", value: "", icon: "icon-park-solid:time" },
  // 发证机关 duplicateIssuingAuthority
  { label: "发证机关", key: "duplicateIssuingAuthority", value: "", icon: "tdesign:seal-filled" },
];
export default { originList, copyList };
