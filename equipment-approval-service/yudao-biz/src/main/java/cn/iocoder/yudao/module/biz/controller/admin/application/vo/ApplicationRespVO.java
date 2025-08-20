package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ApplicationRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30957")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "申请编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请编号")
    private String appNo;

    @Schema(description = "机构ID（关联dept_id）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2710")
    @ExcelProperty("机构ID（关联dept_id）")
    private Long institutionId;

    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更")
    private Integer appType;

    @Schema(description = "许可设备名称", example = "李四")
    @ExcelProperty("许可设备名称")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    @ExcelProperty("阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "配置理由", example = "不香")
    @ExcelProperty("配置理由")
    private String configReason;

    @Schema(description = "申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成")
    private Integer appStatus;

    @Schema(description = "初审结果：1-通过，0-不通过")
    @ExcelProperty("初审结果：1-通过，0-不通过")
    private Integer initialReviewResult;

    @Schema(description = "初审意见")
    @ExcelProperty("初审意见")
    private String initialReviewOpinion;

    @Schema(description = "初审人ID", example = "13515")
    @ExcelProperty("初审人ID")
    private Long initialReviewerId;

    @Schema(description = "初审时间")
    @ExcelProperty("初审时间")
    private LocalDateTime initialReviewTime;

    @Schema(description = "专家审核结果：1-通过，0-不通过")
    @ExcelProperty("专家审核结果：1-通过，0-不通过")
    private Integer expertReviewResult;

    @Schema(description = "专家审核意见")
    @ExcelProperty("专家审核意见")
    private String expertReviewOpinion;

    @Schema(description = "专家ID", example = "26192")
    @ExcelProperty("专家ID")
    private Long expertId;

    @Schema(description = "专家审核时间")
    @ExcelProperty("专家审核时间")
    private LocalDateTime expertReviewTime;

    @Schema(description = "许可证编号")
    @ExcelProperty("许可证编号")
    private String licenseNo;

    @Schema(description = "许可证有效期")
    @ExcelProperty("许可证有效期")
    private LocalDate licenseValidDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
