package cn.iocoder.yudao.module.system.dal.dataobject.datasync;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@TableName("sync_record")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncRecordDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String syncBatchNo;

    private String syncType;

    private Integer syncStatus;

    private Integer totalCount;

    private Integer successCount;

    private Integer failCount;

    private String errorMsg;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long costTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
