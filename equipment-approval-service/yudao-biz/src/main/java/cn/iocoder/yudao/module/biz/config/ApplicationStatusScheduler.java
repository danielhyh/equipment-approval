package cn.iocoder.yudao.module.biz.config;

import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;
import cn.iocoder.yudao.module.biz.service.notification.CreateNotificationRequest;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.operation.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class ApplicationStatusScheduler {

    private final JdbcClient client; // 或直接注入 Mapper

    private final ApplicationMapper applicationMapper;

    private final OperationLogService operationLogService;

    private final NotificationService notificationService;

    private final TransactionTemplate transactionTemplate;

    Map<Integer, String> appTypeMap = Map.of(1, "申请", 2, "补办", 3 , "变更", 4, "基本信息变更");


    /**
     * 每天凌晨 2 点执行一次，检查并更新过期申请
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点
    @Transactional
    public void updateExpiredApplications() {
        LocalDate today = LocalDate.now();
        LocalDate deadlineThreshold = today.minusDays(45); // 45天前的日期
        LambdaQueryWrapper<ApplicationDO> wrapper = Wrappers.lambdaQuery(ApplicationDO.class)
                .le(ApplicationDO::getCreateTime, deadlineThreshold)
                .eq(ApplicationDO::getAppStatus, 1);
        List<ApplicationDO> applicationDOS = applicationMapper.selectList(wrapper);

        // 计算45天前的时间点

        String sql = """
            UPDATE biz_application
            SET app_status = 2,  -- 初审不通过
                update_time = NOW(),
                updater = 'system-auto'
            WHERE app_status = 1  -- 待初审
              AND create_time <= ?
              AND deleted = 0
            """;
        transactionTemplate.execute(status -> {
            try {
                int updatedRows = client.sql(sql).param(deadlineThreshold).update();
                for (ApplicationDO applicationDO : applicationDOS) {
                    String type = appTypeMap.get(applicationDO.getAppType());
                    operationLogService.log(applicationDO.getId(), 1L, "admin", type + "已过期，初步审核未通过");
                    publisherNotification(applicationDO);
                }
            } catch (Exception e) {
                status.setRollbackOnly();
            }

            return null;
        });

    }

    private void publisherNotification(ApplicationDO applicationDO) {
        String licenseDeviceName = applicationDO.getLicenseDeviceName();
        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
        createNotificationRequest.setTitle(licenseDeviceName + "申请进度更新");
        String format = String.format("您提交的%s配置许可证%s%s, 审核意见：%s。", licenseDeviceName, appTypeMap.get(applicationDO.getAppType()), "已过期，初步审核未通过", "未通过");
        createNotificationRequest.setContent(format);
        createNotificationRequest.setPublishNow(true);
        createNotificationRequest.setCreator(String.valueOf((Long) 1L));
        notificationService.createNotification(createNotificationRequest);
    }
}
