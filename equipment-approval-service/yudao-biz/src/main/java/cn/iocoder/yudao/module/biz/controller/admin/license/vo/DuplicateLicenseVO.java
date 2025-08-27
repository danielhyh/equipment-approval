package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "副本响应体")
@Data
public class DuplicateLicenseVO {
    @Schema(description = "生产企业", example = "上海医疗器械有限公司")
    private String productionEnterprise;

    @Schema(description = "信息报送日期", example = "2025-04-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date infoSubmitDate;

    @Schema(description = "具体型号", example = "GE-CT1000")
    private String specificModel;

    @Schema(description = "副本发证机关", example = "上海市卫生健康委员会")
    private String duplicateIssuingAuthority;

    @Schema(description = "产品序列号", example = "SN20250401001")
    private String productSerialNo;

    @Schema(description = "副本发证日期", example = "2025-04-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date duplicateIssueDate;

    @Schema(description = "装机日期", example = "2025-04-05")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date installationDate;

    @Schema(description = "备注信息", example = "该设备用于肿瘤筛查")
    private String remark;
}
