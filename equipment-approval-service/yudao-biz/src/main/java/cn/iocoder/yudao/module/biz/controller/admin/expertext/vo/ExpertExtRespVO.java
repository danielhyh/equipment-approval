package cn.iocoder.yudao.module.biz.controller.admin.expertext.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 专家扩展信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ExpertExtRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21379")
    @ExcelProperty("主键ID")
    private Long id;


    @Schema(description = "专家姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("专家姓名")
    private String name;

    @Schema(description = "性别：1-男，2-女")
    @ExcelProperty("性别：1-男，2-女")
    private Integer gender;

    @Schema(description = "年龄")
    @ExcelProperty("年龄")
    private Integer age;

    @Schema(description = "工作单位")
    @ExcelProperty("工作单位")
    private String unit;

    @Schema(description = "职称")
    @ExcelProperty("职称")
    private String title;

    @Schema(description = "职务")
    @ExcelProperty("职务")
    private String position;

    @Schema(description = "专业领域")
    @ExcelProperty("专业领域")
    private String specialty;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String phone;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "评审数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("评审数量")
    private Integer reviewNums;

    @Schema(description = "备注", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("备注")
    private String remark;
}
