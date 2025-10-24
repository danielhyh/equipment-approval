package cn.iocoder.yudao.module.system.service.datasync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SyncUserDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @NotBlank(message = "外部用户ID不能为空")
    private String userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String caption;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean enable;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机
     */
    @Schema(description = "手机")
    private String tel;

    /**
     * 部门id
     */
    @Schema(description = "部门id")
    private String orgId;

    /**
     * 行政区划
     */
    @Schema(description = "行政区划")
    private String xzqx;

}
