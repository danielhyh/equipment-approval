package cn.iocoder.yudao.module.system.dal.dataobject.datasync;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("system_push_log")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushRecordDO {


    /**
     * 主键ID
     */
    private Long id;

    /**
     * 推送类型：user/dept/batch
     */
    private String pushType;

    /**
     * 外部ID
     */
    private String externalId;

    /**
     * 操作：create/update/delete
     */
    private String operation;

    /**
     * 处理状态：0-失败 1-成功
     */
    private Integer status;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 推送时间
     */
    private LocalDateTime pushTime;

    /**
     * 处理时间
     */
    private LocalDateTime processTime;
}
