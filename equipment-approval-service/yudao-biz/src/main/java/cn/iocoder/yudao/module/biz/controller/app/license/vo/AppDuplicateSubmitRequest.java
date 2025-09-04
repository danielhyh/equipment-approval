package cn.iocoder.yudao.module.biz.controller.app.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "副本提交入参实体")
public class AppDuplicateSubmitRequest {

    @Schema(description = "正本id")
    @NotNull(message = "正本id不能为null")
    private Long originalId;

    @Schema(description = "生产企业")
    @NotNull(message = "生产企业不能为null")
    private String productionEnterprise;

    @Schema(description = "信息报送日期")
    @NotNull(message = "信息报送日期")
    private LocalDate infoSubmitDate;

    @Schema(description = "具体型号")
    @NotNull(message = "具体型号不能为null")
    private String specificModel;

    @Schema(description = "副本发证机关")
    @NotNull(message = "副本发证机关不能为null")
    private String duplicateIssuingAuthority;

    @Schema(description = "产品序列号")
    @NotNull(message = "产品序列号不能为null")
    private String productSerialNo;

    @Schema(description = "副本发证日期")
    @NotNull(message = "副本发证日期不能为null")
    private LocalDate duplicateIssueDate;

    @Schema(description = "装机日期")
    @NotNull(message = "装机日期不能为null")
    private LocalDate installationDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "采购价格")
    private String purchasePrice;

    @Schema(description = "设备特殊说明")
    private String specialDescription;

    @Schema(description = "设备使用人员 JSON格式")
    private String equipmentUsers;
}
