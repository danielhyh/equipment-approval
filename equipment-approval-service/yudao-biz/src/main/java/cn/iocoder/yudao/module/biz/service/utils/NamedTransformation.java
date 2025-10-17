package cn.iocoder.yudao.module.biz.service.utils;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedTransformation {

    public static List<Map<String, Object>> convertKeysToCamelCase(List<Map<String, Object>> list) {
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

    public static List<Map<String, Object>> convertKeysToCamelCase(List<Map<String, Object>> list, CaseFormat caseFormat) {
        if (list == null) {
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map != null) {
                result.add(convertKeysToCamelCase(map, caseFormat));
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public static Map<String, Object> convertKeysToCamelCase(Map<String, Object> map) {
        return convertKeysToCamelCase(map, CaseFormat.LOWER_UNDERSCORE);
    }

    public static Map<String, Object> convertKeysToCamelCase(Map<String, Object> map, CaseFormat caseFormat) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // 转换 key：snake_case -> camelCase
            String convertedKey = caseFormat.to(CaseFormat.LOWER_CAMEL, key);

            // 如果 value 是 Map，递归处理
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                value = convertKeysToCamelCase(nestedMap, caseFormat);
            }
            // 注意：如果还需要处理 List 或 Collection，可以进一步扩展

            result.put(convertedKey, value);
        }
        return result;
    }
}
