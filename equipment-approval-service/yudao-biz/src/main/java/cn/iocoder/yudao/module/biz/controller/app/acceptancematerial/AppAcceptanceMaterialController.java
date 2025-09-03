package cn.iocoder.yudao.module.biz.controller.app.acceptancematerial;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import cn.iocoder.yudao.module.biz.service.acceptancematerial.AcceptanceMaterialService;

@Tag(name = "用户 APP - 验收资料")
@RestController
@RequestMapping("/biz/acceptance-material")
@Validated
public class AppAcceptanceMaterialController {

    @Resource
    private AcceptanceMaterialService acceptanceMaterialService;

    @PostMapping("/create")
    @Operation(summary = "创建验收资料")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Long> createAcceptanceMaterial(@Valid @RequestBody AppAcceptanceMaterialSaveReqVO createReqVO) {
        return success(acceptanceMaterialService.createAcceptanceMaterial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新验收资料")
    public CommonResult<Boolean> updateAcceptanceMaterial(@Valid @RequestBody AppAcceptanceMaterialSaveReqVO updateReqVO) {
        acceptanceMaterialService.updateAcceptanceMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除验收资料")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteAcceptanceMaterial(@RequestParam("id") Long id) {
        acceptanceMaterialService.deleteAcceptanceMaterial(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "查询验收资料列表")
    @ApiResponse
    public CommonResult<List<AppAcceptanceMaterialRespVO>> list(@RequestParam(value = "type", required = false) Integer type) {
        return success(BeanUtils.toBean(acceptanceMaterialService.list(type), AppAcceptanceMaterialRespVO.class));
    }


}