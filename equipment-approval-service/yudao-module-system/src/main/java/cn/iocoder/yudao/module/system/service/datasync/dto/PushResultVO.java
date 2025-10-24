package cn.iocoder.yudao.module.system.service.datasync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PushResultVO {

    private Boolean success;
    private String message;
    private String externalId;  // 返回外部ID，便于直报系统追踪

    public static PushResultVO fail(@NotBlank(message = "外部用户ID不能为空") String userId, String message) {
        PushResultVO result = new PushResultVO();
        result.setSuccess(false);
        result.setMessage(message);
        result.setExternalId(userId);
        return result;
    }

    public static PushResultVO success(@NotBlank(message = "外部用户ID不能为空") String userId) {
        PushResultVO result = new PushResultVO();
        result.setSuccess(true);
        result.setExternalId(userId);
        return result;
    }
}
