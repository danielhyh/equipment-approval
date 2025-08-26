package cn.iocoder.yudao.module.biz.controller.admin.expertext.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ExpertReviewVO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expertReviewTime;

    private Integer appStatus;

    private String licenseDeviceName;

    private String institutionName;

    private String appNo;
}
