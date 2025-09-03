package cn.iocoder.yudao.module.biz.dal.mysql.classaequipment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo.*;

/**
 * 甲类设备 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClassAEquipmentMapper extends BaseMapperX<ClassAEquipmentDO> {

    default PageResult<ClassAEquipmentDO> selectPage(ClassAEquipmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ClassAEquipmentDO>()
                .orderByDesc(ClassAEquipmentDO::getId));
    }

}