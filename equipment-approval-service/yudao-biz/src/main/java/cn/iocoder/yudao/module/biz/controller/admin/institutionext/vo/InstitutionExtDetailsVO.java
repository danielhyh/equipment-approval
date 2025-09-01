package cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "机构扩展信息 详情Response VO")
public class InstitutionExtDetailsVO extends InstitutionExtRespVO {

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系人电话")
    private String contactPhone;

    @Schema(description = "上级机构")
    private String superiorInstitution;
}
