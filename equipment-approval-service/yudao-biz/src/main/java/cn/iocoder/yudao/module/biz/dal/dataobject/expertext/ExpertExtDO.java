package cn.iocoder.yudao.module.biz.dal.dataobject.expertext;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 专家扩展信息 DO
 *
 * @author 芋道源码
 */
@TableName("biz_expert_ext")
@KeySequence("biz_expert_ext_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpertExtDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 关联用户ID
     */
    private Long userId;
    /**
     * 专家姓名
     */
    private String name;
    /**
     * 性别：1-男，2-女
     */
    private Integer gender;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 工作单位
     */
    private String unit;

    /**
     * 职务
     */
    private String position;
    /**
     * 职称
     */
    private String title;
    /**
     * 专业领域
     */
    private String specialty;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 资质证书
     */
    private String qualificationCert;
    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
