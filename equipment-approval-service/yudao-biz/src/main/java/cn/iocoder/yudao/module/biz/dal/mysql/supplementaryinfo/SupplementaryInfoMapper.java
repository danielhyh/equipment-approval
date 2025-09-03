package cn.iocoder.yudao.module.biz.dal.mysql.supplementaryinfo;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo.SupplementaryInfoDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo.*;

/**
 * 补充信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SupplementaryInfoMapper extends BaseMapperX<SupplementaryInfoDO> {

    default PageResult<SupplementaryInfoDO> selectPage(AppSupplementaryInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SupplementaryInfoDO>()
                .eqIfPresent(SupplementaryInfoDO::getInfoType, reqVO.getInfoType())
                .eqIfPresent(SupplementaryInfoDO::getCreator, reqVO.getCreator())
                .betweenIfPresent(SupplementaryInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SupplementaryInfoDO::getId));
    }

}