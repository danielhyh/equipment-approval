package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ApprovalDetailsVO {

    //初审结果 1-通过，0-不通过
    private String initialReviewResult;
    //初审意见
    private String initialReviewOpinion;

    //初审人
    private String initialReviewer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //初审日期
    private Date initialReviewTime;
    //专家审核结果：1-通过，0-不通过
    private String expertReviewResult;
    //专家审核意见
    private String expertReviewOpinion;
    //专家id 多个时以逗号分隔
    @JsonIgnore
    private String expertId;

    private List<String> expertList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expertReviewTime;

    //证书生成时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseGenerateDate;

    //专家附件path 多个时以逗号分隔
    private String expertAttachments;
    //许可证编号
    private String licenseNo;
}
