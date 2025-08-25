package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import lombok.Data;

@Data
public class ApplicationReviewVO {

    private Long id;

    private Integer reviewResult;

    private String reviewOpinion;

    //INITIAL 初审 EXPERT 专家审核
    private String reviewType;

    private String expertIds;

    private String expertAttachments;
}
