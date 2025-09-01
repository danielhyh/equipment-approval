package cn.iocoder.yudao.module.biz.controller.app.user.applicationmaterial;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.biz.controller.app.user.applicationmaterial.vo.MaterialInsertRequest;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import cn.iocoder.yudao.module.biz.dal.mysql.applicationmaterial.ApplicationMaterialMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<ApplicationMaterialDO> bean = BeanUtils.toBean(requests, ApplicationMaterialDO.class);
        applicationMaterialMapper.insertBatch(bean);
        return success(true);
    }
}
