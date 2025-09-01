package cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 设备生产企业 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EquipmentManufacturerRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28331")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "公司名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("公司名称")
    private String companyName;

    @Schema(description = "公司地址")
    @ExcelProperty("公司地址")
    private String address;

    @Schema(description = "主要设备类型", example = "1")
    @ExcelProperty("主要设备类型")
    private String mainEquipmentType;

    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}