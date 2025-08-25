package cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 申请资料 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ApplicationMaterialRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17576")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24818")
    @ExcelProperty("申请ID")
    private Long applicationId;

    @Schema(description = "资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表")
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
