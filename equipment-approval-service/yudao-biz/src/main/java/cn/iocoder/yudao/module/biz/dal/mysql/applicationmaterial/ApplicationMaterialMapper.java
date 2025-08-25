package cn.iocoder.yudao.module.biz.dal.mysql.applicationmaterial;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo.*;

/**
 * 申请资料 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ApplicationMaterialMapper extends BaseMapperX<ApplicationMaterialDO> {

    default PageResult<ApplicationMaterialDO> selectPage(ApplicationMaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ApplicationMaterialDO>()
                .eqIfPresent(ApplicationMaterialDO::getApplicationId, reqVO.getApplicationId())
                .eqIfPresent(ApplicationMaterialDO::getMaterialType, reqVO.getMaterialType())
                .likeIfPresent(ApplicationMaterialDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(ApplicationMaterialDO::getFilePath, reqVO.getFilePath())
                .eqIfPresent(ApplicationMaterialDO::getFileSize, reqVO.getFileSize())
                .betweenIfPresent(ApplicationMaterialDO::getUploadTime, reqVO.getUploadTime())
                .betweenIfPresent(ApplicationMaterialDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ApplicationMaterialDO::getId));
    }

}