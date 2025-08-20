package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 申请分页 Request VO")
@Data
public class ApplicationPageReqVO extends PageParam {


    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更", example = "1")
    private Integer appType;

    @Schema(description = "许可设备名称", example = "李四")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;


    @Schema(description = "申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成", example = "1")
    private Integer appStatus;

    @Schema(description = "初审结果：1-通过，0-不通过")
    private Integer initialReviewResult;

    @Schema(description = "专家审核结果：1-通过，0-不通过")
    private Integer expertReviewResult;

    @Schema(description = "申请单位或者设备名称")
    private String deptOrDeviceName;
}