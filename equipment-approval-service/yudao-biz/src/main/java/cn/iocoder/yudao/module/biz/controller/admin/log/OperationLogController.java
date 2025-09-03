package cn.iocoder.yudao.module.biz.controller.admin.log;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.biz.dal.dataobject.operationlog.OperationLogDO;
import cn.iocoder.yudao.module.biz.service.operation.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "后台-操作历史 ")
@RestController
@RequestMapping("biz/log")
public class OperationLogController {
    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/list")
    @Parameter(name = "id", description = "申请id")
    @ApiResponse
    @Operation(summary = "查询操作历史")
    public CommonResult<List<OperationLogDO>> list(@RequestParam("id") Long id) {
        return CommonResult.success(operationLogService.listByBusiness(id));
    }
}
