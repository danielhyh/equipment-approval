package cn.iocoder.yudao.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
/**
 * 外部用户信息表实体类
 */
@Data
@Accessors(chain = true) // 支持链式写法
@EqualsAndHashCode(callSuper = false)
@TableName("system_external_user")
public class ExternalUser {

    /**
     * 用户id（外部系统唯一标识）
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 用户名
     */
    private String caption;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 状态：true-启用，false-禁用
     */
    private Boolean enable;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String tel;

    /**
     * 部门id（外部系统的部门标识）
     */
    private String orgId;

    /**
     * 行政区划代码（如：110101 表示东城区）
     */
    private String xzqx;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
