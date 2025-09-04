package cn.iocoder.yudao.module.biz.dal.mysql.acceptancematerial;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.*;

/**
 * 验收资料 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AcceptanceMaterialMapper extends BaseMapperX<AcceptanceMaterialDO> {

    default PageResult<AcceptanceMaterialDO> selectPage(AppAcceptanceMaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AcceptanceMaterialDO>()
                .eqIfPresent(AcceptanceMaterialDO::getApplicationId, reqVO.getApplicationId())
                .eqIfPresent(AcceptanceMaterialDO::getMaterialType, reqVO.getMaterialType())
                .betweenIfPresent(AcceptanceMaterialDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AcceptanceMaterialDO::getId));
    }

}