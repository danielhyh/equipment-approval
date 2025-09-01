package cn.iocoder.yudao.module.biz.controller.app.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 申请新增/修改 Request VO")
@Data
public class AppApplicationSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30957")
    private Long id;

    @Schema(description = "机构ID（关联dept_id）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2710")
    private Long institutionId;

    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer appType;

    @Schema(description = "许可设备名称", example = "李四")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "配置理由", example = "不香")
    private String configReason;

}