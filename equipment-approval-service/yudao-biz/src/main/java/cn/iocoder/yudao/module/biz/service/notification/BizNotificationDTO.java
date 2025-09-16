package cn.iocoder.yudao.module.biz.service.notification;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BizNotificationDTO {

    private Long id;
    private String title;
    private String content;
    private String status; // 未发布, 已发布, 已撤回
    private LocalDateTime publishTime;
    private Integer viewCount; // 浏览数（实际是已读人数）
}
