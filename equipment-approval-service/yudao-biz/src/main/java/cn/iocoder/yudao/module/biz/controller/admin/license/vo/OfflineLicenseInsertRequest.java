package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "线下办理许可证请求对象")
public class OfflineLicenseInsertRequest {

    @Schema(description = "正本ID，用于更新时传入，新增时为空")
    private Long originalId;

    @Schema(description = "副本ID，用于更新时传入，新增时为空")
    private Long duplicateId;

    @Schema(description = "正本许可证信息")
    private OriginalLicenseVO originalLicense;

    @Schema(description = "副本许可证信息")
    private DuplicateLicenseVO duplicateLicense;

}
