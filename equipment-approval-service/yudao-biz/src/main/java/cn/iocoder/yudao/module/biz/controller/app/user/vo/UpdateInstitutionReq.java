package cn.iocoder.yudao.module.biz.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateInstitutionReq {

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "营业执照图片")
    private String businessLicensePic;

    @Schema(description = "法人代表")
    private String legalPerson;

    @Schema(description = "详细地址")
    private String detailedAddress;

    @Schema(description = "机构id")
    @NotEmpty(message = "机构id不能为空")
    private Long institutionId;

    public String generateChangeDescription() {
        List<String> changes = new ArrayList<>();

        if (this.contactPerson != null && !this.contactPerson.trim().isEmpty()) {
            changes.add("联系人变更");
        }
        if (this.contactPhone != null && !this.contactPhone.trim().isEmpty()) {
            changes.add("联系电话变更");
        }
        if (this.businessLicensePic != null && !this.businessLicensePic.trim().isEmpty()) {
            changes.add("营业执照图片变更");
        }
        if (this.legalPerson != null && !this.legalPerson.trim().isEmpty()) {
            changes.add("法人代表变更");
        }
        if (this.detailedAddress != null && !this.detailedAddress.trim().isEmpty()) {
            changes.add("详细地址变更");
        }

        return String.join("，", changes); // 使用中文逗号分隔
    }
}
