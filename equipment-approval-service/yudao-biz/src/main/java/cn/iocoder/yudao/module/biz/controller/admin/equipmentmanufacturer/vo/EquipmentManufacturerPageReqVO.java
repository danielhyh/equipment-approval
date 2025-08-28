package cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备生产企业分页 Request VO")
@Data
public class EquipmentManufacturerPageReqVO extends PageParam {

    @Schema(description = "公司名称", example = "王五")
    private String companyName;
//
//    @Schema(description = "公司地址")
//    private String address;

    @Schema(description = "主要设备类型", example = "1")
    private String mainEquipmentType;

    @Schema(description = "状态：1-启用，0-禁用", example = "2")
    private Integer status;

//    @Schema(description = "创建时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
//    private LocalDateTime[] createTime;

}