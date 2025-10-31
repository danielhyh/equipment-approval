package cn.iocoder.yudao.module.biz.controller.admin.history;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.service.history.HistoryService;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryDuplicateLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryOriginalLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryResult;
import cn.iocoder.yudao.module.biz.service.history.dto.QueryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@Tag(name = "管理后台 - 历史数据")
@RequestMapping("/biz/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @GetMapping("/page")
    @Schema(description = "历史数据分页")
    @ApiResponse
    public CommonResult<PageResult<HistoryResult>> page(QueryRequest queryRequest) {
        return success(historyService.pageResult(queryRequest));
    }


    @GetMapping("/getOriginal")
    @Schema(description = "历史数据正本查询")
    @ApiResponse
    public CommonResult<HistoryOriginalLicense> getOriginal(@RequestParam("id") String id) {
        return success(historyService.getOriginal(id));
    }

    @GetMapping("/getDuplicate")
    @Schema(description = "历史数据副本查询")
    @ApiResponse
    public CommonResult<HistoryDuplicateLicense> getDuplicate(@RequestParam("id") String id) {
        return success(historyService.getDuplicate(id));
    }
}
