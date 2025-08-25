package cn.iocoder.yudao.module.biz.controller.admin.expertext.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 专家扩展信息分页 Request VO")
@Data
public class ExpertExtPageReqVO extends PageParam {

    @Schema(description = "关联用户ID", example = "27725")
    private Long userId;

    @Schema(description = "专家姓名", example = "赵六")
    private String name;

    @Schema(description = "性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "工作单位")
    private String unit;

    @Schema(description = "职称")
    private String title;

    @Schema(description = "专业领域")
    private String specialty;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "资质证书")
    private String qualificationCert;


}