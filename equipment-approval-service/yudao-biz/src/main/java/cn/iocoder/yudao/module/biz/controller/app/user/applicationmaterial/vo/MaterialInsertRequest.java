package cn.iocoder.yudao.module.biz.controller.app.user.applicationmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "申请材料新增请求参数")
@Data
public class MaterialInsertRequest {

    @Schema(description = "申请id")
    @NotNull(message = "申请id不能为空")
    private Long applicationId;

    @Schema(description = "1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表")
    @NotNull(message = "申请材料类型不能为空")
    private Integer materialType;

    @Schema(description = "资料名称")
    @NotNull(message = "资料名称不能为空")
    private String materialName;

    @Schema(description = "文件路径")
    @NotNull(message = "文件路径不能为空")
    private String filePath;

    @Schema(description = "文件大小")
    private String fileSize;

}
