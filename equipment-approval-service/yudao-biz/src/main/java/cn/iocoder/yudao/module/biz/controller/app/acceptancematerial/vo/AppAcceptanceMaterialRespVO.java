package cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 验收资料 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppAcceptanceMaterialRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20620")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15177")
    @ExcelProperty("申请ID")
    private Long applicationId;

    @Schema(description = "资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件")
    private Integer materialType;

    @Schema(description = "资料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("资料名称")
    private String materialName;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文件路径")
    private String filePath;

    @Schema(description = "文件大小(字节)")
    @ExcelProperty("文件大小(字节)")
    private Long fileSize;

    @Schema(description = "上传时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("上传时间")
    private LocalDateTime uploadTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
