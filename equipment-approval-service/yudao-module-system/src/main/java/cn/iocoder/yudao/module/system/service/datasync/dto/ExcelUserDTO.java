package cn.iocoder.yudao.module.system.service.datasync.dto;

import cn.idev.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.module.system.service.datasync.converter.BooleanConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = false)
public class ExcelUserDTO {

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    @ExcelProperty(index = 0)
    private String userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @ExcelProperty(index = 1)
    private String caption;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @ExcelProperty(index = 2)
    private String password;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @ExcelProperty(index = 3, converter = BooleanConverter.class)
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
    @ExcelProperty(index = 9)
    private String orgId;

    /**
     * 行政区划
     */
    @Schema(description = "行政区划")
    private String xzqx;

    @Schema(description = "操作类型 create/update/delete")
    private final String operation = "create";

    @Schema(description = "有效期起")
    private LocalDate fromDate;

    @Schema(description = "有效期止")
    private LocalDate toDate;
}
