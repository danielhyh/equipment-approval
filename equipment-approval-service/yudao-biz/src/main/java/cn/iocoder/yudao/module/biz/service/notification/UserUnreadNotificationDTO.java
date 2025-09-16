package cn.iocoder.yudao.module.biz.service.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserUnreadNotificationDTO {

    private Long id;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "发布时间")
    private LocalDateTime publishTime;
    @Schema(description = "状态")
    private String status;
    @Schema(description = "浏览量")
    private Integer viewCount;
}
