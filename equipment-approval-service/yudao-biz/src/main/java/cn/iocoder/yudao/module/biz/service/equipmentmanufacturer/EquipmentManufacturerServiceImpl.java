package cn.iocoder.yudao.module.biz.service.equipmentmanufacturer;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.equipmentmanufacturer.EquipmentManufacturerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.equipmentmanufacturer.EquipmentManufacturerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 设备生产企业 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class EquipmentManufacturerServiceImpl implements EquipmentManufacturerService {

    @Resource
    private EquipmentManufacturerMapper equipmentManufacturerMapper;

    @Override
    public Long createEquipmentManufacturer(EquipmentManufacturerSaveReqVO createReqVO) {
        // 插入
        EquipmentManufacturerDO equipmentManufacturer = BeanUtils.toBean(createReqVO, EquipmentManufacturerDO.class);
        equipmentManufacturerMapper.insert(equipmentManufacturer);

        // 返回
        return equipmentManufacturer.getId();
    }

    @Override
    public void updateEquipmentManufacturer(EquipmentManufacturerSaveReqVO updateReqVO) {
        // 校验存在
        validateEquipmentManufacturerExists(updateReqVO.getId());
        // 更新
        EquipmentManufacturerDO updateObj = BeanUtils.toBean(updateReqVO, EquipmentManufacturerDO.class);
        equipmentManufacturerMapper.updateById(updateObj);
    }

    @Override
    public void deleteEquipmentManufacturer(Long id) {
        // 校验存在
        validateEquipmentManufacturerExists(id);
        // 删除
        equipmentManufacturerMapper.deleteById(id);
    }

    @Override
        public void deleteEquipmentManufacturerListByIds(List<Long> ids) {
        // 删除
        equipmentManufacturerMapper.deleteByIds(ids);
        }


    private void validateEquipmentManufacturerExists(Long id) {
        if (equipmentManufacturerMapper.selectById(id) == null) {
            throw exception(EQUIPMENT_MANUFACTURER_NOT_EXISTS);
        }
    }

    @Override
    public EquipmentManufacturerDO getEquipmentManufacturer(Long id) {
        return equipmentManufacturerMapper.selectById(id);
    }

    @Override
    public PageResult<EquipmentManufacturerDO> getEquipmentManufacturerPage(EquipmentManufacturerPageReqVO pageReqVO) {
        return equipmentManufacturerMapper.selectPage(pageReqVO);
    }

}