package cn.iocoder.yudao.module.biz.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "机构信息详情实体")
public class UserInstitutionInfo {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "机构id")
    private Long institutionId;

    @Schema(description = "法人代表")
    private String legalPerson;

    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(description = "详细地址")
    private String detailedAddress;

    @Schema(description = "所有制性质")
    private String ownershipNature;

    @Schema(description = "机构性质")
    private String institutionType;

    @Schema(description = "上级机构名称")
    private String superiorInstitution;

    @Schema(description = "机构级别")
    private String institutionLevel;

    @Schema(description = "地区（已拼接陕西省）")
    private String region;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;
}
