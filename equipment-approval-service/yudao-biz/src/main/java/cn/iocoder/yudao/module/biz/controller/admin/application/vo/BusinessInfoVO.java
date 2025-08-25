package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 业务信息视图对象（对应数据库查询字段）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class BusinessInfoVO {

    /**
     * 许可设备名称
     */
    private String licenseDeviceName;

    /**
     * 梯形图配置型号
     */
    private String ladderConfigModel;

    /**
     * 设备配置地址
     */
    private String equipmentConfigAddress;

    /**
     * 生产企业
     */
    private String productionEnterprise;

    /**
     * 具体型号
     */
    private String specificModel;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 安装日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date installationDate;

    /**
     * 采购价格
     */
    private String purchasePrice;

    /**
     * 特殊说明
     */
    private String specialDescription;
}
