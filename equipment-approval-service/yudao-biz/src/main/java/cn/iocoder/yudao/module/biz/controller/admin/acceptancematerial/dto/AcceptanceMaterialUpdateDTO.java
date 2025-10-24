package cn.iocoder.yudao.module.biz.controller.admin.acceptancematerial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AcceptanceMaterialUpdateDTO {
    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20620")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15177")
    private Long applicationId;

    @Schema(description = "资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer materialType;

    @Schema(description = "资料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    private String materialName;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filePath;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "上传时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime uploadTime;

    @Schema(description = "状态 待审核，已通过，已驳回")
    private String status;
}
