package cn.iocoder.yudao.module.biz.dal.dataobject.institutionext;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 机构扩展信息 DO
 *
 * @author Listen
 */
@TableName("biz_institution_ext")
@KeySequence("biz_institution_ext_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionExtDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 关联部门ID
     */
    private Long deptId;
    /**
     * 机构名称
     */
    private String institutionName;
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    /**
     * 机构级别
     */
    private String institutionLevel;
    /**
     * 所有制性质
     */
    private String ownershipNature;
    /**
     * 所属区域
     */
    private String region;
    /**
     * 执业许可证号
     */
    private String licenseNo;
    /**
     * 设备数量
     */
    private Integer deviceNum;
    /**
     * 营业执照号
     */
    private String businessLicenseNo;
    /**
     * 法定代表人
     */
    private String legalPerson;
    /**
     * 机构地址
     */
    private String address;
    /**
     * 详细地址
     */
    private String detailedAddress;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 机构性质：1-社会办医，2-筹建或在建，3-自贸区内社会办医，4-筹建或在建自贸区
     */
    private Integer institutionType;


}