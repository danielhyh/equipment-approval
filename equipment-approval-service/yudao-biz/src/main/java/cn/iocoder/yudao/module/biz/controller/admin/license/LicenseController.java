package cn.iocoder.yudao.module.biz.controller.admin.license;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.controller.admin.application.vo.ApprovalDetailsVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.*;
import cn.iocoder.yudao.module.biz.service.license.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/offline-process")
    @Operation(summary = "线下办理许可证")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> offlineProcess(@RequestBody OfflineLicenseInsertRequest req) {
        licenseService.offlineProcessLicense(req);
        return CommonResult.success(true);
    }

    @GetMapping("/get-license")
    @Operation(summary = "查询正本和副本")
    @ApiResponse
    @Parameters({
            @Parameter(required = true, name = "oid", description = "正本id"),
            @Parameter(required = true, name = "did", description = "副本id")
    })
    public CommonResult<OfflineLicenseInsertRequest> getLicense(@RequestParam("oid") Long id, @RequestParam(value = "did", required = false) Long did) {
        OfflineLicenseInsertRequest req = new OfflineLicenseInsertRequest();
        req.setOriginalId(id);
        req.setDuplicateId(did);
        req.setOriginalLicense(licenseService.getOriginalById(id));
        req.setDuplicateLicense(licenseService.getDuplicateById(did));
        return CommonResult.success(req);
    }


    @PostMapping("/update-original")
    @Operation(summary = "修改正本")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> updateOriginal(@RequestBody OriginalLicenseVO req) {
        return CommonResult.success(licenseService.updateOriginal(req));
    }

    @PostMapping("/update-duplicate")
    @Operation(summary = "修改副本")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Boolean> updateDuplicate(@RequestBody DuplicateLicenseVO req) {
        return CommonResult.success(licenseService.updateDuplicate(req));
    }

    @GetMapping("/approval-details")
    @Operation(summary = "设备验收审核回显")
    @ApiResponse
    public CommonResult<DuplicateApprovalDetails> approvalDetails(@RequestParam("id")  Long id) {
        return CommonResult.success(licenseService.approvalDetails(id));
    }

    @PostMapping("/approval")
    @Operation(summary = "设备验收审核")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<Void> approval(@Validated @RequestBody DuplicateApprovalRequest req) {
        licenseService.approval(req);
        return CommonResult.success(null);
    }


}
