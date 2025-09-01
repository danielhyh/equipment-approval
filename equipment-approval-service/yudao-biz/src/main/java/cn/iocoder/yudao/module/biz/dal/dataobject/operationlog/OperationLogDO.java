package cn.iocoder.yudao.module.biz.dal.dataobject.operationlog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("operation_log")
@Schema(description = "操作日志")
public class OperationLogDO {

    @Schema(description = "主键ID", example = "123456789")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "业务ID", example = "1001")
    private Long businessId;

    @Schema(description = "操作人ID", example = "1001")
    private Long operatorId;

    @Schema(description = "操作人姓名", example = "张主任")
    private String operatorName;

    @Schema(description = "操作描述", example = "提交配置许可申请")
    private String actionDesc;

    @Schema(description = "扩展信息格式为json")
    private String extraInfo;

    @Schema(description = "备注", example = "首次提交申请")
    private String remark;

    @Schema(description = "创建时间", example = "2024-01-10T09:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

}
