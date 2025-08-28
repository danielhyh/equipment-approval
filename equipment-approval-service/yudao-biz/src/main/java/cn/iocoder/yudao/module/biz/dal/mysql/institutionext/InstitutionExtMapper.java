package cn.iocoder.yudao.module.biz.dal.mysql.institutionext;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 机构扩展信息 Mapper
 *
 * @author Listen
 */
@Mapper
public interface InstitutionExtMapper extends BaseMapperX<InstitutionExtDO> {

    default PageResult<InstitutionExtDO> selectPage(InstitutionExtPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InstitutionExtDO>()
                .eqIfPresent(InstitutionExtDO::getId, reqVO.getId())
                .likeIfPresent(InstitutionExtDO::getInstitutionName, reqVO.getInstitutionName())
                .eqIfPresent(InstitutionExtDO::getInstitutionLevel, reqVO.getInstitutionLevel())
                .eqIfPresent(InstitutionExtDO::getOwnershipNature, reqVO.getOwnershipNature())
                .eqIfPresent(InstitutionExtDO::getRegion, reqVO.getRegion())
                .orderByDesc(InstitutionExtDO::getId));
    }

    InstitutionExtDetailsVO getDetails(@Param("id") Long id);

}