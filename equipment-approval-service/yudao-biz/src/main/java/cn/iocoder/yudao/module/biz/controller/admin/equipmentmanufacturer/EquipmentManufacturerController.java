package cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer;

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

import cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.equipmentmanufacturer.EquipmentManufacturerDO;
import cn.iocoder.yudao.module.biz.service.equipmentmanufacturer.EquipmentManufacturerService;

@Tag(name = "管理后台 - 设备生产企业")
@RestController
@RequestMapping("/biz/equipment-manufacturer")
@Validated
public class EquipmentManufacturerController {

    @Resource
    private EquipmentManufacturerService equipmentManufacturerService;

    @PostMapping("/create")
    @Operation(summary = "创建设备生产企业")
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:create')")
    public CommonResult<Long> createEquipmentManufacturer(@Valid @RequestBody EquipmentManufacturerSaveReqVO createReqVO) {
        return success(equipmentManufacturerService.createEquipmentManufacturer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备生产企业")
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:update')")
    public CommonResult<Boolean> updateEquipmentManufacturer(@Valid @RequestBody EquipmentManufacturerSaveReqVO updateReqVO) {
        equipmentManufacturerService.updateEquipmentManufacturer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备生产企业")
    @Parameter(name = "id", description = "编号", required = true)
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:delete')")
    public CommonResult<Boolean> deleteEquipmentManufacturer(@RequestParam("id") Long id) {
        equipmentManufacturerService.deleteEquipmentManufacturer(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除设备生产企业")
                //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:delete')")
    public CommonResult<Boolean> deleteEquipmentManufacturerList(@RequestParam("ids") List<Long> ids) {
        equipmentManufacturerService.deleteEquipmentManufacturerListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备生产企业",
            responses =
                    {@ApiResponse(content =
                    @Content(schema =
                    @Schema(implementation = EquipmentManufacturerRespVO.class)))}
    )
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:query')")
    public CommonResult<EquipmentManufacturerRespVO> getEquipmentManufacturer(@RequestParam("id") Long id) {
        EquipmentManufacturerDO equipmentManufacturer = equipmentManufacturerService.getEquipmentManufacturer(id);
        return success(BeanUtils.toBean(equipmentManufacturer, EquipmentManufacturerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备生产企业分页", responses =
            {@ApiResponse(content =
            @Content(schema =
            @Schema(implementation = EquipmentManufacturerRespVO.class)))})
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:query')")
    public CommonResult<PageResult<EquipmentManufacturerRespVO>> getEquipmentManufacturerPage(@Valid EquipmentManufacturerPageReqVO pageReqVO) {
        PageResult<EquipmentManufacturerDO> pageResult = equipmentManufacturerService.getEquipmentManufacturerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EquipmentManufacturerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备生产企业 Excel")
    //@PreAuthorize("@ss.hasPermission('biz:equipment-manufacturer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEquipmentManufacturerExcel(@Valid EquipmentManufacturerPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EquipmentManufacturerDO> list = equipmentManufacturerService.getEquipmentManufacturerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备生产企业.xls", "数据", EquipmentManufacturerRespVO.class, BeanUtils.toBean(list, EquipmentManufacturerRespVO.class));
    }

}