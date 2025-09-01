package cn.iocoder.yudao.module.biz.controller.admin.license;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.DuplicateLicenseVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageRequestVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.OriginalLicenseVO;
import cn.iocoder.yudao.module.biz.service.license.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理后台 - 许可证中心")
@RestController
@RequestMapping("/biz/license")
public class LicenseController {

    @Resource
    private LicenseService licenseService;

    @Operation(summary = "许可证中心分页查询",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(implementation = LicensePageVO.class))
            ))
    @GetMapping("/page")
    public CommonResult<PageResult<LicensePageVO>> page(LicensePageRequestVO vo) {
        return CommonResult.success(licenseService.page(vo));
    }

    @Operation(summary = "许可证中心正本查询",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(implementation = OriginalLicenseVO.class))
            ))
    @Parameter(required = true, name = "id", description = "正本id")
    @GetMapping("/getOriginalById")
    public CommonResult<OriginalLicenseVO> getOriginalById(@RequestParam("id") Long id) {
        return CommonResult.success(licenseService.getOriginalById(id));
    }

    @Operation(summary = "许可证中心副本查询",
            responses = @ApiResponse(
                    content = @Content(schema = @Schema(implementation = DuplicateLicenseVO.class))
            ))
    @Parameter(required = true, name = "id", description = "副本id")
    @GetMapping("/getDuplicateById")
    public CommonResult<DuplicateLicenseVO> getDuplicateById(@RequestParam("id") Long id) {
        return CommonResult.success(licenseService.getDuplicateById(id));
    }

}
