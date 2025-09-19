package cn.iocoder.yudao.module.biz.service.statistics;

import cn.iocoder.yudao.module.biz.dal.mysql.statistics.StatisticsMapper;
import com.google.common.base.CaseFormat;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;


    public Map<String, Object> applicationSummary(Integer status) {
        return statisticsMapper.applicationSummary(status);
    }


    public Map<String, Object> equipmentSummary(Integer year) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> maps = convertKeysToCamelCase(statisticsMapper.equipmentSummary(year));
        map.put("total", maps.size());
        map.put("list", maps);
        return map;
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
