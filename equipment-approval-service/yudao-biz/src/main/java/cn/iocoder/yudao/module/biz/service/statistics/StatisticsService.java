package cn.iocoder.yudao.module.biz.service.statistics;


import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.DetailResponseVO;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.FilterRequest;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.QueryRequest;
import cn.iocoder.yudao.module.biz.dal.mysql.statistics.StatisticsMapper;
import cn.iocoder.yudao.module.biz.service.utils.NamedTransformation;
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

    public Map<String, Object> historySummary(QueryRequest request) {
        Map<String, Object> map = statisticsMapper.historySummary(request);
        Collection<Object> values = map.values();
        return map;
    }


    public Map<String, Object> applicationSummary(Integer status, QueryRequest request) {
        Integer cnt = jdbcClient.sql("SELECT count(DISTINCT application_id) as cnt FROM biz_acceptance_material where status = '待审核'")
                .query(Integer.class)
                .single();
        Map<String, Object> stringObjectMap = statisticsMapper.applicationSummary(status, request);
        stringObjectMap.put("acceptance_material_count", cnt);
        return stringObjectMap;
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
        return NamedTransformation.convertKeysToCamelCase(statisticsMapper.expertSummary());
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
                                SELECT '铜川市' UNION ALL
                                SELECT '宝鸡市' UNION ALL
                                SELECT '咸阳市' UNION ALL
                                SELECT '渭南市' UNION ALL
                                SELECT '延安市' UNION ALL
                                SELECT '汉中市' UNION ALL
                                SELECT '榆林市' UNION ALL
                                SELECT '安康市' UNION ALL
                                SELECT '商洛市' UNION ALL
                                SELECT '杨凌区' UNION ALL
                                SELECT '西咸新区' UNION ALL
                                SELECT '中国（陕西）自由贸易试验区' UNION ALL
                                SELECT '陕西省(仅本地市)'
                )
                SELECT
                    r.region,
                    COUNT(a.id) AS total
                FROM regions r
                LEFT JOIN biz_institution_ext b ON r.region = b.region
                LEFT JOIN biz_application a ON a.institution_id = b.dept_id

                where a.app_status = 5
                GROUP BY r.region
                ORDER BY total DESC, r.region
                """;
        List<Map<String, Object>> result = jdbcClient.sql(sql).query().listOfRows();
        return NamedTransformation.convertKeysToCamelCase(result);
    }

    public List<Map<String, Object>> annualIncremental() {
        String sql = """
                        WITH yearly_counts AS (
                            SELECT
                                YEAR(create_time) AS `year`,
                                COUNT(*) AS yearly_count
                            FROM biz_license_original
                            WHERE deleted = 0
                            GROUP BY YEAR(create_time)
                        ),
                             yearly_growth AS (
                                 SELECT
                                     `year`,
                                     yearly_count,
                                     LAG(yearly_count, 1) OVER (ORDER BY `year`) AS prev_year_count
                                 FROM yearly_counts
                             )
                        SELECT
                            `year`,
                            yearly_count AS inc_count,
                            prev_year_count AS prev_count,
                            ROUND(
                                    CASE
                                        WHEN prev_year_count IS NULL THEN NULL
                                        WHEN prev_year_count = 0 THEN NULL
                                        ELSE (yearly_count - prev_year_count) * 100.0 / prev_year_count
                                        END,
                                    2
                            ) AS yoy_rate
                        FROM yearly_growth
                        ORDER BY `year`
                """;
        List<Map<String, Object>> maps = jdbcClient.sql(sql).query().listOfRows();
        return NamedTransformation.convertKeysToCamelCase(maps);
    }

    public List<Map<String, Object>> ladderConfigDistribution() {
        String sql = """
                WITH models AS (
                                    SELECT '科研型' as s UNION ALL
                                    SELECT '临床研究型' UNION ALL
                                    SELECT '临床实用型' UNION ALL
                                    SELECT '未实施阶梯分型'
                                )
                                SELECT
                                    m.s ,
                                    COUNT(a.id) AS count,
                                    ROUND(COUNT(a.id) * 100.0 / SUM(COUNT(*)) OVER (), 1) AS percentage
                                FROM models m
                                LEFT JOIN biz_license_original a ON m.s = a.ladder_config_model AND a.deleted = 0
                                GROUP BY s
                """;
//        List<Map<String, Object>> maps = statisticsMapper.ladderConfigDistribution();
        List<Map<String, Object>> maps = jdbcClient.sql(sql).query().listOfRows();
        //因为model为dm关键字 故此处理
        for (Map<String, Object> map : maps) {
            map.put("model", map.get("s"));
            map.remove("s");
        }
        adjustPercentage(maps);
        return maps;
    }

    public List<Map<String, Object>> deviceModelDistribution(FilterRequest req) {
//        String sql = """
//                SELECT
//                                  count(b.id) as num,
//                                  a.region,
//                                  b.license_device_name,
//                                   case when a.institution_type = 1 then '社会办医'
//                                   else '政府办医'
//                                  END AS institution_type_label,
//                                  SUM(CASE WHEN d.acceptance_status = 1 THEN 1 ELSE 0 END) as accepted,
//                                  SUM(CASE WHEN d.acceptance_status = 0 THEN 1 ELSE 0 END) as not_accepted
//                                FROM
//                                  biz_institution_ext a
//                                  LEFT JOIN biz_application b ON a.dept_id = b.institution_id
//                                  left join biz_license_original c on b.id = c.application_id
//                                  left join biz_license_duplicate d on c.id = d.original_id
//                                  where b.app_status = 5 and b.deleted = 0
//                                  GROUP BY a.region, b.license_device_name
//        """;
//        List<Map<String, Object>> dbResult = jdbcClient.sql(sql).query().listOfRows();
        List<Map<String, Object>> dbResult = statisticsMapper.deviceModelDistribution(req);
        List<Map<String, Object>> map = MedicalDeviceProcessor.processToFlatFormat(dbResult);
        return NamedTransformation.convertKeysToCamelCase(map, CaseFormat.LOWER_UNDERSCORE);
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



    public PageResult<DetailResponseVO> equipmentStatisticsDetail(FilterRequest filterRequest) {
        String deviceTypes = filterRequest.getDeviceTypes();
        if (StrUtil.isNotBlank(deviceTypes)) {
            filterRequest.setTypes(Arrays.asList(deviceTypes.split(",")));
        }
        Page<DetailResponseVO> page = new Page<>(filterRequest.getPageNo(), filterRequest.getPageSize());
//        if ("陕西省".equals(filterRequest.getRegion())) {
//            filterRequest.setRegion(null);
//        }
        statisticsMapper.equipmentStatisticsDetail(page, filterRequest);
        //List<Map<String, Object>> convertedRecords = NamedTransformation.convertKeysToCamelCase(page.getRecords());

        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    public Map<String, Object> processedLicenseSummary(Integer year) {
        return NamedTransformation.convertKeysToCamelCase(statisticsMapper.licenseCount(year));
    }
}
