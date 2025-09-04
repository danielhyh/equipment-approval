package cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 甲类设备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ClassAEquipmentRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15943")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "配置单位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("配置单位名称")
    private String configUnitName;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系人")
    private String contactPerson;

    @Schema(description = "生产企业", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产企业")
    private String productionEnterprise;

    @Schema(description = "统一社会信用代码")
    @ExcelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系人电话")
    private String contactPhone;

    @Schema(description = "具体型号")
    @ExcelProperty("具体型号")
    private String specificModel;

    @Schema(description = "法定代表人(或主要负责人)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("法定代表人(或主要负责人)")
    private String legalPerson;

    @Schema(description = "许可设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("许可设备名称")
    private String licenseDeviceName;

    @Schema(description = "装机日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("装机日期")
    private LocalDate installationDate;

    @Schema(description = "所有制性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("所有制性质")
    private String ownershipNature;

    @Schema(description = "设备配置地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("设备配置地址")
    private String equipmentConfigAddress;

    @Schema(description = "采购价格(元)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20340")
    @ExcelProperty("采购价格(元)")
    private BigDecimal purchasePrice;

    @Schema(description = "设备特殊说明", example = "随便")
    @ExcelProperty("设备特殊说明")
    private String specialDescription;

    @Schema(description = "设备使用人员JSON(包含:身份证号码,姓名,性别,出生日期,职称,联系电话)")
    @ExcelProperty("设备使用人员JSON(包含:身份证号码,姓名,性别,出生日期,职称,联系电话)")
    private String equipmentUsers;

    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
