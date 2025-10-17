package cn.iocoder.yudao.module.biz.controller.admin.statistics.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilterRequest {

    @NotNull(message = "分页页码不能为空")
    private Integer pageNo = 1;

    @NotNull(message = "分页每页数量不能为空")
    private Integer pageSize = 10;

    @Schema(description = "许可证编号或医疗机构名")
    private String keywords;

    @Schema(description = "统计年份")
    private Integer year;

    @Schema(description = "行政区划")
    private String region;

    @Schema(description = "起始范围")
    private LocalDate startDate;

    @Schema(description = "截至日期")
    private LocalDate endDate;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "证书状态")
    private Integer status;

    @Schema(description = "设备类型 逗号分隔")
    private String deviceTypes;

    @Schema(description = "验收状态 1-通过，0-未通过")
    private Integer acceptanceStatus;

    @Schema(description = "机构性质 1社会办医 2政府办医")
    private Integer institutionType;


    @Schema(hidden = true)
    @JsonIgnore
    private List<String> types;
}
