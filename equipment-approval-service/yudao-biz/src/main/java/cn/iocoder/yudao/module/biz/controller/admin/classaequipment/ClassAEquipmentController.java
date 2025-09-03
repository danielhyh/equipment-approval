package cn.iocoder.yudao.module.biz.controller.admin.classaequipment;

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

import cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.module.biz.service.classaequipment.ClassAEquipmentService;

@Tag(name = "管理后台 - 甲类设备")
@RestController
@RequestMapping("/biz/class-A-equipment")
@Validated
public class ClassAEquipmentController {

    @Resource
    private ClassAEquipmentService classAEquipmentService;

    @PostMapping("/create")
    @Operation(summary = "创建甲类设备")
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:create')")
    public CommonResult<Long> createClassAEquipment(@Valid @RequestBody ClassAEquipmentSaveReqVO createReqVO) {
        return success(classAEquipmentService.createClassAEquipment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新甲类设备")
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:update')")
    public CommonResult<Boolean> updateClassAEquipment(@Valid @RequestBody ClassAEquipmentSaveReqVO updateReqVO) {
        classAEquipmentService.updateClassAEquipment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除甲类设备")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:delete')")
    public CommonResult<Boolean> deleteClassAEquipment(@RequestParam("id") Long id) {
        classAEquipmentService.deleteClassAEquipment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除甲类设备")
                @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:delete')")
    public CommonResult<Boolean> deleteClassAEquipmentList(@RequestParam("ids") List<Long> ids) {
        classAEquipmentService.deleteClassAEquipmentListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得甲类设备")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:query')")
    public CommonResult<ClassAEquipmentRespVO> getClassAEquipment(@RequestParam("id") Long id) {
        ClassAEquipmentDO classAEquipment = classAEquipmentService.getClassAEquipment(id);
        return success(BeanUtils.toBean(classAEquipment, ClassAEquipmentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得甲类设备分页")
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:query')")
    public CommonResult<PageResult<ClassAEquipmentRespVO>> getClassAEquipmentPage(@Valid ClassAEquipmentPageReqVO pageReqVO) {
        PageResult<ClassAEquipmentDO> pageResult = classAEquipmentService.getClassAEquipmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ClassAEquipmentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出甲类设备 Excel")
    @PreAuthorize("@ss.hasPermission('biz:class-A-equipment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportClassAEquipmentExcel(@Valid ClassAEquipmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ClassAEquipmentDO> list = classAEquipmentService.getClassAEquipmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "甲类设备.xls", "数据", ClassAEquipmentRespVO.class,
                        BeanUtils.toBean(list, ClassAEquipmentRespVO.class));
    }

}