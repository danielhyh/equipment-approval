package cn.iocoder.yudao.module.biz.controller.admin.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.biz.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResult<Map<String, Object>> applicationSummary(@RequestParam(required = false, name = "status") Integer status) {
        return success(statisticsService.applicationSummary(status));
    }
}
