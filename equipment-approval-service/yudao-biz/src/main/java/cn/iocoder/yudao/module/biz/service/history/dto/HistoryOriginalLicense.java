package cn.iocoder.yudao.module.biz.service.history.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class HistoryOriginalLicense {

    @Schema(description = "配置单位")
    private String institutionName;

    @Schema(description = "社会统一信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "许可设备名称")
    private String licenseDeviceName;

    @Schema(description = "所有制性质")
    private String ownershipNature;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "设备配置地址")
    private String equipmentConfigAddress;

    @Schema(description = "详细地址")
    private String detailedAddress;

    @Schema(description = "发证机关")
    private String issuingAuthority;

    @Schema(description = "发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

}
