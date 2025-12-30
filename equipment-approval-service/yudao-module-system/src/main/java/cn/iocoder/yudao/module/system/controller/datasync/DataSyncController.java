package cn.iocoder.yudao.module.system.controller.datasync;

import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.service.datasync.DataSyncService;
import cn.iocoder.yudao.module.system.service.datasync.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RequestMapping("/api/external/sync")
@RestController
@Tag(name = "数据同步")
@PermitAll
@Slf4j
public class DataSyncController {

    @Resource
    private DataSyncService dataSyncService;


    @PostMapping("/user")
    @Operation(summary = "同步用户")
    @PermitAll
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<PushResultVO> receiveUser(
            @Validated @RequestBody SyncUserDTO dto,
            @RequestHeader("X-API-Key") String apiKey) {

        // 验证API密钥
        validateApiKey(apiKey);

        PushResultVO result = dataSyncService.syncUser(dto);
        return success(result);
    }

    @PostMapping("/dept")
    @Operation(summary = "同步部门")
    @PermitAll
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<PushResultVO> receiveDept(
            @Validated @RequestBody SyncDeptDTO dto,
            @RequestHeader("X-API-Key") String apiKey) {

        validateApiKey(apiKey);

        //默认客户端
        PushResultVO result = dataSyncService.syncDept(dto, UserTypeEnum.MEMBER);
        return success(result);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量同步")
    @PermitAll
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<BatchPushResultVO> receiveBatch(
            @Validated @RequestBody ExternalBatchPushDTO dto,
            @RequestHeader("X-API-Key") String apiKey) {

        validateApiKey(apiKey);

        BatchPushResultVO result = dataSyncService.batchSync(dto);
        return success(result);
    }


    /**
     * 验证API密钥（简单实现）
     */
    private void validateApiKey(String apiKey) {
        // TODO: 从配置或数据库读取合法的API Key
        String validApiKey = "71009cdd-a039-4d64-b6f2-bca514704e71";
        if (!validApiKey.equals(apiKey)) {
            throw new ServiceException(1111, "API密钥验证失败");
        }
    }
}
