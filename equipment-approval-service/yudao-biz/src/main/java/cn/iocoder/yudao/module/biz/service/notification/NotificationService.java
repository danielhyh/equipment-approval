package cn.iocoder.yudao.module.biz.service.notification;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    private final JdbcTemplate jdbcTemplate;

    private final JdbcClient jdbcClient;

    public NotificationService(JdbcTemplate jdbcTemplate, JdbcClient jdbcClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcClient = jdbcClient;
    }

    // 通知 DTO 映射器
    private final RowMapper<BizNotificationDTO> notificationRowMapper = (rs, rowNum) -> {
        BizNotificationDTO dto = new BizNotificationDTO();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setStatus(rs.getString("status"));
        dto.setPublishTime(rs.getTimestamp("publish_time") != null ? Date.from(rs.getTimestamp("publish_time").toInstant()) : null);
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

    
    public PageResult<BizNotificationDTO> pageNotifications(int pageNum, int pageSize, String status) {
//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//        List<Long> appIdList = jdbcTemplate.query("select id from biz_application where deleted = 0 and creator = ?", (rs, rowNum) -> rs.getLong("id"), loginUserId);


        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

        String countSql = "SELECT COUNT(*) FROM biz_notifications WHERE deleted = 0 and visibility = 'system'";
        if (StrUtil.isNotBlank(status)) {
            String res = " AND status = '%s'";
            countSql += String.format(res, status);
        }
//        MapSqlParameterSource totalQuery = new MapSqlParameterSource();
//        totalQuery.addValue("appIds", appIdList);
        Long total = template.queryForObject(countSql, new HashMap<>(),  Long.class);


        String dataSql = """
            SELECT id, title, content, status, publish_time,
                   (SELECT COUNT(*) FROM biz_user_notification_status
                    WHERE notification_id = n.id AND is_read = 1) AS view_count
            FROM biz_notifications n
            WHERE deleted = 0 and visibility = 'system' %s
            ORDER BY create_time DESC LIMIT :pageNum, :pageSize
            """;

        MapSqlParameterSource paramSource = new MapSqlParameterSource();


        int offset = (pageNum - 1) * pageSize;



//        paramSource.addValue("appIds", appIdList);
        if (StrUtil.isNotBlank(status)) {
            String res = " AND status = '%s'";
            String res2 = String.format(res, status);
            dataSql = String.format(dataSql, res2);
        } else {
            dataSql = String.format(dataSql, "");
        }
        paramSource.addValue("pageSize", pageSize);
        paramSource.addValue("pageNum", offset);
        List<BizNotificationDTO> list = template.query(dataSql, paramSource, notificationRowMapper);
        return new PageResult<>(list, total);
    }

    
    public void updateNotificationStatus(Long id, String status) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String sql = "UPDATE biz_notifications SET `status` = ?,  updater = ?, publish_time = CASE WHEN ? = '已发布' AND publish_time IS NULL THEN NOW() ELSE publish_time END , update_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, status, loginUserId, status, id);
        insertUnreadRecordsForAllUsers(id);
    }

    
    public void updateNotificationContent(Long id, String title, String content) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String sql = "UPDATE biz_notifications SET title = ?, updater = ?, content = ?, update_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, title, loginUserId, content, id);
    }

    public void deleteNotification(Long id) {
        String sql = "update biz_notifications set deleted = 1, update_time = NOW() where id = ?";
        jdbcTemplate.update(sql, id);
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
        INSERT INTO biz_notifications (title, content, unit_name, app_id, `status`, publish_time, creator, create_time, deleted, visibility)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, request.getUnitName());
            ps.setLong(4, request.getAppId());
            ps.setString(5, status);
            ps.setTimestamp(6, publishNow ? Timestamp.valueOf(now) : null);
            ps.setString(7, creator != null ? creator : "");
            ps.setTimestamp(8, Timestamp.valueOf(now));
            ps.setBoolean(9, false);
            ps.setString(10, request.getVisibility());
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
            WHERE n.deleted = 0 AND n.status = '已发布' AND (s.is_read IS NULL OR s.is_read = 0)
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

    public BizNotificationDTO getNotification(Long id) {
        String sql = """
                 SELECT id, title, content, status, publish_time,
                   (SELECT COUNT(*) FROM biz_user_notification_status
                    WHERE notification_id = n.id AND is_read = 1) AS view_count
            FROM biz_notifications n where n.id = ?
                """;
        BizNotificationDTO single = jdbcClient.sql(sql)
                .param(id)
                .query(notificationRowMapper).single();
        return single;
    }
}
