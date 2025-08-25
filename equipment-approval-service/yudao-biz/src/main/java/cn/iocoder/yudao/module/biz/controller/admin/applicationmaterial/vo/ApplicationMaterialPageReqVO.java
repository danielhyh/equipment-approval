package cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 申请资料分页 Request VO")
@Data
public class ApplicationMaterialPageReqVO extends PageParam {

    @Schema(description = "申请ID", example = "24818")
    private Long applicationId;

    @Schema(description = "资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表", example = "2")
    private Integer materialType;

    @Schema(description = "资料名称", example = "赵六")
    private String materialName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "上传时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] uploadTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}