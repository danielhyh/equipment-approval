package cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 机构扩展信息分页 Request VO")
@Data
public class InstitutionExtPageReqVO extends PageParam {

    @Schema(description = "主键ID", example = "17057")
    private Long id;

    @Schema(description = "机构名称", example = "赵六")
    private String institutionName;

    @Schema(description = "机构级别")
    private String institutionLevel;

    @Schema(description = "所有制性质")
    private String ownershipNature;

    @Schema(description = "所属区域")
    private String region;

}