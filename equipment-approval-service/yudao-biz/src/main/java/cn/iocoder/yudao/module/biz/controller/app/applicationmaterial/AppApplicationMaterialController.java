package cn.iocoder.yudao.module.biz.controller.app.applicationmaterial;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.app.applicationmaterial.vo.MaterialInsertRequest;
import cn.iocoder.yudao.module.biz.controller.app.applicationmaterial.vo.MaterialResponse;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import cn.iocoder.yudao.module.biz.dal.mysql.applicationmaterial.ApplicationMaterialMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 申报材料")
@RestController
@RequestMapping("/biz/appMaterial")
@Validated
public class AppApplicationMaterialController {

    @Resource
    private ApplicationMaterialMapper applicationMaterialMapper;

    @PostMapping("/insert")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<?> insert(@RequestBody @Valid @NotEmpty
            @ArraySchema(schema = @Schema(implementation = MaterialInsertRequest.class))
                                      List<MaterialInsertRequest> requests) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        }
        List<ApplicationMaterialDO> bean = BeanUtils.toBean(requests, ApplicationMaterialDO.class,
                applicationMaterialDO -> applicationMaterialDO.setCreator(loginUserId.toString()));
        applicationMaterialMapper.insertBatch(bean);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "用户申报材料查询")
    @ApiResponses
    public CommonResult<List<MaterialResponse>> list() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        }
        List<ApplicationMaterialDO> list = applicationMaterialMapper.selectList("creator", loginUserId);
        return success(BeanUtils.toBean(list, MaterialResponse.class));
    }
}
