package cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 设备生产企业新增/修改 Request VO")
@Data
public class EquipmentManufacturerSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28331")
    private Long id;

    @Schema(description = "公司名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "公司名称不能为空")
    private String companyName;

    @Schema(description = "公司地址")
    private String address;

    @Schema(description = "主要设备类型", example = "1")
    private String mainEquipmentType;

    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态：1-启用，0-禁用不能为空")
    private Integer status;

}