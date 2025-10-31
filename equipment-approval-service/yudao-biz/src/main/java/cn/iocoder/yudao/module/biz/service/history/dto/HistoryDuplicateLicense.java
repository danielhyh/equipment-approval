package cn.iocoder.yudao.module.biz.service.history.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class HistoryDuplicateLicense {
    @Schema(description = "生产企业")
    private String productionEnterprise;

    @Schema(description = "信息报送日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date infoSubmitDate;

    @Schema(description = "具体型号")
    private String specificModel;

    @Schema(description = "副本发证机关")
    private String duplicateIssuingAuthority;

    @Schema(description = "产品序列号")
    private String productSerialNo;

    @Schema(description = "副本发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date duplicateIssueDate;

    @Schema(description = "装机日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate installationDate;

    @Schema(description = "备注信息")
    private String remark;
}
