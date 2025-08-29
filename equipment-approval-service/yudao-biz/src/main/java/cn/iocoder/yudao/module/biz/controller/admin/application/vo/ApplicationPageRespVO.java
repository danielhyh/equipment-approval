package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "管理后台 - 查询分页 VO")
@Data
public class ApplicationPageRespVO {

    private Long id;

    private String institutionName;

    private String licenseDeviceName;

    private String ladderConfigModel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String appStatus;

    private String deadline;

    private String remainingDays;

    private String appType;
}
