package cn.iocoder.yudao.module.biz.controller.admin.atg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 高效通办系统 - 创建申请 Request VO
 * 用于回调接口创建申请（无设备名称和阶梯机型）
 */
@Schema(description = "高效通办系统 - 创建申请 Request VO")
@Data
public class AtgApplicationCreateReqVO {

    @Schema(description = "统一办件单号")
    private String projId;

    @Schema(description = "申请类型：1-乙类许可证申请，2-乙类许可证补办，3-乙类许可证变更，4-基本信息变更，6-乙类许可证注销")
    private Integer appType;

    @Schema(description = "机构名称")
    private String institutionName;

    @Schema(description = "机构ID")
    private Long institutionId;

    @Schema(description = "申请人姓名")
    private String applicantName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "附件URL列表（JSON数组）")
    private String attachments;

    @Schema(description = "表单信息（高办系统原始JSON）")
    private String formInfo;

}
