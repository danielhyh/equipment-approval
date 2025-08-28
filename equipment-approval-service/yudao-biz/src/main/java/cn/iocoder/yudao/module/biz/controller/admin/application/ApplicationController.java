package cn.iocoder.yudao.module.biz.controller.admin.application;

import cn.iocoder.yudao.module.biz.controller.admin.license.vo.DuplicateLicenseVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.OriginalLicenseVO;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
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

import cn.iocoder.yudao.module.biz.controller.admin.application.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.service.application.ApplicationService;

@Tag(name = "管理后台 - 申请")
@RestController
@RequestMapping("/biz/application")
@Validated
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcClient jdbcClient;

    @PostMapping("/create")
    @Operation(summary = "创建申请")
    @PreAuthorize("@ss.hasPermission('biz:application:create')")
    public CommonResult<Long> createApplication(@Valid @RequestBody ApplicationSaveReqVO createReqVO) {
        return success(applicationService.createApplication(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新申请")
    @PreAuthorize("@ss.hasPermission('biz:application:update')")
    public CommonResult<Boolean> updateApplication(@Valid @RequestBody ApplicationSaveReqVO updateReqVO) {
        applicationService.updateApplication(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除申请")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:application:delete')")
    public CommonResult<Boolean> deleteApplication(@RequestParam("id") Long id) {
        applicationService.deleteApplication(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除申请")
                @PreAuthorize("@ss.hasPermission('biz:application:delete')")
    public CommonResult<Boolean> deleteApplicationList(@RequestParam("ids") List<Long> ids) {
        applicationService.deleteApplicationListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:application:query')")
    public CommonResult<ApplicationRespVO> getApplication(@RequestParam("id") Long id) {
        ApplicationDO application = applicationService.getApplication(id);
        return success(BeanUtils.toBean(application, ApplicationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得申请分页")
    @PreAuthorize("@ss.hasPermission('biz:application:query')")
    public CommonResult<PageResult<ApplicationPageRespVO>> getApplicationPage(@Valid ApplicationPageReqVO pageReqVO) {
        return success(applicationService.getApplicationPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出申请 Excel")
    @PreAuthorize("@ss.hasPermission('biz:application:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportApplicationExcel(@Valid ApplicationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ApplicationPageRespVO> list = applicationService.getApplicationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "申请.xls", "数据", ApplicationRespVO.class,
                        BeanUtils.toBean(list, ApplicationRespVO.class));
    }

    @GetMapping("/basicInfo/{id}")
    public CommonResult<ApplicationBasicInformationVO> getApplicationBasicInformation(@PathVariable("id") Long id) {
        return success(applicationService.getApplicationBasicInformation(id));
    }

    @GetMapping("/bizInfo/{id}")
    public CommonResult<BusinessInfoVO> getBusinessInfo(@PathVariable("id") Long id) {
        return success(applicationService.businessInfo(id));
    }

    @PostMapping("/approval")
    public CommonResult<Void> approval(@RequestBody ApplicationReviewVO vo) {
        applicationService.approval(vo);
        return CommonResult.success(null);
    }

    @GetMapping("/generateLicense/{id}")
    public  CommonResult<String> generateLicense(@PathVariable("id") Long id) {
        return success(applicationService.getLicenseNumber(id));
    }

    @GetMapping("/approvalDetails")
    public CommonResult<ApprovalDetailsVO>  getApprovalDetails(@RequestParam("id") Long id) {
        return success(applicationService.approvalDetails(id));
    }

    @GetMapping("/getOriginal")
    public CommonResult<OriginalLicenseVO> getOriginal(@RequestParam("id") Long id) {
        String sql = """
                SELECT
                            config_unit_name,
                            unified_social_credit_code,
                            legal_person,
                            license_device_name,
                            ownership_nature,
                            ladder_config_model,
                            equipment_config_address,
                            detailed_address,
                            issuing_authority,
                            issue_date
                        from biz_license_original a
                        left join biz_application b on a.application_id = b.id
                        where b.id = ?
                """;
        OriginalLicenseVO single = jdbcClient.sql(sql).param(id).query(OriginalLicenseVO.class).single();
        return success(single);
    }

    @GetMapping("/getDuplicate")
    public CommonResult<DuplicateLicenseVO> getDuplicate(@RequestParam("id") Long id) {
        String sql = """
                SELECT
                            production_enterprise,
                            specific_model,
                            product_serial_no,
                            installation_date,
                            info_submit_date,
                            duplicate_issuing_authority,
                            duplicate_issue_date,
                            remark
                        FROM
                            biz_license_duplicate a
                left join biz_license_original b on a.original_id = b.id
                left join biz_application c on b.application_id = c.id
                where c.id = ?
                """;
        DuplicateLicenseVO single = jdbcClient.sql(sql).param(id).query(DuplicateLicenseVO.class).single();
        return success(single);
    }

}