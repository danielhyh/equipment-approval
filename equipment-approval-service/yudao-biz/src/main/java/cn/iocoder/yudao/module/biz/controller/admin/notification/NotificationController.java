package cn.iocoder.yudao.module.biz.controller.admin.notification;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.service.notification.BizNotificationDTO;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 通知")
@RestController
@RequestMapping("/biz/notification")
public class NotificationController {

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private NotificationService notificationService;

    @GetMapping("/notification-summary")
    @Operation(summary = "待办通知汇总")
    public CommonResult<Map<String, String>> notificationSummary() {
        String sql = """
                SELECT
                    COALESCE(SUM(IF(b.app_type = 1, 1, 0)), 0) as '证书申请',
                    COALESCE(SUM(IF(b.app_type = 2, 1, 0)), 0) as '证书补办',
                    COALESCE(SUM(IF(b.app_type = 3, 1, 0)), 0) as '证书变更',
                    COALESCE(SUM(IF(b.app_type = 4, 1, 0)), 0) as '信息变更',
                    COUNT(b.app_type) as '总数'
                FROM
                    biz_notifications a
                LEFT JOIN biz_application b on a.app_id = b.id
                where a.deleted = b'0' and b.deleted = b'0'
                """;
        Map<String, String> single = jdbcClient.sql(sql).query(JdbcClientHelper::resultSetToMap).single();
        return success(single);
    }

    @GetMapping("/todo-notification")
    @Operation(summary = "待办通知")
    public CommonResult<List<Map<String, String>>> todoNotification() {
        String sql = """
                select app_type, content, title, publish_time from biz_notifications a
                left join biz_application b on a.app_id = b.id
                where a.deleted = b'0' and b.deleted = b'0'
                """;
        List<Map<String, String>> list = jdbcClient.sql(sql).query(JdbcClientHelper::resultSetToMap).list();
        return success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "通知分页查询")
    public CommonResult<PageResult<BizNotificationDTO>> page(@RequestParam("pageNum") Integer pageNum,
                                                             @RequestParam("pageSize") Integer pageSize) {
        return success(notificationService.pageNotifications(pageNum, pageSize));
    }

    @PostMapping("/update-status")
    @Operation(summary = "更新通知状态")
    public CommonResult<Boolean> updateStatus(@RequestBody UpdateStatusRequest req) {
        notificationService.updateNotificationStatus(req.getId(), req.getStatus());
        return success(true);
    }

    @PostMapping("/update-content")
    @Operation(summary = "更新通知内容")
    public CommonResult<Boolean> updateStatus(@RequestBody UpdateContentRequest req) {
        notificationService.updateNotificationContent(req.getId(), req.getTitle(), req.getContent());
        return success(true);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<Boolean> deleteById(@PathVariable("id") Long id) {
        notificationService.deleteNotification(id);
        return success(true);
    }



}
