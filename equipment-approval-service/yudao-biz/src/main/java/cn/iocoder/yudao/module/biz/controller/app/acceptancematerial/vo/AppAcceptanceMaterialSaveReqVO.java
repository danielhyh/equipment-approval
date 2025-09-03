package cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 验收资料新增/修改 Request VO")
@Data
public class AppAcceptanceMaterialSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20620")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15177")
    @NotNull(message = "申请ID不能为空")
    private Long applicationId;

    @Schema(description = "资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件不能为空")
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