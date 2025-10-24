package cn.iocoder.yudao.module.system.service.datasync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SyncResult {

    @Schema(description = "true 成功， false 失败")
    private boolean success;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "外部id")
    private String externalId;
}
