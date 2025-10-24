package cn.iocoder.yudao.module.system.dal.mysql.user;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.user.ExternalUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExternalUserMapper extends BaseMapperX<ExternalUser> {
}
