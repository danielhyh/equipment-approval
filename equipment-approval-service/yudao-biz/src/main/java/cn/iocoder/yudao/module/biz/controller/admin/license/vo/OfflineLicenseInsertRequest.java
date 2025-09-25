package cn.iocoder.yudao.module.biz.controller.admin.license.vo;

import lombok.Data;

@Data
public class OfflineLicenseInsertRequest {

    OriginalLicenseVO originalLicense;

    DuplicateLicenseVO duplicateLicense;


}
