package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 许可证中心查询入参对象")
@Data
public class LicensePageRequestVO {

    private Integer pageSize;

    private Integer pageNum;

    @Schema(description = "许可证类型")
    private String licenseType;

    @Schema(description = "设备类型")
    private String deviceType;
    @Schema(description = "阶梯配置")
    private String ladderConfigModel;
    @Schema(description = "所属区域")
    private String region;
    @Schema(description = "许可证编号、配置单位、设备名称")
    private String keywords;
}
