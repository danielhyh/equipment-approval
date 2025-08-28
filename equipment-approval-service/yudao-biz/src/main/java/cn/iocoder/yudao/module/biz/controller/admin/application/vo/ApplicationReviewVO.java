package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationReviewVO {

    private Long id;

    private Integer reviewResult;

    private String reviewOpinion;

    //INITIAL 初审 EXPERT 专家审核
    private String reviewType;

    private String expertIds;

    private String expertAttachments;

    private String licenseCode;

    private LocalDate licenseGenerateDate;
}
