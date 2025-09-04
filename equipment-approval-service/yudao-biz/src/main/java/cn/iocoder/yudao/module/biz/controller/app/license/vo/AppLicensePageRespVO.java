package cn.iocoder.yudao.module.biz.controller.app.license.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "用户 APP - 许可证分页响应  VO")
@Data
public class AppLicensePageRespVO {

    @Schema(description = "申请id")
    private Long applicationId;

    @Schema(description = "正本id")
    private Long originalId;

    @Schema(description = "副本id")
    private Long duplicateId;

    @Schema(description = "许可证编号")
    private String licenseNo;

    @Schema(description = "许可设备地址")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "设备配置地址")
    private String equipmentConfigAddress;

    @Schema(description = "发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @Schema(description = "生产企业")
    private String productionEnterprise;

    @Schema(description = "具体型号")
    private String specificModel;

    @Schema(description = "装机日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate installationDate;

}
