package cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 申请资料新增/修改 Request VO")
@Data
public class ApplicationMaterialSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17576")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24818")
    @NotNull(message = "申请ID不能为空")
    private Long applicationId;

    @Schema(description = "资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表不能为空")
    private Integer materialType;

    @Schema(description = "资料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "资料名称不能为空")
    private String materialName;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件路径不能为空")
    private String filePath;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "上传时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "上传时间不能为空")
    private LocalDateTime uploadTime;

}