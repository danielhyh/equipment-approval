package cn.iocoder.yudao.module.biz.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode APPLICATION_NOT_EXISTS = new ErrorCode(1233, "申请不存在");

    ErrorCode APPLICATION_MATERIAL_NOT_EXISTS = new ErrorCode(1234, "申请资料不存在");

    ErrorCode EXPERT_EXT_NOT_EXISTS = new ErrorCode(1235, "专家扩展信息不存在");
}
