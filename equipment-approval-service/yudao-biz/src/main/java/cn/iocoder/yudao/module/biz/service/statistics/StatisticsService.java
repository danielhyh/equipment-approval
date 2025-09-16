package cn.iocoder.yudao.module.biz.service.statistics;

import cn.iocoder.yudao.module.biz.dal.mysql.statistics.StatisticsMapper;
import com.google.common.base.CaseFormat;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;


    public Map<String, Object> applicationSummary(Integer status) {
        return statisticsMapper.applicationSummary(status);
    }

    public static Map<String, Object> convertKeysToCamelCase(Map<String, Object> map) {
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
