package cn.iocoder.yudao.module.biz.controller.admin.atg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 高效通办系统 - 补充设备信息 Request VO
 * 管理员查看附件后，补充设备名称和阶梯配置机型
 */
@Schema(description = "高效通办系统 - 补充设备信息 Request VO")
@Data
public class AtgApplicationUpdateReqVO {

    @NotNull(message = "申请ID不能为空")
    @Schema(description = "申请ID")
    private Long id;

    @Schema(description = "许可设备名称")
    private String licenseDeviceName;

    @Schema(description = "阶梯配置机型")
    private String ladderConfigModel;

    @Schema(description = "设备配置地址")
    private String equipmentConfigAddress;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

}
