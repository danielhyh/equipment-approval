package cn.iocoder.yudao.module.biz.service.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BizNotificationDTO {

    private Long id;
    private String title;
    private String content;
    private String status; // 未发布, 已发布, 已撤回
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;
    private Integer viewCount; // 浏览数（实际是已读人数）
}
