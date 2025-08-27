package cn.iocoder.yudao.module.biz.service.devicelicense;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.biz.dal.dataobject.devicelicense.DeviceLicenseDO;
import cn.iocoder.yudao.module.biz.dal.mysql.devicelicense.DeviceLicenseMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceLicenseService {

    @Resource
    private DeviceLicenseMapper deviceLicenseMapper;

    @Transactional
    public String generateLicenseNumber(
            String deviceClass,           // "甲" or "乙"
            String provinceName,          // 如"北京市"
            String categoryName,          // 如"质子放射治疗系统"
            String stepType,              // "0", "1", "2", "3"
            String manufacturer           // 生产厂家名称
    ) {

        // 1. 参数校验
        if (stepType == null ) {
            throw new ServiceException(1111, "阶梯分型不能为空");
        }
        String stepCode = STEP_TYPE.get(stepType);
        if (stepCode == null) {
            throw new ServiceException(1111, "不支持的阶梯分型: " + stepType);
        }
        String provCode = PROVINCE_MAP.get("陕西省");

        String categoryCode = CATEGORY_CODE_YI.get(categoryName);

        if (categoryCode == null) {
            throw new ServiceException(1111, "不支持的设备类型: " + categoryName);
        }

        // 优先查找“可复用”的编号（已生成但未使用）
        List<DeviceLicenseDO> reusable = deviceLicenseMapper.findReusableByProvinceCategoryStep(provinceName, categoryName, stepType);
        DeviceLicenseDO licenseToUse = null;

        if (!reusable.isEmpty()) {
            // 取最早生成的那个（最小 ID）
            licenseToUse = reusable.get(0);
            // 更新厂家（可能不同请求厂家不同，按最新）
            licenseToUse.setManufacturer(manufacturer);
            // 状态仍为 GENERATED，等待业务后续改为 USED
        }


        // 查询当前已配置数量（同省、同类、同阶梯）
        int count = deviceLicenseMapper.countByProvinceAndCategoryAndStep(
                provinceName, categoryName, stepType);

        if (count >= 5) {
            throw new ServiceException(1111,"该设备在本地区已达最大配置数量（5台），无法继续申请。");
        }

        //  查询该厂家已配置数量
        int manufacturerCount = deviceLicenseMapper.countByProvinceAndCategoryAndStepAndManufacturer(
                provinceName, categoryName, stepType, manufacturer);

        if (manufacturerCount >= 3) {
            throw new ServiceException(1111,"该厂家在本地区此类设备已达上限（3台），无法继续申请。");
        }
        if (licenseToUse != null) {
            // 复用已有编号
            licenseToUse.setManufacturer(manufacturer);
            deviceLicenseMapper.insertOrUpdate(licenseToUse); // 更新厂家
            return licenseToUse.getLicenseNumber();
        }

        //  生成顺序码（5位，从00001开始）
        String nextSeq = String.format("%05d", count + 1);

        //  组合编号
        String numberPart = provCode + categoryCode + stepCode + nextSeq;
        String licenseNumber = deviceClass + numberPart;

        //  保存记录
        DeviceLicenseDO license = new DeviceLicenseDO();
        license.setLicenseNumber(licenseNumber);
        license.setProvinceCode(provinceName);
        license.setCategoryCode(categoryName);
        license.setStepType(stepType);
        license.setDeviceClass(deviceClass);
        license.setManufacturer(manufacturer);

        deviceLicenseMapper.insert(license);

        return licenseNumber;
    }

    /**
     * 解析大型医用设备配置许可证编号，返回详细信息（含数据库查到的 manufacturer）
     * @param licenseNumber 许可证编号，如：甲0102100001
     * @return Map 包含 deviceClass, provinceName, categoryName, stepType, manufacturer
     * @throws IllegalArgumentException 编号格式错误或数据不存在
     */
    public Map<String, String> parseLicenseNumber(String licenseNumber) {
        Map<String, String> result = new HashMap<>();

        if (licenseNumber == null || licenseNumber.length() != 11) {
            throw new IllegalArgumentException("许可证编号必须是11位字符，格式为：甲/乙 + 10位数字");
        }

        String deviceClass = licenseNumber.substring(0, 1);
        if (!"甲".equals(deviceClass) && !"乙".equals(deviceClass)) {
            throw new IllegalArgumentException("编号第一位必须是'甲'或'乙'");
        }
        result.put("deviceClass", deviceClass);

        String numberPart = licenseNumber.substring(1); // 后10位
        if (!numberPart.matches("\\d{10}")) {
            throw new IllegalArgumentException("编号后10位必须全部为数字");
        }

        String provinceCode = numberPart.substring(0, 2);
        String categoryCode = numberPart.substring(2, 4);
        String stepTypeCode = numberPart.substring(4, 5);
        String sequence = numberPart.substring(5, 10); // 00001

        // 反查省份名称
        String provinceName = getProvinceNameByCode(provinceCode);
        if (provinceName == null) {
            throw new IllegalArgumentException("无效的省份代码：" + provinceCode);
        }
        result.put("provinceName", provinceName);

        // 反查设备类别名称
        String categoryName = getCategoryNameByCode(deviceClass, categoryCode);
        if (categoryName == null) {
            throw new IllegalArgumentException("无效的设备类别代码：" + categoryCode + "（" + deviceClass + "类）");
        }
        result.put("categoryName", categoryName);

        // 阶梯分型
        if (!Arrays.asList("0", "1", "2", "3").contains(stepTypeCode)) {
            throw new IllegalArgumentException("无效的阶梯分型代码：" + stepTypeCode);
        }
        Map<String, String> stepTypeMap = Map.of(
                "0", "未实施阶梯分型",
                "1", "临床实用型",
                "2", "临床研究型",
                "3", "科研型"
        );
        result.put("stepType", stepTypeCode);
        result.put("stepTypeName", stepTypeMap.get(stepTypeCode)); // 可选：返回中文名称

        // 查询数据库获取 manufacturer（通过完整编号查询）
        DeviceLicenseDO license = deviceLicenseMapper.findByLicenseNumber(licenseNumber);
        if (license != null) {
            result.put("manufacturer", license.getManufacturer());
        } else {
            result.put("manufacturer", null); // 未找到记录
        }

        // 附加原始字段（可选）
        result.put("provinceCode", provinceCode);
        result.put("categoryCode", categoryCode);
        result.put("sequence", sequence);

        return result;
    }

    // ==================== 辅助方法 ====================

    private String getProvinceNameByCode(String code) {
        return PROVINCE_MAP.entrySet().stream()
                .filter(e -> e.getValue().equals(code))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private String getCategoryNameByCode(String deviceClass, String code) {
        Map<String, String> map = "甲".equals(deviceClass) ? CATEGORY_CODE_JIA : CATEGORY_CODE_YI;

        // 反向查找 key
        return map.entrySet().stream()
                .filter(e -> e.getValue().equals(code))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    // 省代码映射
    private static final Map<String, String> PROVINCE_MAP = Map.ofEntries(
            Map.entry("北京市", "01"),
            Map.entry("天津市", "02"),
            Map.entry("河北省", "03"),
            Map.entry("山西省", "04"),
            Map.entry("内蒙古自治区", "05"),
            Map.entry("辽宁省", "06"),
            Map.entry("吉林省", "07"),
            Map.entry("黑龙江省", "08"),
            Map.entry("上海市", "09"),
            Map.entry("江苏省", "10"),
            Map.entry("浙江省", "11"),
            Map.entry("安徽省", "12"),
            Map.entry("福建省", "13"),
            Map.entry("江西省", "14"),
            Map.entry("山东省", "15"),
            Map.entry("河南省", "16"),
            Map.entry("湖北省", "17"),
            Map.entry("湖南省", "18"),
            Map.entry("广东省", "19"),
            Map.entry("广西壮族自治区", "20"),
            Map.entry("海南省", "21"),
            Map.entry("重庆市", "22"),
            Map.entry("四川省", "23"),
            Map.entry("贵州省", "24"),
            Map.entry("云南省", "25"),
            Map.entry("西藏自治区", "26"),
            Map.entry("陕西省", "27"),
            Map.entry("甘肃省", "28"),
            Map.entry("青海省", "29"),
            Map.entry("宁夏回族自治区", "30"),
            Map.entry("新疆维吾尔自治区", "31")
    );

    // 设备类别编码（甲类）
    private static final Map<String, String> CATEGORY_CODE_JIA = Map.of(
            "重离子放射治疗系统", "01",
            "质子放射治疗系统", "02",
            "正电子发射型磁共振成像系统", "03",
            "高端放射治疗设备", "04"
    );

    // 乙类
    private static final Map<String, String> CATEGORY_CODE_YI = Map.of(
            "X线正电子发射断层扫描仪", "01",
            "内窥镜手术器械控制系统", "02",
            "64排及以上X线计算机断层扫描仪", "03",
            "1.5T及以上磁共振成像系统", "04",
            "直线加速器", "05",
            "伽玛射线立体定向放射治疗系统", "06"
    );

    //阶梯分型代码
    private static final Map<String, String> STEP_TYPE = Map.of(
            "未实施阶梯分型", "0",
            "临床实用型", "1",
            "临床研究型", "2",
            "科研型", "3"
    );
}
