package cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 补充信息新增/修改 Request VO")
@Data
public class AppSupplementaryInfoSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30671")
    private Long id;

    @Schema(description = "申请ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10485")
    @NotNull(message = "申请ID不能为空")
    private Long applicationId;

    @Schema(description = "信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更不能为空")
    private Integer infoType;

    @Schema(description = "信息内容")
    private String infoContent;

    @Schema(description = "附件路径")
    private String filePath;

    @Schema(description = "提交时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "提交时间不能为空")
    private LocalDateTime submitTime;

}