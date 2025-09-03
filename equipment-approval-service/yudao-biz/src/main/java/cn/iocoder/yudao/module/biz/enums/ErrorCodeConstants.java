package cn.iocoder.yudao.module.biz.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode APPLICATION_NOT_EXISTS = new ErrorCode(1233, "申请不存在");

    ErrorCode APPLICATION_MATERIAL_NOT_EXISTS = new ErrorCode(1234, "申请资料不存在");

    ErrorCode EXPERT_EXT_NOT_EXISTS = new ErrorCode(1235, "专家扩展信息不存在");

    ErrorCode EQUIPMENT_MANUFACTURER_NOT_EXISTS = new ErrorCode(1236, "设备生产企业不存在");

    ErrorCode INSTITUTION_EXT_NOT_EXISTS = new ErrorCode(1237, "机构扩展信息不存在");

    ErrorCode CLASS_A_EQUIPMENT_NOT_EXISTS = new ErrorCode(1238, "设备不存在");

    ErrorCode SUPPLEMENTARY_INFO_NOT_EXISTS = new ErrorCode(1239, "补充信息不存在");

    ErrorCode ACCEPTANCE_MATERIAL_NOT_EXISTS = new ErrorCode(1240, "验收资料不存在");
}
