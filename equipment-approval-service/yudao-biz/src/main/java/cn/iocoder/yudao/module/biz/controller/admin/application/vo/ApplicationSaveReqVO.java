package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 申请新增/修改 Request VO")
@Data
public class ApplicationSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30957")
    private Long id;

    @Schema(description = "申请编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "申请编号不能为空")
    private String appNo;

    @Schema(description = "机构ID（关联dept_id）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2710")
    @NotNull(message = "机构ID（关联dept_id）不能为空")
    private Long institutionId;

    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更不能为空")
    private Integer appType;

    @Schema(description = "许可设备名称", example = "李四")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "配置理由", example = "不香")
    private String configReason;

    @Schema(description = "申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成不能为空")
    private Integer appStatus;

    @Schema(description = "初审结果：1-通过，0-不通过")
    private Integer initialReviewResult;

    @Schema(description = "初审意见")
    private String initialReviewOpinion;

    @Schema(description = "初审人ID", example = "13515")
    private Long initialReviewerId;

    @Schema(description = "初审时间")
    private LocalDateTime initialReviewTime;

    @Schema(description = "专家审核结果：1-通过，0-不通过")
    private Integer expertReviewResult;

    @Schema(description = "专家审核意见")
    private String expertReviewOpinion;

    @Schema(description = "专家ID", example = "26192")
    private Long expertId;

    @Schema(description = "专家审核时间")
    private LocalDateTime expertReviewTime;

    @Schema(description = "许可证编号")
    private String licenseNo;

    @Schema(description = "许可证有效期")
    private LocalDate licenseValidDate;

}