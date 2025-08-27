package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApprovalDetailsVO {

    //初审结果 1-通过，0-不通过
    private String initialReviewResult;
    //初审意见
    private String initialReviewOpinion;
    //专家审核结果：1-通过，0-不通过
    private String expertReviewResult;
    //专家审核意见
    private String expertReviewOpinion;
    //专家id 多个时以逗号分隔
    private String expertId;

    //证书生成时间
    private LocalDate licenseGenerateDate;

    //专家附件path 多个时以逗号分隔
    private String expertAttachments;
    //许可证编号
    private String licenseNo;
}
