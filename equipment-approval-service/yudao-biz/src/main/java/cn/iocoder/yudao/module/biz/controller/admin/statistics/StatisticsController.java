package cn.iocoder.yudao.module.biz.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.FilterRequest;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.vo.QueryRequest;
import cn.iocoder.yudao.module.biz.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RequestMapping("/biz/statistics")
@RestController
@Tag(name = "统计分析相关接口")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    @GetMapping("/application-summary")
    @Operation(summary = "办件统计汇总")
    public CommonResult<Map<String, Object>> applicationSummary(@RequestParam(required = false, name = "status") Integer status, QueryRequest request) {
        return success(statisticsService.applicationSummary(status, request));
    }

    @GetMapping("/equipment-summary")
    @Operation(summary = "设备汇总统计")
    public CommonResult<Map<String, Object>> equipmentSummary(@RequestParam(required = false, name = "year") Integer year) {
        return success(statisticsService.equipmentSummary(year));
    }

    @GetMapping("/history-summary")
    @Operation(summary = "历史数据统计汇总")
    public CommonResult<Map<String, Object>> historySummary(@RequestParam(required = false, name = "year") Integer year) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", 0);
        map.put("petCtScanner", 0);
        map.put("gammaRayStereotacticRtSystem", 0);
        map.put("linearAccelerator", 0);
        map.put("endoscopicSurgicalSystem", 0);
        map.put("mriSystem1_5tPlus", 0);
        map.put("ctScanner64SlicePlus", 0);
        return success(map);
    }

    @GetMapping("/license-summary")
    @Operation(summary = "许可证统计汇总")
    public CommonResult<Map<String, Object>> licenseSummary(QueryRequest request) {
        return success(statisticsService.licenseSummary(request));
    }

    @GetMapping("/expert-summary")
    @Operation(summary = "专家统计汇总")
    public CommonResult<Map<String, Object>> expertSummary() {
        return success(statisticsService.expertSummary());
    }

    @GetMapping("/notice-summary")
    @Operation(summary = "公告统计汇总")
    public CommonResult<Map<String, Object>> noticeSummary(QueryRequest request) {
        return success(statisticsService.noticeSummary(request));
    }

    @GetMapping("/equipment-manufacturer-summary")
    @Operation(summary = "设备生产企业汇总")
    public CommonResult<Map<String, Object>> equipmentManufacturerSummary(QueryRequest request) {
        return success(statisticsService.equipmentManufacturerSummary(request));
    }

    @GetMapping("/medical-institution-summary")
    @Operation(summary = "医疗机构汇总")
    public CommonResult<Map<String, Object>> medicalInstitutionSummary(QueryRequest request) {
        return success(statisticsService.medicalInstitutionSummary(request));
    }

    @GetMapping("/equipment-statistics-area")
    @Operation(summary = "设备拥有量统计-区域分布")
    public CommonResult<List<Map<String, Object>>> equipmentStatisticsArea() {
        return success(statisticsService.equipmentStatisticsArea());
    }

    @GetMapping("/equipment-statistics-detail")
    @Operation(summary = "设备拥有量统计-设备详细信息")
    @RequestBody
    public CommonResult<PageResult<Map<String, Object>>> equipmentStatisticsDetail(FilterRequest filterRequest) {
        return success(statisticsService.equipmentStatisticsDetail(filterRequest));
    }

    @GetMapping("/annual-incremental")
    @Operation(summary = "年度递增分量及总量-年度增量")
    public CommonResult<List<Map<String, Object>>> annualIncremental() {
        return success(statisticsService.annualIncremental());
    }

    @GetMapping("/ladder-config-distribution")
    @Operation(summary = "阶梯配置分布")
    public CommonResult<List<Map<String, Object>>> ladderConfigDistribution() {
        return success(statisticsService.ladderConfigDistribution());
    }
}
