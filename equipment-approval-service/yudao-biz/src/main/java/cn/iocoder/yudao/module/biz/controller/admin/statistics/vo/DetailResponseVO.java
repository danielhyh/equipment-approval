package cn.iocoder.yudao.module.biz.controller.admin.statistics.vo;


import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.iocoder.yudao.module.biz.controller.admin.statistics.converter.StatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema
public class DetailResponseVO {

    @Schema(description = "区域")
    @ExcelProperty("区域")
    private String region;

    @Schema(description = "机构名称")
    @ExcelProperty("机构名称")
    private String institutionName;

    @Schema(description = "设备名")
    @ExcelProperty("设备名")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    @ExcelProperty("阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "序列号")
    @ExcelProperty("序列号")
    private String licenseNo;

    @Schema(description = "发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("发证日期")
    private LocalDate issueDate;

    @Schema(description = "安装日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("安装日期")
    private LocalDate installationDate;

    @Schema(description = "生产企业")
    @ExcelProperty("生产企业")
    private String productionEnterprise;
    @Schema(description = "具体型号")

    @ExcelProperty("具体型号")
    private String specificModel;


    @Schema(description = "副本发证日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelProperty("副本发证日期")
    private LocalDate duplicateIssueDate;


    @Schema(description = "状态")
    @ExcelProperty(value = "状态", converter = StatusConverter.class)
    private Integer status;


}
