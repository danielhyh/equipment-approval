package cn.iocoder.yudao.module.system.service.datasync.dto;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.converters.booleanconverter.BooleanNumberConverter;
import cn.idev.excel.converters.localdate.LocalDateDateConverter;
import cn.iocoder.yudao.module.system.service.datasync.converter.BooleanConverter;
import cn.iocoder.yudao.module.system.service.datasync.converter.DateConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = false)
public class ExcelDeptDTO {

    /**
     * 部门编号
     */
    @Schema(description = "部门编号")
    @ExcelProperty(index = 0)
    private String orgId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @ExcelProperty(index = 1)
    private String caption;

    /**
     * 部门父编号
     */
    @Schema(description = "部门父编号")
    @ExcelProperty(index = 2)
    private String parent;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    @ExcelProperty(index = 4, converter = BooleanConverter.class)
    private Boolean enabled;

    /**
     * 机构负责人
     */
    @Schema(description = "机构负责人")
    @ExcelProperty(index = 5)
    private String governor;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @ExcelProperty(index = 6)
    private String tel;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @ExcelProperty(index = 8, converter = DateConverter.class)
    private LocalDateTime createDate;

    /**
     * 组织机构代码
     */
    @Schema(description = "组织机构代码")
    @ExcelProperty(index = 26)
    private String deptCode;

    /**
     * 社会信用代码
     */
    @Schema(description = "社会信用代码")
    @ExcelProperty(index = 27)
    private String deptShxydm;

    /**
     * 机构分类管理代码
     */
    @Schema(description = "机构分类管理代码")
    @ExcelProperty(index = 29)
    private String jgflgl;

    /**
     * 医院等级（级）
     */
    @Schema(description = "医院等级（级）")
    @ExcelProperty(index = 34)
    private String yydjJ;

    /**
     * 医院等级（等）
     */
    @Schema(description = "医院等级（等）")
    @ExcelProperty(index = 35)
    private String yydjD;

    /**
     * 通讯地址
     */
    @Schema(description = "通讯地址")
    @ExcelProperty(index = 47)
    private String txDz;

    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    @ExcelProperty(index = 49)
    private String txDhhm;

    /**
     * 法定代表人
     */
    @Schema(description = "法定代表人")
    @ExcelProperty(index = 45)
    private String fzr;

    /**
     * 国家标准区划
     */
    @Schema(description = "国家标准区划")
    @ExcelProperty(index = 76)
    private String gbQhdm;

    @Schema(description = "卫生机构类别代码")
    @ExcelProperty(index = 30)
    private String deptClass;

    @Schema(description = "行政区划代码")
    @ExcelProperty(index = 32)
    private String deptAddressCode;


    @Schema(description = "执业许可证登记号")
    @ExcelProperty(index = 28)
    private String deptZyxkzdjh;

//    @ExcelProperty(index = 29)
//    private String jjlx;

    @Schema(description = "操作类型 create/update/delete")
    private final String operation = "create";
}
