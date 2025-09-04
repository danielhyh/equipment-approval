package cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 甲类设备 DO
 *
 * @author 芋道源码
 */
@TableName("biz_class_a_equipment")
@KeySequence("biz_class_a_equipment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassAEquipmentDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 配置单位名称
     */
    private String configUnitName;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 生产企业
     */
    private String productionEnterprise;
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 具体型号
     */
    private String specificModel;
    /**
     * 法定代表人(或主要负责人)
     */
    private String legalPerson;
    /**
     * 许可设备名称
     */
    private String licenseDeviceName;
    /**
     * 装机日期
     */
    private LocalDate installationDate;
    /**
     * 所有制性质
     */
    private String ownershipNature;
    /**
     * 设备配置地址
     */
    private String equipmentConfigAddress;
    /**
     * 采购价格(元)
     */
    private BigDecimal purchasePrice;
    /**
     * 设备特殊说明
     */
    private String specialDescription;
    /**
     * 设备使用人员JSON(包含:身份证号码,姓名,性别,出生日期,职称,联系电话)
     */
    private String equipmentUsers;
    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;


}
