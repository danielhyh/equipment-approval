package cn.iocoder.yudao.module.biz.dal.dataobject.equipmentmanufacturer;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备生产企业 DO
 *
 * @author 芋道源码
 */
@TableName("biz_equipment_manufacturer")
@KeySequence("biz_equipment_manufacturer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentManufacturerDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司编码
     */
    private String companyCode;
    /**
     * 法定代表人
     */
    private String legalPerson;
    /**
     * 联系人
     */
    private String contactPerson;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 公司地址
     */
    private String address;
    /**
     * 资质证书
     */
    private String qualificationCert;
    /**
     * 主要设备类型
     */
    private String mainEquipmentType;
    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;


}