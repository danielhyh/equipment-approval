package cn.iocoder.yudao.module.biz.controller.app.application;

import cn.iocoder.yudao.module.biz.controller.admin.application.vo.ApplicationPageReqVO;
import cn.iocoder.yudao.module.biz.controller.admin.application.vo.ApplicationPageRespVO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import cn.iocoder.yudao.module.biz.controller.app.application.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.service.application.ApplicationService;

@Tag(name = "用户 APP - 申请")
@RestController
@RequestMapping("/biz/application")
@Validated
public class AppApplicationController {

    @Resource
    private ApplicationService applicationService;

    @PostMapping("/create")
    @Operation(summary = "创建申请")
    public CommonResult<Long> createApplication(@Valid @RequestBody AppApplicationSaveReqVO createReqVO) {
        return success(applicationService.createApplication(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新申请")
    public CommonResult<Boolean> updateApplication(@Valid @RequestBody AppApplicationSaveReqVO updateReqVO) {
        applicationService.updateApplication(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除申请")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteApplication(@RequestParam("id") Long id) {
        applicationService.deleteApplication(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除申请")
    public CommonResult<Boolean> deleteApplicationList(@RequestParam("ids") List<Long> ids) {
        applicationService.deleteApplicationListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppApplicationRespVO> getApplication(@RequestParam("id") Long id) {
        ApplicationDO application = applicationService.getApplication(id);
        return success(BeanUtils.toBean(application, AppApplicationRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得申请分页",
            responses = {
                    @ApiResponse(content =
                    @Content(schema =
                    @Schema(implementation = AppApplicationRespVO.class)))
            })
    public CommonResult<PageResult<AppApplicationRespVO>> getApplicationPage(@Valid AppApplicationPageReqVO pageReqVO) {
        PageResult<ApplicationPageRespVO> pageResult = applicationService.getApplicationPage(BeanUtils.toBean(pageReqVO, ApplicationPageReqVO.class));
        return success(BeanUtils.toBean(pageResult, AppApplicationRespVO.class));
    }


}