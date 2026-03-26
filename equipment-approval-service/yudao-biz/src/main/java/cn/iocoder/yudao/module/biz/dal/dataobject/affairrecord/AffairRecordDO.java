package cn.iocoder.yudao.module.biz.dal.dataobject.affairrecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

/**
 * 高效通办办件记录 DO（用于回调幂等校验）
 */
@TableName("biz_affair_record")
@KeySequence("biz_affair_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffairRecordDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 统一办件单号（唯一索引，用于幂等）
     */
    private String projId;

    /**
     * 关联的申请ID
     */
    private Long applicationId;

    /**
     * 受理状态：1-成功，0-失败
     */
    private Integer status;

}
