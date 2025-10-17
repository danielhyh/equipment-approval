package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DuplicateApprovalRequest {

    @Schema(description = "副本id")
    @NotNull(message = "副本id不能为空")
    private Long id;

    @Schema(description = "审核结果 1-通过，0-不通过")
    @NotNull(message = "审核结果不能为空")
    private Integer reviewResult;

    @Schema(description = "审核意见")
    @NotBlank(message = "审核意见不能为空")
    private String reviewOpinion;

    @Schema(description = "专家id 逗号分隔")
    @NotBlank(message = "专家id不能为空")
    private String expertIds;

    @Schema(description = "专家审核附件path 逗号分隔")
    private String expertAttachments;
}
