package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "管理后台 - 许可证中心分页响应对象 VO")
public class LicensePageVO {

    @Schema(description = "正本id")
    private Long originalId;

    @Schema(description = "副本id")
    private Long duplicateId;

    @Schema(description = "许可证编号")
    private String licenseNo;

    @Schema(description = "配置单位")
    private String configUnitName;

    @Schema(description = "设备名称")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "所属区域")
    private String region;

    @Schema(description = "正本发证日期")
    private String originalIssuanceDate;

    @Schema(description = "副本发证日期")
    private String duplicateIssuanceDate;

    @Schema(description = "许可证类型")
    private String licenseType;

    @Schema(description = "状态：1-有效，2-已注销，3-已变更")
    private String status;

}
