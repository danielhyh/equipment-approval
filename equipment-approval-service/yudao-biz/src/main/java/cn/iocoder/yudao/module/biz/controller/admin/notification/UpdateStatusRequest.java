package cn.iocoder.yudao.module.biz.controller.admin.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateStatusRequest {

    @Schema(description = "通知id")
    @NotEmpty(message = "通知id")
    private Long id;

    @Schema(description = "状态 未发布, 已发布, 已撤回")
    private String status;
}
