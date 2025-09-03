package cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 补充信息 DO
 *
 * @author 芋道源码
 */
@TableName("biz_supplementary_info")
@KeySequence("biz_supplementary_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementaryInfoDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 申请ID
     */
    private Long applicationId;
    /**
     * 信息类型：1-正本悬挂位置，2-设备使用情况，3-检查保养记录，4-使用人员变更
     */
    private Integer infoType;
    /**
     * 信息内容
     */
    private String infoContent;
    /**
     * 附件路径
     */
    private String filePath;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;


}
