import request from '@/config/axios'

// 获取甲类设备分页
export function getEquipmentPage(params: any) {
  return request.get({
    url: '/biz/class-A-equipment/page',
    params
  })
}
// 创建甲类设备
export function createEquipment(params: any) {
  return request.post({
    url: '/biz/class-A-equipment/create',
    data: params
  })
}
// 编辑甲类设备
export function editEquipment(params: any) {
  return request.put({
    url: '/biz/class-A-equipment/update',
    data: params
  })
}
// 获取甲类设备详情
export function getEquipmentDetail(id: string) {
  return request.get({
    url: '/biz/class-A-equipment/get',
    params: { id }
  })
}
// 删除甲类设备
export function deleteEquipment(id) {
  return request.delete({
    url: '/biz/class-A-equipment/delete',
    params: { id }
  })
}

//获取设备生产企业列表
export function getproductionCompanyList(params) {
  return request.get({
    url: '/biz/equipment-manufacturer/page',
    params
  })
}

//创建设备生产企业
export function createproductionCompany(params) {
  return request.post({
    url: '/biz/equipment-manufacturer/create',
    data: params
  })
}
// 编辑设备生产企业
export function editproductionCompany(params) {
  return request.put({
    url: '/biz/equipment-manufacturer/update',
    data: params
  })
}
// 获取设备生产企业详情
export function getproductionCompanyDetail(id: string) {
  return request.get({
    url: '/biz/equipment-manufacturer/get',
    params: { id }
  })
}
// 删除设备生产企业
export function deleteproductionCompany(id: string) {
  return request.delete({
    url: '/biz/equipment-manufacturer/delete',
    params: { id }
  })
}

// 获取医疗机构列表
export function getHospitalList(params) {
  return request.get({
    url: '/biz/institution-ext/page',
    params
  })
}
// 创建医疗机构
export function createHospital(params) {
  return request.post({
    url: '/biz/institution-ext/create',
    data: params
  })
}
// 编辑医疗机构
export function editHospital(params) {
  return request.put({
    url: '/biz/institution-ext/update',
    data: params
  })
}
// 删除医疗机构
export function deleteHospital(id: string) {
  return request.delete({
    url: '/biz/institution-ext/delete',
    params: { id }
  })
}

// 获取医疗机构详情
export function getHospitalDetail(id: string) {
  return request.get({
    url: '/biz/institution-ext/get',
    params: { id }
  })
}
