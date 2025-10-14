package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DuplicateApprovalDetails {


    @Schema(description = "审核结果 1-通过，0-不通过")
    @NotNull(message = "审核结果不能为空")
    private Integer reviewResult;

    @Schema(description = "审核意见")
    private String reviewOpinion;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String expertIds;

    private List<Map<String, String>> expertList;

    @Schema(description = "专家审核附件path 逗号分隔")
    private String expertAttachments;
}
