package cn.iocoder.yudao.module.biz.controller.admin.statistics.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class QueryRequest {

    @Schema(description = "行政区划")
    private String region;

    @Schema(description = "统计年份")
    private Integer year;

    @Schema(description = "设备品目名称列表")
    private List<String> deviceNames;
}
