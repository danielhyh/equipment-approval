package cn.iocoder.yudao.module.biz.controller.admin.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateContentRequest {

    @Schema(description = "通知id")
    private Long id;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;
}
