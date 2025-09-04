package cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 补充信息分页 Request VO")
@Data
public class AppSupplementaryInfoPageReqVO extends PageParam {

    @Schema(description = "信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更", example = "1")
    private Integer infoType;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}