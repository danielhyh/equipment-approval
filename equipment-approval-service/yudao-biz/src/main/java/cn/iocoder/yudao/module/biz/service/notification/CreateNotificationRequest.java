package cn.iocoder.yudao.module.biz.service.notification;

import lombok.Data;

@Data
public class CreateNotificationRequest {

    private String title;
    private String content;
    private String creator;
    private Boolean publishNow; // 是否立即发布
    private String unitName;
    private Long appId;
    private String visibility = "user";
}
