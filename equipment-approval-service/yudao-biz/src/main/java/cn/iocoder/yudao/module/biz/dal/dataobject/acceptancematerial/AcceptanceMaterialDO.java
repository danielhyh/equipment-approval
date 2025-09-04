package cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 验收资料 DO
 *
 * @author 芋道源码
 */
@TableName("biz_acceptance_material")
@KeySequence("biz_acceptance_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptanceMaterialDO extends BaseDO {

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
     * 资料类型：1-采购合同，2-中标通知书，3-采购发票，4-验收合格复印件，5-医疗器械注册证，6-承诺事项落实材料，7-配置信息登记表，8-副本原件
     */
    private Integer materialType;
    /**
     * 资料名称
     */
    private String materialName;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 文件大小(字节)
     */
    private Long fileSize;
    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;


}
