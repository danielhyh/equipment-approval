package cn.iocoder.yudao.module.biz.service.history.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class HistoryResult {

    @Schema(description = "id")
    private String id;

    @Schema(description = "许可证编号")
    private String licenseNumber;

    @Schema(description = "配置单位")
    private String institutionName;

    @Schema(description = "设备名称")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "所属区域")
    private String region;

    @Schema(description = "正本发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date originalIssueDate;

    @Schema(description = "副本发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date copyIssueDate;

    @Schema(description = "设备状态")
    private String deviceStatus;

    @Schema(description = "正本录入状态")
    private String originalEntryStatus;

    @Schema(description = "副本录入状态")
    private String copyEntryStatus;
}
