package cn.iocoder.yudao.module.system.service.datasync.dto;

import cn.idev.excel.annotation.ExcelProperty;
import cn.iocoder.yudao.module.system.service.datasync.converter.BooleanConverter;
import cn.iocoder.yudao.module.system.service.datasync.converter.DateConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = false)
public class ExcelDeptDTO2 {

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
    @ExcelProperty(index = 3, converter = BooleanConverter.class)
    private Boolean enabled;

    /**
     * 机构负责人
     */
    @Schema(description = "机构负责人")
    @ExcelProperty(index = 4)
    private String governor;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @ExcelProperty(index = 5)
    private String tel;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @ExcelProperty(index = 6, converter = DateConverter.class)
    private LocalDateTime createDate;

    /**
     * 组织机构代码
     */
    @Schema(description = "组织机构代码")
    @ExcelProperty(index = 10)
    private String deptCode;

    /**
     * 社会信用代码
     */
    @Schema(description = "社会信用代码")
    @ExcelProperty(index = 11)
    private String deptShxydm;

    /**
     * 机构分类管理代码
     */
//    @Schema(description = "机构分类管理代码")
//    @ExcelProperty(index = 29)
//    private String jgflgl;

    /**
     * 医院等级（级）
     */
    @Schema(description = "医院等级（级）")
    @ExcelProperty(index = 16)
    private String yydjJ;

    /**
     * 医院等级（等）
     */
    @Schema(description = "医院等级（等）")
    @ExcelProperty(index = 17)
    private String yydjD;

    /**
     * 通讯地址
     */
    @Schema(description = "通讯地址")
    @ExcelProperty(index = 24)
    private String txDz;

    /**
     * 电话号码
     */
    @Schema(description = "电话号码")
    @ExcelProperty(index = 26)
    private String txDhhm;

    /**
     * 法定代表人
     */
    @Schema(description = "法定代表人")
    @ExcelProperty(index = 22)
    private String fzr;

    /**
     * 国家标准区划
     */
//    @Schema(description = "国家标准区划")
//    @ExcelProperty(index = 76)
//    private String gbQhdm;

    @Schema(description = "卫生机构类别代码")
    @ExcelProperty(index = 12)
    private String deptClass;

    @Schema(description = "行政区划代码")
    @ExcelProperty(index = 14)
    private String deptAddressCode;


//    @Schema(description = "执业许可证登记号")
//    @ExcelProperty(index = 28)
//    private String deptZyxkzdjh;
    /**
     * 经济类型代码
     */
    @ExcelProperty(index = 13)
    private String jjlx;

    /**
     * 主办单位
     */
    @ExcelProperty(index = 18)
    private String zbdw;

    /**
     * 政府办机构隶属关系
     */
    @ExcelProperty(index = 19)
    private String lsgx;

    @Schema(description = "操作类型 create/update/delete")
    private final String operation = "create";
}
