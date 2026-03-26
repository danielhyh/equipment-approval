package cn.iocoder.yudao.module.biz.dal.mysql.affairrecord;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.affairrecord.AffairRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 高效通办办件记录 Mapper
 */
@Mapper
public interface AffairRecordMapper extends BaseMapperX<AffairRecordDO> {

    default AffairRecordDO selectByProjId(String projId) {
        return selectOne(AffairRecordDO::getProjId, projId);
    }

}
