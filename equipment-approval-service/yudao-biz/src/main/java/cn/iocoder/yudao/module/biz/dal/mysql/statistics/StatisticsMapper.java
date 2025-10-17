package cn.iocoder.yudao.module.biz.dal.mysql.statistics;

import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.FilterRequest;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    //办件汇总
    Map<String, Object> applicationSummary(@Param("status") Integer status,@Param("req") QueryRequest request);

    //设备
    List<Map<String, Object>> equipmentSummary(@Param("year") Integer year);
    //许可证统计汇总
    Map<String, Object> licenseSummary(@Param("req") QueryRequest request);
    // totalCount: 专家总数
    // radiologyCount: 放射影像专业专家数量
    // radiotherapyCount: 放射治疗专业专家数量
    // nuclearMedicineCount: 核医学专业专家数量
    // healthManagementCount: 卫生管理专业专家数量
    // medicalEquipmentCount: 医学设备与安全防护专业专家数量
    // medicalIntelligenceCount: 医学智能工程专业专家数量
    Map<String, Object> expertSummary();

    //公告统计汇总
    Map<String, Object> noticeSummary(@Param("req") QueryRequest request);
    // 设备生产企业汇总
    Map<String, Object> equipmentManufacturerSummary(@Param("req") QueryRequest request);

    //医疗机构汇总
    Map<String, Object> medicalInstitutionSummary(@Param("req") QueryRequest request);

    //设备拥有量区域分布
    List<Map<String, Object>> equipmentStatisticsArea();

    //设备拥有量--设备详细信息
    IPage<Map<String, Object>> equipmentStatisticsDetail(IPage<Map<String, Object>> page, @Param("req") FilterRequest filterRequest);

    //年度递增分量及总量-年度增量
    // prev_count 上年数量 inc_count 新增数量 yoy_rate 同比增长率百分比
    List<Map<String, Object>> annualIncremental();

    //阶梯配置 -- 阶梯配置分布情况
    // model 机型名称 count 数量
    List<Map<String, Object>> ladderConfigDistribution();
}
