package cn.iocoder.yudao.module.biz.dal.mysql.equipmentmanufacturer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.biz.dal.dataobject.equipmentmanufacturer.EquipmentManufacturerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo.*;

/**
 * 设备生产企业 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface EquipmentManufacturerMapper extends BaseMapperX<EquipmentManufacturerDO> {

    default PageResult<EquipmentManufacturerDO> selectPage(EquipmentManufacturerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EquipmentManufacturerDO>()
                .likeIfPresent(EquipmentManufacturerDO::getCompanyName, reqVO.getCompanyName())
                .eqIfPresent(EquipmentManufacturerDO::getMainEquipmentType, reqVO.getMainEquipmentType())
                .eqIfPresent(EquipmentManufacturerDO::getStatus, reqVO.getStatus())
                .orderByDesc(EquipmentManufacturerDO::getId));
    }

}