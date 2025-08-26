package cn.iocoder.yudao.module.biz.controller.admin.expertext;

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

import cn.iocoder.yudao.module.biz.controller.admin.expertext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.expertext.ExpertExtDO;
import cn.iocoder.yudao.module.biz.service.expertext.ExpertExtService;

@Tag(name = "管理后台 - 专家扩展信息")
@RestController
@RequestMapping("/biz/expert-ext")
@Validated
public class ExpertExtController {

    @Resource
    private ExpertExtService expertExtService;

    @PostMapping("/create")
    @Operation(summary = "创建专家扩展信息")
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:create')")
    public CommonResult<Long> createExpertExt(@Valid @RequestBody ExpertExtSaveReqVO createReqVO) {
        return success(expertExtService.createExpertExt(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新专家扩展信息")
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:update')")
    public CommonResult<Boolean> updateExpertExt(@Valid @RequestBody ExpertExtSaveReqVO updateReqVO) {
        expertExtService.updateExpertExt(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除专家扩展信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:delete')")
    public CommonResult<Boolean> deleteExpertExt(@RequestParam("id") Long id) {
        expertExtService.deleteExpertExt(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除专家扩展信息")
                @PreAuthorize("@ss.hasPermission('biz:expert-ext:delete')")
    public CommonResult<Boolean> deleteExpertExtList(@RequestParam("ids") List<Long> ids) {
        expertExtService.deleteExpertExtListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得专家扩展信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:query')")
    public CommonResult<ExpertExtRespVO> getExpertExt(@RequestParam("id") Long id) {
        ExpertExtDO expertExt = expertExtService.getExpertExt(id);
        return success(BeanUtils.toBean(expertExt, ExpertExtRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得专家扩展信息分页")
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:query')")
    public CommonResult<PageResult<ExpertExtRespVO>> getExpertExtPage(@Valid ExpertExtPageReqVO pageReqVO) {
        PageResult<ExpertExtRespVO> pageResult = expertExtService.getExpertExtPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ExpertExtRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出专家扩展信息 Excel")
    @PreAuthorize("@ss.hasPermission('biz:expert-ext:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExpertExtExcel(@Valid ExpertExtPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ExpertExtRespVO> list = expertExtService.getExpertExtPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "专家扩展信息.xls", "数据", ExpertExtRespVO.class,
                        BeanUtils.toBean(list, ExpertExtRespVO.class));
    }

    @GetMapping("/list")
    public CommonResult<List<ExpertExtRespVO>> getExpertExtList(@RequestParam(required = false, name = "keywords") String keywords,
                                                                @RequestParam(required = false, name = "specialty")String specialty) {
        return success(expertExtService.list(keywords,  specialty));
    }

    @GetMapping("/getSpecialty")
    public CommonResult<List<String >> getSpecialty() {
        return success(expertExtService.getSpecialty());
    }

    @GetMapping("/expertReviewRecord/{id}")
    public CommonResult<List<ExpertReviewVO>> getExpertReviewRecord(@PathVariable Long id) {
        return success(expertExtService.selectReviewRecord(id));
    }

}