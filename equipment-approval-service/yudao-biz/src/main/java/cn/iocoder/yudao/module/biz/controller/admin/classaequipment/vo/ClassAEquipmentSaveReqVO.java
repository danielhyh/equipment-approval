package cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 甲类设备新增/修改 Request VO")
@Data
public class ClassAEquipmentSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15943")
    private Long id;

    @Schema(description = "配置单位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "配置单位名称不能为空")
    private String configUnitName;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系人不能为空")
    private String contactPerson;

    @Schema(description = "生产企业", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "生产企业不能为空")
    private String productionEnterprise;

    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "联系人电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "联系人电话不能为空")
    private String contactPhone;

    @Schema(description = "具体型号")
    private String specificModel;

    @Schema(description = "法定代表人(或主要负责人)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "法定代表人(或主要负责人)不能为空")
    private String legalPerson;

    @Schema(description = "许可设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "许可设备名称不能为空")
    private String licenseDeviceName;

    @Schema(description = "装机日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "装机日期不能为空")
    private LocalDate installationDate;

    @Schema(description = "所有制性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "所有制性质不能为空")
    private String ownershipNature;

    @Schema(description = "设备配置地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "设备配置地址不能为空")
    private String equipmentConfigAddress;

    @Schema(description = "采购价格(元)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20340")
    @NotNull(message = "采购价格(元)不能为空")
    private BigDecimal purchasePrice;

    @Schema(description = "设备特殊说明", example = "随便")
    private String specialDescription;

    @Schema(description = "设备使用人员JSON(包含:身份证号码,姓名,性别,出生日期,职称,联系电话)")
    private String equipmentUsers;

    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：1-启用，0-禁用不能为空")
    private Integer status;

}