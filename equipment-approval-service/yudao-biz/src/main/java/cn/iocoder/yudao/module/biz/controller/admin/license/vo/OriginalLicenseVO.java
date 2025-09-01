package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "正本响应对象")
@Data
public class OriginalLicenseVO {

    @Schema(description = "单位名称", example = "某某医院")
    private String configUnitName;

    @Schema(description = "统一社会信用代码", example = "912345678901234567")
    private String unifiedSocialCreditCode;

    @Schema(description = "法定代表人", example = "张三")
    private String legalPerson;

    @Schema(description = "许可设备名称", example = "CT扫描仪")
    private String licenseDeviceName;

    @Schema(description = "所有制性质", example = "公立医院")
    private String ownershipNature;

    @Schema(description = "阶梯配置机型", example = "Model-X200")
    private String ladderConfigModel;

    @Schema(description = "设备配置地址", example = "北京市朝阳区XX路1号")
    private String equipmentConfigAddress;

    @Schema(description = "详细地址", example = "北京市朝阳区XX路1号影像科3楼")
    private String detailedAddress;

    @Schema(description = "发证机关", example = "北京市卫生健康委员会")
    private String issuingAuthority;

    @Schema(description = "发证日期", example = "2025-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;
}
