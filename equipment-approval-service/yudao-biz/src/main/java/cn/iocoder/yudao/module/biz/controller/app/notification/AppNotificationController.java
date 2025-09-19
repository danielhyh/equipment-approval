package cn.iocoder.yudao.module.biz.controller.app.notification;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.notification.UserUnreadNotificationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户APP - 通知")
@RestController
@RequestMapping("/biz/notification")
public class AppNotificationController {

    private final NotificationService notificationService;

    public AppNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/unread")
    @Operation(summary = "用户未读通知")
    @ApiResponse
    public CommonResult<List<UserUnreadNotificationDTO>> unreadPage() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        return success(notificationService.pageUnreadNotifications(loginUserId));
    }

    @GetMapping("/{id}/read")
    @Operation(summary = "标记公告为已读")
    @Parameter(name = "id", description = "公告id")
    public CommonResult<Boolean> markAsRead(@PathVariable Long id) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        notificationService.markNotificationAsRead(userId, id);
        return success(true);
    }
}
