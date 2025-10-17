package cn.iocoder.yudao.module.biz.controller.admin.acceptancematerial;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.AppAcceptanceMaterialRespVO;
import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.AppAcceptanceMaterialSaveReqVO;
import cn.iocoder.yudao.module.biz.service.acceptancematerial.AcceptanceMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理端 - 验收资料")
@RestController
@RequestMapping("/biz/acceptance-material")
@Validated
public class AcceptanceMaterialController {

    @Resource
    private AcceptanceMaterialService acceptanceMaterialService;

    @PostMapping("/create")
    @Operation(summary = "创建验收资料")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> createAcceptanceMaterial(@Valid @RequestBody List<AppAcceptanceMaterialSaveReqVO> createReqVO) {
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
    public CommonResult<List<AppAcceptanceMaterialRespVO>> list(@RequestParam(value = "applicationId") Long id) {
        return success(BeanUtils.toBean(acceptanceMaterialService.list(id), AppAcceptanceMaterialRespVO.class));
    }


}