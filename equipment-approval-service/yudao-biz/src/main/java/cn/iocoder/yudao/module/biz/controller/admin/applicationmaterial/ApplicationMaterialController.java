package cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
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

import cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import cn.iocoder.yudao.module.biz.service.applicationmaterial.ApplicationMaterialService;

@Tag(name = "管理后台 - 申请资料")
@RestController
@RequestMapping("/biz/application-material")
@Validated
public class ApplicationMaterialController {

    @Resource
    private ApplicationMaterialService applicationMaterialService;

    @PostMapping("/create")
    @Operation(summary = "创建申请资料")
    @PreAuthorize("@ss.hasPermission('biz:application-material:create')")
    public CommonResult<Long> createApplicationMaterial(@Valid @RequestBody ApplicationMaterialSaveReqVO createReqVO) {
        return success(applicationMaterialService.createApplicationMaterial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新申请资料")
    @PreAuthorize("@ss.hasPermission('biz:application-material:update')")
    public CommonResult<Boolean> updateApplicationMaterial(@Valid @RequestBody ApplicationMaterialSaveReqVO updateReqVO) {
        applicationMaterialService.updateApplicationMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除申请资料")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:application-material:delete')")
    public CommonResult<Boolean> deleteApplicationMaterial(@RequestParam("id") Long id) {
        applicationMaterialService.deleteApplicationMaterial(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除申请资料")
                @PreAuthorize("@ss.hasPermission('biz:application-material:delete')")
    public CommonResult<Boolean> deleteApplicationMaterialList(@RequestParam("ids") List<Long> ids) {
        applicationMaterialService.deleteApplicationMaterialListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得申请资料")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:application-material:query')")
    public CommonResult<ApplicationMaterialRespVO> getApplicationMaterial(@RequestParam("id") Long id) {
        ApplicationMaterialDO applicationMaterial = applicationMaterialService.getApplicationMaterial(id);
        return success(BeanUtils.toBean(applicationMaterial, ApplicationMaterialRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得申请资料分页")
    @PreAuthorize("@ss.hasPermission('biz:application-material:query')")
    public CommonResult<PageResult<ApplicationMaterialRespVO>> getApplicationMaterialPage(@Valid ApplicationMaterialPageReqVO pageReqVO) {
        PageResult<ApplicationMaterialDO> pageResult = applicationMaterialService.getApplicationMaterialPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ApplicationMaterialRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出申请资料 Excel")
    @PreAuthorize("@ss.hasPermission('biz:application-material:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportApplicationMaterialExcel(@Valid ApplicationMaterialPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ApplicationMaterialDO> list = applicationMaterialService.getApplicationMaterialPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "申请资料.xls", "数据", ApplicationMaterialRespVO.class,
                        BeanUtils.toBean(list, ApplicationMaterialRespVO.class));
    }

    @GetMapping("/list")
    public CommonResult<List<ApplicationMaterialRespVO>> getApplicationMaterialList(@RequestParam("id") Long id) {
        return success(BeanUtils.toBean(applicationMaterialService.getApplicationMaterialList(id), ApplicationMaterialRespVO.class));
    }


}