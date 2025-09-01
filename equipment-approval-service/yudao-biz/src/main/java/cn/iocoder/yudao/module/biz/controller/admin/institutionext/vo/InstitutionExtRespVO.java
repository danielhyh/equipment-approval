package cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 机构扩展信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InstitutionExtRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17057")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "机构名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("机构名称")
    private String institutionName;

    @Schema(description = "统一社会信用代码")
    @ExcelProperty("统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "机构级别")
    @ExcelProperty("机构级别")
    private String institutionLevel;

    @Schema(description = "所有制性质")
    @ExcelProperty("所有制性质")
    private String ownershipNature;

    @Schema(description = "所属区域")
    @ExcelProperty("所属区域")
    private String region;

    @Schema(description = "设备数量")
    @ExcelProperty("设备数量")
    private Integer deviceNum;

    @Schema(description = "法定代表人")
    @ExcelProperty("法定代表人")
    private String legalPerson;

    @Schema(description = "机构性质：1-社会办医，2-筹建或在建，3-自贸区内社会办医，4-筹建或在建自贸区", example = "2")
    @ExcelProperty("机构性质：1-社会办医，2-筹建或在建，3-自贸区内社会办医，4-筹建或在建自贸区")
    private Integer institutionType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}