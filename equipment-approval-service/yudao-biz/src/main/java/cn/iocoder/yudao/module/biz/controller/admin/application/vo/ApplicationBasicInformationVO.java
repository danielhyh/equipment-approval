package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "应用编号")
    private String appNo;

    /**
     * 机构名称
     */
    @Schema(description = "机构名称")
    private String institutionName;

    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String licenseDeviceName;

    /**
     * 统一社会信用代码
     */
    @Schema(description = "统一社会信用代码")
    private String unifiedSocialCreditCode;

    /**
     * 法定代表人
     */
    @Schema(description = "法定代表人")
    private String legalPerson;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactPhone;

    /**
     * 所属性质（所有制性质）
     */
    @Schema(description = "所有制性质")
    private String ownershipNature;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    private String detailedAddress;

    @Schema(description = "机构名称")
    private String region;

    @Schema(description = "机构等级")
    private String institutionLevel;

    @Schema(description = "机构性质")
    private Integer institutionType;

    @Schema(description = "上级机构")
    private String superiorInstitution;
}
