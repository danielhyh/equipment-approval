package cn.iocoder.yudao.module.biz.dal.dataobject.application;

import cn.iocoder.yudao.framework.mybatis.core.type.LongListTypeHandler;
import cn.iocoder.yudao.framework.mybatis.core.type.StringListTypeHandler;
import lombok.*;

import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 申请 DO
 *
 * @author listen
 */
@TableName("biz_application")
@KeySequence("biz_application_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 申请编号
     */
    private String appNo;
    /**
     * 机构ID（关联dept_id）
     */
    private Long institutionId;
    /**
     * 申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更
     */
    private Integer appType;
    /**
     * 许可设备名称
     */
    private String licenseDeviceName;
    /**
     * 阶梯配置机型
     */
    private String ladderConfigModel;
    /**
     * 配置理由
     */
    private String configReason;
    /**
     * 申请状态：1-待初审，2-初审通过，3-初审不通过，4-待专家审核，5-专家审核通过，6-专家审核不通过，7-副本待审批，8-验收资料待审批，9-已完成
     */
    private Integer appStatus;
    /**
     * 初审结果：1-通过，0-不通过
     */
    private Integer initialReviewResult;
    /**
     * 初审意见
     */
    private String initialReviewOpinion;
    /**
     * 初审人ID
     */
    private Long initialReviewerId;
    /**
     * 初审时间
     */
    private LocalDateTime initialReviewTime;
    /**
     * 专家审核结果：1-通过，0-不通过
     */
    private Integer expertReviewResult;
    /**
     * 专家审核意见
     */
    private String expertReviewOpinion;
    /**
     * 专家ID
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> expertId;
    /**
     * 专家审核时间
     */
    private LocalDateTime expertReviewTime;

    /**
     * 专家审核附件
     */
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> expertAttachments;
    /**
     * 许可证编号
     */
    private String licenseNo;
    /**
     * 许可证有效期
     */
    private LocalDate licenseValidDate;

    //许可证生成时间
    private LocalDate licenseGenerateDate;

    /**
     * 截止日期
     */
    private LocalDate deadline;


}
