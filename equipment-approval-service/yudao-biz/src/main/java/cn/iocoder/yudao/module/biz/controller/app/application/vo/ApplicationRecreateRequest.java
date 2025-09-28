package cn.iocoder.yudao.module.biz.controller.app.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplicationRecreateRequest {

    @Schema(description = "要变更或补办的appId")
    @NotBlank(message = "appId不能为空")
    private Long appId;

    @Schema(description = "2补办3变更")
    @NotBlank(message = "申请类型不能为空")
    private Integer appType;

    @Schema(description = "许可设备名称")
    @NotBlank(message = "许可设备名称不能为空")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    @NotBlank(message = "阶梯配置机型不能为空")
    private String ladderConfigModel;
}
