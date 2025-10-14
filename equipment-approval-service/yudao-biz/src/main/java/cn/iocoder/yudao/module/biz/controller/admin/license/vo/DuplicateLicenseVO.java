package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Schema(description = "副本响应体")
@Data
public class DuplicateLicenseVO {
    private Long id;

    @Schema(description = "生产企业", example = "上海医疗器械有限公司")
    @NotNull(message = "生产企业不能为null")
    private String productionEnterprise;

    @Schema(description = "信息报送日期", example = "2025-04-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "信息报送日期不能为null")
    private Date infoSubmitDate;

    @Schema(description = "具体型号", example = "GE-CT1000")
    @NotBlank(message = "具体型号不能为空")
    private String specificModel;

    @Schema(description = "副本发证机关", example = "上海市卫生健康委员会")
    @NotBlank(message = "副本发证机关不能为空")
    private String duplicateIssuingAuthority;

    @Schema(description = "产品序列号", example = "SN20250401001")
    @NotBlank(message = "产品序列号不能为空")
    private String productSerialNo;

    @Schema(description = "副本发证日期", example = "2025-04-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "副本发证日期不能为null")
    private Date duplicateIssueDate;

    @Schema(description = "装机日期", example = "2025-04-05")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "装机日期不能为null")
    private LocalDate installationDate;

    @Schema(description = "备注信息", example = "该设备用于肿瘤筛查")
    private String remark;

    @Schema(description = "采购价格")
    @NotBlank(message = "采购价格不能为空")
    private String purchasePrice;

    @Schema(description = "设备特殊说明")
    private String specialDescription;

    @Schema(description = "设备使用人员JSON")
    private List<ObjectNode> equipmentUsers;

    @Schema(description = "正本悬挂位置")
    private String originalPosition;

    @JsonIgnore
    private Long equipmentId;
}
