package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 应用基本信息视图对象（VO）
 */
@Data
public class ApplicationBasicInformationVO {

    private Long id;
    /**
     * 应用编号
     */
    private String appNo;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 设备名称
     */
    private String licenseDeviceName;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

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
     * 所属性质（所有制性质）
     */
    private String ownershipNature;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 详细地址
     */
    private String detailedAddress;
}
