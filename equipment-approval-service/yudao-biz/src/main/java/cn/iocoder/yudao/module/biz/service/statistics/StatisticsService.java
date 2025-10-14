package cn.iocoder.yudao.module.biz.service.statistics;


import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.FilterRequest;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.QueryRequest;
import cn.iocoder.yudao.module.biz.dal.mysql.statistics.StatisticsMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Resource
    private JdbcClient jdbcClient;


    public Map<String, Object> applicationSummary(Integer status, QueryRequest request) {
        return statisticsMapper.applicationSummary(status, request);
    }


    public Map<String, Object> equipmentSummary(Integer year) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> maps = statisticsMapper.equipmentSummary(year);
        Map<String, Object> row = maps.get(0);
        if (row != null) {
            int sum = row.values().stream().mapToInt(val -> Integer.parseInt(val.toString())).sum();
            map.put("total", sum);
        } else {
            map.put("total", 0);
        }
        map.put("list", maps);
        return map;
    }


    public Map<String, Object> licenseSummary(QueryRequest request) {
        return statisticsMapper.licenseSummary(request);
    }

    public Map<String, Object> expertSummary() {
        return convertKeysToCamelCase(statisticsMapper.expertSummary());
    }

    public Map<String, Object> noticeSummary(QueryRequest request) {
        return statisticsMapper.noticeSummary(request);
    }

    public Map<String, Object> equipmentManufacturerSummary(QueryRequest request) {
        return statisticsMapper.equipmentManufacturerSummary(request);
    }

    public Map<String, Object> medicalInstitutionSummary(QueryRequest request) {
        return statisticsMapper.medicalInstitutionSummary(request);
    }

    public List<Map<String, Object>> equipmentStatisticsArea() {
        String sql = """
                WITH regions AS (
                    SELECT '西安市' AS region UNION ALL
                    SELECT '宝鸡市' UNION ALL
                    SELECT '汉中市' UNION ALL
                    SELECT '咸阳市' UNION ALL
                    SELECT '渭南市' UNION ALL
                    SELECT '延安市' UNION ALL
                    SELECT '安康市' UNION ALL
                    SELECT '榆林市'
                )
                SELECT
                    r.region,
                    COUNT(a.id) AS total
                FROM regions r
                LEFT JOIN biz_institution_ext b ON r.region = b.region\s
                LEFT JOIN biz_application a ON a.institution_id = b.dept_id
                GROUP BY r.region
                ORDER BY total DESC, r.region;
                """;
        List<Map<String, Object>> result = jdbcClient.sql(sql).query().listOfRows();
        return convertKeysToCamelCase(result);
    }

    public List<Map<String, Object>> annualIncremental() {
        return convertKeysToCamelCase(statisticsMapper.annualIncremental());
    }

    public List<Map<String, Object>> ladderConfigDistribution() {
        List<Map<String, Object>> maps = statisticsMapper.ladderConfigDistribution();
        adjustPercentage(maps);
        return maps;
    }

    public void adjustPercentage(List<Map<String, Object>> maps) {
        if (maps == null || maps.isEmpty()) {
            return; // 空列表，无需处理
        }

        // 判断是否所有 model 都是 null
        boolean allNull = maps.stream()
                .allMatch(m -> m.get("model") == null || m.get("model").toString().trim().isEmpty());

        if (allNull) {
            return; // 所有 model 都是 null，不修正百分比
        }

        // 提取当前所有 percentage 并求和（原始值可能是 double 或 BigDecimal）
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> map : maps) {
            Object pctObj = map.get("percentage");
            if (pctObj != null) {
                total = total.add(new BigDecimal(pctObj.toString()));
            }
        }

        // 当前总和已经是 100，不需要调整
        if (total.compareTo(BigDecimal.valueOf(100.00)) == 0) {
            return;
        }

        // 找到最大 count 的项（最合理的是调整最大的那一项来吸收误差）
        Map<String, Object> target = null;
        int maxCount = -1;
        for (Map<String, Object> map : maps) {
            Object countObj = map.get("count");
            int count = countObj != null ? ((Number) countObj).intValue() : 0;
            if (count > maxCount) {
                maxCount = count;
                target = map;
            }
        }

        // 计算其他项之和：totalOthers = total - currentPercentageOfTarget
        BigDecimal currentPct = BigDecimal.ZERO;
        Object pctObj = target.get("percentage");
        if (pctObj != null) {
            currentPct = new BigDecimal(pctObj.toString());
        }

        BigDecimal othersSum = total.subtract(currentPct);
        BigDecimal adjustedPct = BigDecimal.valueOf(100.00).subtract(othersSum);

        // 保留两位小数，四舍五入
        adjustedPct = adjustedPct.setScale(2, RoundingMode.HALF_UP);

        // 更新目标项
        target.put("percentage", adjustedPct.doubleValue()); // 或保持为 BigDecimal，看前端需要
    }



    public PageResult<Map<String, Object>> equipmentStatisticsDetail(FilterRequest filterRequest) {
        String deviceTypes = filterRequest.getDeviceTypes();
        if (StrUtil.isNotBlank(deviceTypes)) {
            filterRequest.setTypes(Arrays.asList(deviceTypes.split(",")));
        }
        Page<Map<String, Object>> page = new Page<>(filterRequest.getPageNo(), filterRequest.getPageSize());
        statisticsMapper.equipmentStatisticsDetail(page, filterRequest);
        List<Map<String, Object>> convertedRecords = convertKeysToCamelCase(page.getRecords());

        return new PageResult<>(convertedRecords, page.getTotal());
    }


    public List<Map<String, Object>> convertKeysToCamelCase(List<Map<String, Object>> list) {
        if (list == null) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map != null) {
                result.add(convertKeysToCamelCase(map));
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public Map<String, Object> convertKeysToCamelCase(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 转换 key：snake_case -> camelCase
            String convertedKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

            // 如果 value 是 Map，递归处理
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                value = convertKeysToCamelCase(nestedMap);
            }
            // 注意：如果还需要处理 List 或 Collection，可以进一步扩展

            result.put(convertedKey, value);
        }
        return result;
    }
}
