package cn.iocoder.yudao.module.biz.controller.app.applicationmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户申报材料响应")
public class MaterialResponse {

    @Schema(description = "1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表")
    private Integer materialType;

    @Schema(description = "资料名称")
    private String materialName;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件大小")
    private String fileSize;
}
