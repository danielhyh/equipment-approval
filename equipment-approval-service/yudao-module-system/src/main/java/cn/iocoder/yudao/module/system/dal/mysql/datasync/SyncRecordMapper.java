package cn.iocoder.yudao.module.system.dal.mysql.datasync;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;

import cn.iocoder.yudao.module.system.dal.dataobject.datasync.PushRecordDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SyncRecordMapper extends BaseMapperX<PushRecordDO> {
}
