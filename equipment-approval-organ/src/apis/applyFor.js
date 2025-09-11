import instance from "@/utils/request.js";

// 创建申请 | 修改
/**
 *
 * @param {
 * id:int,
 * institutionId:'机构ID,dept_id', int
 * applyType:'1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更', int
 * licenseDeviceName:''许可设备名称
 * ladderConfigModel:''阶梯配置机型
 * configReason :'配置理由'
 * } data
 * @returns
 */
export const createApply = (data) => {
  return instance.post("/app-api/biz/application/create", data);
};

// 文件上传
export const createUploadFile = (data) => {
  return instance.post("/app-api/infra/file/upload", data);
};
// 申报材料提交 
/**
 * 
 * @param {
 * applicationId:'申请ID', int
 * materialType:'材料类型', int
 * materialName:'材料名称', string
 * filePath:'文件路径', string
 * fileSize:'文件大小', string
 * } data []
 * @returns 
 */
export const createApplyMaterial = (data) => {
  return instance.post("app-api/biz/appMaterial/insert", data);
}
