package cn.iocoder.yudao.module.biz.service.notification;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    private final JdbcTemplate jdbcTemplate;

    public NotificationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 通知 DTO 映射器
    private final RowMapper<BizNotificationDTO> notificationRowMapper = (rs, rowNum) -> {
        BizNotificationDTO dto = new BizNotificationDTO();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setStatus(rs.getString("status"));
        dto.setPublishTime(rs.getTimestamp("publish_time") != null ? rs.getTimestamp("publish_time").toLocalDateTime() : null);
        dto.setViewCount(rs.getInt("view_count"));
        return dto;
    };

    // 用户未读通知 DTO 映射器
    private final RowMapper<UserUnreadNotificationDTO> unreadNotificationRowMapper = (rs, rowNum) -> {
        UserUnreadNotificationDTO dto = new UserUnreadNotificationDTO();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setPublishTime(rs.getTimestamp("publish_time") != null ? rs.getTimestamp("publish_time").toLocalDateTime() : null);
        dto.setStatus(rs.getString("status"));
        dto.setViewCount(rs.getInt("view_count"));
        return dto;
    };

    // ========== 管理端功能 ==========

    
    public PageResult<BizNotificationDTO> pageNotifications(int pageNum, int pageSize) {
        String countSql = "SELECT COUNT(*) FROM biz_notifications WHERE deleted = b'0'";
        String dataSql = """
            SELECT id, title, content, status, publish_time,
                   (SELECT COUNT(*) FROM biz_user_notification_status
                    WHERE notification_id = n.id AND is_read = 1) AS view_count
            FROM biz_notifications n
            WHERE deleted = 0
            ORDER BY create_time DESC
            """;

        Long total = jdbcTemplate.queryForObject(countSql, Long.class);
        int offset = (pageNum - 1) * pageSize;
        List<BizNotificationDTO> list = jdbcTemplate.query(dataSql, notificationRowMapper, offset, pageSize);
        return new PageResult<>(list, total);
    }

    
    public void updateNotificationStatus(Long id, String status) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String sql = "UPDATE biz_notifications SET `status` = ?,  updater = ?, publish_time = CASE WHEN ? = '已发布' AND publish_time IS NULL THEN NOW() ELSE publish_time END , update_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, status, loginUserId, status, id);
    }

    
    public void updateNotificationContent(Long id, String title, String content) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String sql = "UPDATE biz_notifications SET title = ?, updater = ?, content = ?, update_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, title, loginUserId, content, id);
    }

    public Long createNotification(CreateNotificationRequest request) {
        String title = request.getTitle();
        String content = request.getContent();
        String creator = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        boolean publishNow = request.getPublishNow() != null ? request.getPublishNow() : false;

        String status = publishNow ? "已发布" : "未发布";
        LocalDateTime now = LocalDateTime.now();

        // SQL 插入通知
        String sql = """
        INSERT INTO biz_notifications (title, content, `status`, publish_time, creator, create_time, deleted)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, status);
            ps.setTimestamp(4, publishNow ? Timestamp.valueOf(now) : null);
            ps.setString(5, creator != null ? creator : "");
            ps.setTimestamp(6, Timestamp.valueOf(now));
            ps.setBoolean(7, false);
            return ps;
        }, keyHolder);

        Long notificationId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        if (publishNow) {
            insertUnreadRecordsForAllUsers(notificationId);
        }

        return notificationId;
    }

    private void insertUnreadRecordsForAllUsers(Long notificationId) {
        String userSql = "SELECT id FROM system_users WHERE deleted = 0";
        List<Long> userIds = jdbcTemplate.queryForList(userSql, Long.class);

        if (userIds.isEmpty()) {
            return;
        }

        String insertSql = """
        INSERT INTO biz_user_notification_status (user_id, notification_id, is_read, read_at)
        VALUES (?, ?, 0, NULL)
        ON DUPLICATE KEY UPDATE is_read = is_read  -- 无操作，避免报错
        """;

        jdbcTemplate.batchUpdate(insertSql, userIds, 1000, (ps, userId) -> {
            ps.setLong(1, userId);
            ps.setLong(2, notificationId);
        });
    }

    // ========== 用户端功能 ==========

    
    public List<UserUnreadNotificationDTO> pageUnreadNotifications(Long userId) {

        String dataSql = """
            SELECT n.id, n.title, n.content, n.status, n.publish_time,
                   (SELECT COUNT(*) FROM biz_user_notification_status
                    WHERE notification_id = n.id AND is_read = 1) AS view_count
            FROM biz_notifications n
            LEFT JOIN biz_user_notification_status s ON n.id = s.notification_id AND s.user_id = ?
            WHERE n.deleted = b'0' AND n.status = '已发布' AND (s.is_read IS NULL OR s.is_read = b'0')
            ORDER BY n.publish_time DESC
            """;

        return jdbcTemplate.query(dataSql, unreadNotificationRowMapper, userId);
    }

    
    public void markNotificationAsRead(Long userId, Long notificationId) {
        // 先查是否存在记录
        String checkSql = "SELECT COUNT(*) FROM biz_user_notification_status WHERE user_id = ? AND notification_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, notificationId);

        if (count != null && count > 0) {
            // 存在则更新
            String updateSql = "UPDATE biz_user_notification_status SET is_read = 1, read_at = NOW() WHERE user_id = ? AND notification_id = ?";
            jdbcTemplate.update(updateSql, userId, notificationId);
        } else {
            // 不存在则插入
            String insertSql = "INSERT INTO biz_user_notification_status (user_id, notification_id, is_read, read_at) VALUES (?, ?, 1, NOW())";
            jdbcTemplate.update(insertSql, userId, notificationId);
        }
    }
}
