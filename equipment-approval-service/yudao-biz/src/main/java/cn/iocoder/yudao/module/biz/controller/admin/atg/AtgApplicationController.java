package cn.iocoder.yudao.module.biz.controller.admin.atg;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationUpdateReqVO;
import cn.iocoder.yudao.module.biz.service.atg.AtgApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 高效通办系统申请管理
 */
@Tag(name = "管理后台 - 高效通办系统申请")
@RestController
@RequestMapping("/biz/atg-application")
@Validated
public class AtgApplicationController {

    @Resource
    private AtgApplicationService atgApplicationService;

    @PostMapping("/complete-and-generate")
    @Operation(summary = "补充设备信息并生成许可证（一步完成）")
    public CommonResult<String> completeAndGenerateLicense(@Valid @RequestBody AtgApplicationUpdateReqVO reqVO) {
        return success(atgApplicationService.completeAndGenerateLicense(reqVO));
    }

}
