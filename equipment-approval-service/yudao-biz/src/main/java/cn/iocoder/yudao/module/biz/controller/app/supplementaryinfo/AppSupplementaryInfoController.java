package cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo;

import io.swagger.v3.oas.annotations.Parameters;
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

import cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo.SupplementaryInfoDO;
import cn.iocoder.yudao.module.biz.service.supplementaryinfo.SupplementaryInfoService;

@Tag(name = "用户 APP - 补充信息")
@RestController
@RequestMapping("/biz/supplementary-info")
@Validated
public class AppSupplementaryInfoController {

    @Resource
    private SupplementaryInfoService supplementaryInfoService;

    @PostMapping("/create")
    @Operation(summary = "创建补充信息")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> createSupplementaryInfo(@Valid @RequestBody List<AppSupplementaryInfoSaveReqVO> createReqVO) {
        return success(supplementaryInfoService.createSupplementaryInfo(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新补充信息")
    public CommonResult<Boolean> updateSupplementaryInfo(@Valid @RequestBody AppSupplementaryInfoSaveReqVO updateReqVO) {
        supplementaryInfoService.updateSupplementaryInfo(updateReqVO);
        return success(true);
    }
    @DeleteMapping("/delete")
    @Operation(summary = "删除补充信息")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteSupplementaryInfo(@RequestParam("id") Long id) {
        supplementaryInfoService.deleteSupplementaryInfo(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取补充信息列表")
    @Parameters(
            @Parameter(name = "applicationId", description = "申请id")
    )
    @ApiResponse
    public CommonResult<List<AppSupplementaryInfoRespVO>> list( @RequestParam("applicationId") Long id) {
        List<SupplementaryInfoDO> list = supplementaryInfoService.list( id);
        return success(BeanUtils.toBean(list, AppSupplementaryInfoRespVO.class));
    }
}