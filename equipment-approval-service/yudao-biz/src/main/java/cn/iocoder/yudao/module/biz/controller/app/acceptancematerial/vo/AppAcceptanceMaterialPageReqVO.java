package cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 验收资料分页 Request VO")
@Data
public class AppAcceptanceMaterialPageReqVO extends PageParam {

    @Schema(description = "申请ID", example = "15177")
    private Long applicationId;

    @Schema(description = "资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件", example = "1")
    private Integer materialType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}