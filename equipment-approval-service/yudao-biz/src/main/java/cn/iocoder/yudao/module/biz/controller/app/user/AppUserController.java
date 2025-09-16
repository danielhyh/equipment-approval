package cn.iocoder.yudao.module.biz.controller.app.user;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.app.user.vo.UserInstitutionInfo;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import cn.iocoder.yudao.module.biz.dal.mysql.institutionext.InstitutionExtMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 用户信息")
@RestController
@RequestMapping("/biz/institution")
@Validated
public class AppUserController {

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private InstitutionExtMapper institutionExtMapper;

    @GetMapping("/getUserInfo")
    @Operation(summary = "根据用户id查询机构基本信息",
            responses = @ApiResponse(
                    content = @Content(
                            schema = @Schema(implementation = UserInstitutionInfo.class)
                    )
            )
    )
    public CommonResult<UserInstitutionInfo> getUserInfo() {
        String sql = """
                SELECT
                  nickname,
                  b1.institution_name,
                  b1.dept_id as institution_id,
                  b1.legal_person,
                  b1.unified_social_credit_code,
                  b1.detailed_address,
                  b1.ownership_nature,
                  b1.institution_type,
                  b2.institution_name as superior_institution,
                  b1.institution_level,
                  CONCAT('陕西省', b1.region) AS region,
                  b1.contact_person,
                  b1.contact_phone
                FROM
                  system_users su
                  LEFT JOIN biz_institution_ext b1 ON su.dept_id = b1.dept_id
                  INNER JOIN system_dept d1 ON b1.dept_id = d1.id
                  LEFT JOIN biz_institution_ext b2 ON d1.parent_id = b2.dept_id
                where su.id = ? and su.deleted = b'0' and b1.deleted = b'0'
                """;
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        if (loginUserId == null) {
            throw new ServiceException(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR);
        }
        UserInstitutionInfo single = jdbcClient.sql(sql).param(loginUserId).query(UserInstitutionInfo.class).single();
        return success(single);
    }


    @PostMapping("update")
    @Operation(summary = "修改机构基本信息")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
    public CommonResult<?> updateUserInfo(@RequestBody UserInstitutionInfo userInstitutionInfo) {
        LambdaUpdateWrapper<InstitutionExtDO> wrapper = Wrappers.lambdaUpdate(InstitutionExtDO.class)
                .set(userInstitutionInfo.getContactPerson() != null, InstitutionExtDO::getContactPerson, userInstitutionInfo.getContactPerson())
                .set(userInstitutionInfo.getContactPhone() != null, InstitutionExtDO::getContactPhone, userInstitutionInfo.getContactPhone())
                .set(userInstitutionInfo.getLegalPerson() != null, InstitutionExtDO::getLegalPerson, userInstitutionInfo.getLegalPerson())
                .set(userInstitutionInfo.getDetailedAddress() != null, InstitutionExtDO::getDetailedAddress, userInstitutionInfo.getDetailedAddress())
                .eq(InstitutionExtDO::getDeptId, userInstitutionInfo.getInstitutionId());
        institutionExtMapper.update(wrapper);
        return success(true);
    }

    @GetMapping("/test")
    @Operation(summary = "这是测试接口")
    public CommonResult<String> test() {
        return success("test2222");
    }
}
