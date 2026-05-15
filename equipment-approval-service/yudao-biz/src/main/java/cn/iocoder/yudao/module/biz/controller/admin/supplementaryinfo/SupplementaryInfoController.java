package cn.iocoder.yudao.module.biz.controller.admin.supplementaryinfo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo.AppSupplementaryInfoRespVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo.SupplementaryInfoDO;
import cn.iocoder.yudao.module.biz.service.supplementaryinfo.SupplementaryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 补充信息")
@RestController
@RequestMapping("/biz/supplementary-info")
@Validated
public class SupplementaryInfoController {

    @Resource
    private SupplementaryInfoService supplementaryInfoService;

    @GetMapping("/list")
    @Operation(summary = "获取补充信息列表")
    @Parameter(name = "applicationId", description = "申请id", required = true)
    public CommonResult<List<AppSupplementaryInfoRespVO>> list(@RequestParam("applicationId") Long id) {
        List<SupplementaryInfoDO> list = supplementaryInfoService.listAllByApplicationId(id);
        return success(BeanUtils.toBean(list, AppSupplementaryInfoRespVO.class));
    }
}
