package cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 机构扩展信息新增/修改 Request VO")
@Data
public class InstitutionExtSaveReqVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "机构名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "机构名称不能为空")
    private String institutionName;

    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "机构级别")
    private String institutionLevel;

    @Schema(description = "所有制性质")
    private String ownershipNature;

    @Schema(description = "所属区域")
    private String region;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "详细地址")
    private String detailedAddress;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "机构性质：1-社会办医，2-筹建或在建，3-自贸区内社会办医，4-筹建或在建自贸区", example = "2")
    private Integer institutionType;

}