package cn.iocoder.yudao.module.system.service.datasync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "同步部门")
public class SyncDeptDTO {

    /**
     * 部门编号
     */
    @Schema(description = "部门编号")
    @NotBlank(message = "外部机构ID不能为空")
    private String orgId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @NotBlank(message = "机构名称不能为空")
    private String caption;

    /**
     * 部门父编号
     */
    @Schema(description = "部门父编号")
    private String parent;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enabled;

    /**
     * 机构负责人
     */
    @Schema(description = "机构负责人")
    private String governor;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String tel;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createDate;

    /**
     * 组织机构代码
     */
    @Schema(description = "组织机构代码")
    private String deptCode;

    /**
     * 社会信用代码
     */
    @Schema(description = "社会信用代码")
    private String deptShxydm;

    /**
     * 机构分类管理代码
     */
    @Schema(description = "机构分类管理代码")
    private String jgflgl;

    /**
     * 医院等级（级）
     */
    @Schema(description = "医院等级（级）")
    private String yydjJ;

    /**
     * 医院等级（等）
     */
    @Schema(description = "医院等级（等）")
    private String yydjD;

    /**
     * 通讯地址
     */
    @Schema(description = "通讯地址")
    private String txDz;

    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    private String txDhhm;

    /**
     * 法定代表人
     */
    @Schema(description = "法定代表人")
    private String fzr;

    /**
     * 国家标准区划
     */
    @Schema(description = "国家标准区划")
    private String gbQhdm;

    @Schema(description = "卫生机构类别代码")
    private String deptClass;

}
