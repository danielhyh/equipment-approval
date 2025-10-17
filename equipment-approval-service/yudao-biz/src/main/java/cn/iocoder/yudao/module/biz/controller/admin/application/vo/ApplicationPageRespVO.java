package cn.iocoder.yudao.module.biz.controller.admin.application.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private Long institutionId;

    private String appNo;

    private String ladderConfigModel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    private String appStatus;

    private String deadline;

    private String remainingDays;

    private String appType;

    private Long originalId;

    private Long duplicateId;

    private String licenseNo;

    private ObjectNode extra;
}
