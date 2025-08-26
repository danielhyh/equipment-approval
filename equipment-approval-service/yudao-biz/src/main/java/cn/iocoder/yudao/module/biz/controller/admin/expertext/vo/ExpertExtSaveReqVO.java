package cn.iocoder.yudao.module.biz.controller.admin.expertext.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 专家扩展信息新增/修改 Request VO")
@Data
public class ExpertExtSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21379")
    private Long id;

//    @Schema(description = "关联用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27725")
//    @NotNull(message = "关联用户ID不能为空")
//    private Long userId;

    @Schema(description = "专家姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "专家姓名不能为空")
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

    private String remark;

    private String position;

//    @Schema(description = "资质证书")
//    private String qualificationCert;

//    @Schema(description = "状态：1-启用，0-禁用", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
//    @NotNull(message = "状态：1-启用，0-禁用不能为空")
//    private Integer status;

}