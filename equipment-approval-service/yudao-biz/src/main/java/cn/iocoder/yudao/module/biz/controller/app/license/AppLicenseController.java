package cn.iocoder.yudao.module.biz.controller.app.license;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppDuplicateSubmitRequest;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppLicensePageRespVO;
import cn.iocoder.yudao.module.biz.service.license.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 许可证相关")
@RestController
@RequestMapping("/biz/license")
@Validated
public class AppLicenseController {

    @Resource
    private LicenseService licenseService;


    @GetMapping("/page")
    @Parameters({
            @Parameter(name = "pageSize", description = "每页数量"),
            @Parameter(name = "pageNum", description = "页码"),
            @Parameter(name = "type", description = "设备类型"),
    })
    @Operation(summary = "证书列表分页", responses = {
            @ApiResponse(content =
                @Content(schema = @Schema(implementation = AppLicensePageRespVO.class)))
    })
    public CommonResult<PageResult<AppLicensePageRespVO>> licensePage(@RequestParam("pageSize") Integer pageSize,
                                                                      @RequestParam("pageNum") Integer pageNum,
                                                                      @RequestParam(value = "type", required = false) String type) {
        return success(licenseService.licensePage(pageSize, pageNum, type));
    }


    @PostMapping("duplicate-submit")
    @Operation(summary = "副本提交")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> duplicateSubmit(@RequestBody @Valid AppDuplicateSubmitRequest request)  {
        return success(licenseService.insertDuplicateLicense(request));
    }
}
