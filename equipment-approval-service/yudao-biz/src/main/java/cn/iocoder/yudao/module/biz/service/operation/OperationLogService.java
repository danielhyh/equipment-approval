package cn.iocoder.yudao.module.biz.service.operation;

import cn.iocoder.yudao.module.biz.dal.dataobject.operationlog.OperationLogDO;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OperationLogService {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
        INSERT INTO biz_operation_log
        (business_id, operator_id, operator_name
        , action_desc, extra_info, remark, create_time)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String QUERY_SQL = """
        SELECT
             business_id, operator_id, operator_name,
             action_desc, extra_info, remark, create_time
        FROM biz_operation_log
        WHERE business_id = ?
        ORDER BY create_time DESC
        """;
    // 最简：无 remark，无 extra
    public void log(Long businessId, Long operatorId, String operatorName,
                    String actionDesc) {
        log(businessId, operatorId, operatorName, actionDesc, (String) null);
    }

    //  有 remark，无 extra
    public void log(Long businessId, Long operatorId, String operatorName,
                    String actionDesc, String remark) {
        log(businessId, operatorId, operatorName, actionDesc, null, remark);
    }

    //有 remark，有 extra（Map）
    public void log(Long businessId, Long operatorId, String operatorName,
                    String actionDesc, String remark, String... extraPairs) {
        Map<String, String> extra = of(extraPairs);
        log(businessId, operatorId, operatorName, actionDesc, extra, remark);
    }

    public void log(Long businessId, Long operatorId, String operatorName,
                    String actionDesc, Map<String, String> extra, String remark) {
        String extraJson = JSON.toJSONString(extra);
        jdbcTemplate.update(INSERT_SQL,
                businessId, operatorId, operatorName, actionDesc, extraJson, remark, LocalDateTime.now());
    }

    public List<OperationLogDO> listByBusiness(Long businessId) {
        return jdbcTemplate.query(QUERY_SQL,
                (rs, rowNum) -> {
                    OperationLogDO log = new OperationLogDO();
                    log.setId(rs.getLong("id"));
                    log.setBusinessId(rs.getLong("business_id"));
                    log.setOperatorId(rs.getLong("operator_id"));
                    log.setOperatorName(rs.getString("operator_name"));
                    log.setActionDesc(rs.getString("action_desc"));
                    log.setRemark(rs.getString("remark"));
                    log.setCreateTime(Date.from(rs.getTimestamp("create_time").toInstant()));
                    return log;
                }, businessId);
    }

    private static Map<String, String> of(String... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("参数必须是偶数个：key1, value1, key2, value2...");
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }
}
