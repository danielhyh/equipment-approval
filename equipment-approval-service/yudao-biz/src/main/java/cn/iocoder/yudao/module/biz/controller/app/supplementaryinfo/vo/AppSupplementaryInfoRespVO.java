package cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 补充信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppSupplementaryInfoRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30671")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10485")
    @ExcelProperty("申请ID")
    private Long applicationId;

    @Schema(description = "信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更")
    private Integer infoType;

    @Schema(description = "信息内容")
    @ExcelProperty("信息内容")
    private String infoContent;

    @Schema(description = "附件路径")
    @ExcelProperty("附件路径")
    private String filePath;

    @Schema(description = "提交时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
