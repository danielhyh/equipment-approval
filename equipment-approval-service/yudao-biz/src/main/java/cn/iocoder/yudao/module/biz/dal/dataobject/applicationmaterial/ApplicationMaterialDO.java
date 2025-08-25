package cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 申请资料 DO
 *
 * @author 芋道源码
 */
@TableName("biz_application_material")
@KeySequence("biz_application_material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationMaterialDO extends BaseDO {

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
     * 资料类型：1-申请表，2-承诺书，3-营业执照，4-执业许可证，5-技术条件材料，6-补办申请表，7-变更申请表
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
