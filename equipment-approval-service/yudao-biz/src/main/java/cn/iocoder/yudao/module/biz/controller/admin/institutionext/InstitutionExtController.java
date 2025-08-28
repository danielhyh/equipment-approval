package cn.iocoder.yudao.module.biz.controller.admin.institutionext;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import cn.iocoder.yudao.module.biz.service.institutionext.InstitutionExtService;

@Tag(name = "管理后台 - 机构扩展信息")
@RestController
@RequestMapping("/biz/institution-ext")
@Validated
public class InstitutionExtController {

    @Resource
    private InstitutionExtService institutionExtService;

    @PostMapping("/create")
    @Operation(summary = "创建机构扩展信息")
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:create')")
    public CommonResult<Long> createInstitutionExt(@Valid @RequestBody InstitutionExtSaveReqVO createReqVO) {
        return success(institutionExtService.createInstitutionExt(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新机构扩展信息")
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:update')")
    public CommonResult<Boolean> updateInstitutionExt(@Valid @RequestBody InstitutionExtSaveReqVO updateReqVO) {
        institutionExtService.updateInstitutionExt(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除机构扩展信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:delete')")
    public CommonResult<Boolean> deleteInstitutionExt(@RequestParam("id") Long id) {
        institutionExtService.deleteInstitutionExt(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除机构扩展信息")
                @PreAuthorize("@ss.hasPermission('biz:institution-ext:delete')")
    public CommonResult<Boolean> deleteInstitutionExtList(@RequestParam("ids") List<Long> ids) {
        institutionExtService.deleteInstitutionExtListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得机构扩展信息",
    responses = @ApiResponse(
            content = @Content(
                    schema = @Schema(implementation = InstitutionExtDetailsVO.class)
            )
    ))
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:query')")
    public CommonResult<InstitutionExtDetailsVO> getInstitutionExt(@RequestParam("id") Long id) {
        InstitutionExtDetailsVO institutionExt = institutionExtService.getDetails(id);
        return success(institutionExt);
    }

    @GetMapping("/page")
    @Operation(summary = "获得机构扩展信息分页",
            responses = @ApiResponse(
                    content = @Content(
                            schema = @Schema(implementation = InstitutionExtRespVO.class)
                    )
            ))
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:query')")
    public CommonResult<PageResult<InstitutionExtRespVO>> getInstitutionExtPage(@Valid InstitutionExtPageReqVO pageReqVO) {
        PageResult<InstitutionExtDO> pageResult = institutionExtService.getInstitutionExtPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InstitutionExtRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出机构扩展信息 Excel")
    @PreAuthorize("@ss.hasPermission('biz:institution-ext:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInstitutionExtExcel(@Valid InstitutionExtPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InstitutionExtDO> list = institutionExtService.getInstitutionExtPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "机构扩展信息.xls", "数据", InstitutionExtRespVO.class,
                        BeanUtils.toBean(list, InstitutionExtRespVO.class));
    }

}