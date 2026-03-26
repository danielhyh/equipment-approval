package cn.iocoder.yudao.module.biz.controller.admin.atg.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CallbackApplication {

    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer appType;

    @Schema(description = "许可设备名称", example = "李四")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "配置理由", example = "不香")
    private String configReason;

    // 机构 ID
    private Long institutionId = 111111L;

}
