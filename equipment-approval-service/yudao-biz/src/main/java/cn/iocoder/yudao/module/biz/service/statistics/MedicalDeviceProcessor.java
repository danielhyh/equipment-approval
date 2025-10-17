package cn.iocoder.yudao.module.biz.service.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class MedicalDeviceProcessor {
    private static final ObjectMapper mapper = new ObjectMapper();

    // 固定的区域列表（顺序：陕西省放最前面）
    private static final List<String> REGIONS = Arrays.asList(
            "陕西省", "西安市", "宝鸡市", "汉中市", "咸阳市", "铜川市", "渭南市","延安市","榆林市","安康市","商洛市","杨凌区","西咸新区", "中国（陕西）自由贸易试验区"
    );

    // 固定的设备类型列表
    private static final List<String> DEVICE_TYPES = Arrays.asList(
            "伽玛射线立体定向放射治疗系统",
            "直线加速器",
            "1.5T及以上磁共振成像系统",
            "64排及以上X线计算机断层扫描仪",
            "X线正电子发射断层扫描仪",
            "内窥镜手术器械控制系统"
    );

    // 设备简写映射
    private static final Map<String, String> DEVICE_ABBR_MAP = new HashMap<String, String>() {{
        put("伽玛射线立体定向放射治疗系统", "gamma");
        put("直线加速器", "linac");
        put("1.5T及以上磁共振成像系统", "mri");
        put("64排及以上X线计算机断层扫描仪", "ct64");
        put("X线正电子发射断层扫描仪", "pet");
        put("内窥镜手术器械控制系统", "endo");
    }};

    /**
     * 处理查询结果，返回扁平化格式
     *
     * @param queryResults 数据库查询结果
     * @return List<ObjectNode> 扁平化的表格数据
     *
     * 返回格式示例：
     * [{
     *   "region": "陕西省",
     *   "total": 150,
     *   "gamma_gov": 10,
     *   "gamma_society": 5,
     *   "gamma_accepted": 12,
     *   "gamma_notAccepted": 3,
     *   "linac_gov": 20,
     *   "linac_society": 15,
     *   ...
     * }]
     */
    public static List<Map<String, Object>> processToFlatFormat(List<Map<String, Object>> queryResults) {
        // 1. 数据类型转换处理
        List<Map<String, Object>> filteredResults = queryResults.stream()
                .peek(map -> map.replaceAll((k, v) -> {
                    if (v instanceof Long val) {
                        v = val.intValue();
                    } else if (v instanceof BigDecimal val) {
                        v = val.setScale(0, RoundingMode.DOWN).intValue();
                    }
                    return v;
                }))
                .toList();

        // 2. 构建数据映射：region -> deviceAbbr_field -> value
        Map<String, Map<String, Integer>> dataMap = new HashMap<>();

        // 初始化所有区域（包括所有城市）
        for (String region : REGIONS) {
            dataMap.put(region, new HashMap<>());
        }

        // 3. 填充查询结果
        for (Map<String, Object> row : filteredResults) {
            String region = (String) row.get("region");
            String deviceName = (String) row.get("license_device_name");
            String institutionType = (String) row.get("institution_type_label");
            Integer num = (Integer) row.get("num");
            Integer accepted = (Integer) row.getOrDefault("accepted", 0);
            Integer notAccepted = (Integer) row.getOrDefault("not_accepted", 0);

            // 跳过未知设备
            if (!DEVICE_ABBR_MAP.containsKey(deviceName)) {
                continue;
            }

            // 跳过陕西省的原始数据（后面会重新计算汇总）
            if ("陕西省".equals(region)) {
                continue;
            }

            // 如果region不在REGIONS列表中，跳过（防止脏数据）
            if (!dataMap.containsKey(region)) {
                continue;
            }

            Map<String, Integer> regionData = dataMap.get(region);
            String deviceAbbr = DEVICE_ABBR_MAP.get(deviceName);

            // 设置机构类型数据
            String typeKey = deviceAbbr + ("政府办医".equals(institutionType) ? "_gov" : "_society");
            regionData.put(typeKey, regionData.getOrDefault(typeKey, 0) + num);

            // 累加受理状态数据
            String acceptedKey = deviceAbbr + "_accepted";
            String notAcceptedKey = deviceAbbr + "_notAccepted";
            regionData.put(acceptedKey, regionData.getOrDefault(acceptedKey, 0) + accepted);
            regionData.put(notAcceptedKey, regionData.getOrDefault(notAcceptedKey, 0) + notAccepted);
        }

        // 4. 计算陕西省的汇总数据（所有其他市的总和）
        Map<String, Integer> shaanxiData = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : dataMap.entrySet()) {
            if (!"陕西省".equals(entry.getKey())) {
                for (Map.Entry<String, Integer> dataEntry : entry.getValue().entrySet()) {
                    String key = dataEntry.getKey();
                    Integer value = dataEntry.getValue();
                    shaanxiData.put(key, shaanxiData.getOrDefault(key, 0) + value);
                }
            }
        }
        dataMap.put("陕西省", shaanxiData);

        // 5. 构建最终结果（按REGIONS顺序）
        List<Map<String, Object>> result = new ArrayList<>();

        for (String region : REGIONS) {
            Map<String, Object> node = new HashMap<>();
            node.put("region", region);

            Map<String, Integer> regionData = dataMap.get(region);
            int total = 0;

            // 填充所有设备的数据（缺失的补0）
            for (String deviceType : DEVICE_TYPES) {
                String deviceAbbr = DEVICE_ABBR_MAP.get(deviceType);

                int gov = regionData.getOrDefault(deviceAbbr + "_gov", 0);
                int society = regionData.getOrDefault(deviceAbbr + "_society", 0);
                int accepted = regionData.getOrDefault(deviceAbbr + "_accepted", 0);
                int notAccepted = regionData.getOrDefault(deviceAbbr + "_notAccepted", 0);

                node.put(deviceAbbr + "_gov", gov);
                node.put(deviceAbbr + "_society", society);
                node.put(deviceAbbr + "_accepted", accepted);
                node.put(deviceAbbr + "_notAccepted", notAccepted);

                total += gov + society;
            }

            node.put("total", total);
            result.add(node);
        }

        return result;
    }

    /**
     * 获取表头配置（供前端使用）
     */
    public static List<Map<String, Object>> getTableHeaders() {
        List<Map<String, Object>> headers = new ArrayList<>();

        // 固定列
        headers.add(createHeader("region", "行政区", true));
        headers.add(createHeader("total", "合计", false));

        // 设备列（每个设备4个子列）
        for (String deviceType : DEVICE_TYPES) {
            String deviceAbbr = DEVICE_ABBR_MAP.get(deviceType);

            Map<String, Object> deviceHeader = new HashMap<>();
            deviceHeader.put("label", deviceType);

            List<Map<String, Object>> children = Arrays.asList(
                    createHeader(deviceAbbr + "_society", "社会办医", false),
                    createHeader(deviceAbbr + "_gov", "政府办医", false),
                    createHeader(deviceAbbr + "_accepted", "已受理", false),
                    createHeader(deviceAbbr + "_notAccepted", "未受理", false)
            );
            deviceHeader.put("children", children);

            headers.add(deviceHeader);
        }

        return headers;
    }

    private static Map<String, Object> createHeader(String prop, String label, boolean fixed) {
        Map<String, Object> header = new HashMap<>();
        header.put("prop", prop);
        header.put("label", label);
        if (fixed) {
            header.put("fixed", true);
        }
        return header;
    }

    /**
     * 测试示例
     */
    public static void main(String[] args) throws Exception {
        // 模拟数据库查询结果
        List<Map<String, Object>> queryResults = Arrays.asList(
                createRow(16, "咸阳市", "内窥镜手术器械控制系统", "政府办医", 4, 3),
                createRow(2, "咸阳市", "X线正电子发射断层扫描仪", "政府办医", 1, 0),
                createRow(1, "咸阳市", "直线加速器", "政府办医", 1, 0),
                createRow(1, "西安市", "X线正电子发射断层扫描仪", "政府办医", 0, 0),
                createRow(5, "西安市", "直线加速器", "社会办医", 3, 2),
                createRow(3, "宝鸡市", "1.5T及以上磁共振成像系统", "政府办医", 2, 1),
                createRow(2, "汉中市", "64排及以上X线计算机断层扫描仪", "社会办医", 1, 1),
                createRow(4, "安康市", "伽玛射线立体定向放射治疗系统", "政府办医", 3, 1)
        );

        // 处理数据
        List<Map<String, Object>> result = processToFlatFormat(queryResults);

        System.out.println("=== 扁平化数据结果 ===");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

    }

    private static Map<String, Object> createRow(int num, String region, String deviceName,
                                                 String institutionType, int accepted, int notAccepted) {
        Map<String, Object> row = new HashMap<>();
        row.put("num", num);
        row.put("region", region);
        row.put("license_device_name", deviceName);
        row.put("institution_type_label", institutionType);
        row.put("accepted", accepted);
        row.put("not_accepted", notAccepted);
        return row;
    }


}
