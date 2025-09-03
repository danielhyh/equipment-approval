package cn.iocoder.yudao.module.biz.service.classaequipment;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.classaequipment.ClassAEquipmentMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 甲类设备 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ClassAEquipmentServiceImpl implements ClassAEquipmentService {

    @Resource
    private ClassAEquipmentMapper classAEquipmentMapper;

    @Override
    public Long createClassAEquipment(ClassAEquipmentSaveReqVO createReqVO) {
        // 插入
        ClassAEquipmentDO classAEquipment = BeanUtils.toBean(createReqVO, ClassAEquipmentDO.class);
        classAEquipmentMapper.insert(classAEquipment);

        // 返回
        return classAEquipment.getId();
    }

    @Override
    public void updateClassAEquipment(ClassAEquipmentSaveReqVO updateReqVO) {
        // 校验存在
        validateClassAEquipmentExists(updateReqVO.getId());
        // 更新
        ClassAEquipmentDO updateObj = BeanUtils.toBean(updateReqVO, ClassAEquipmentDO.class);
        classAEquipmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteClassAEquipment(Long id) {
        // 校验存在
        validateClassAEquipmentExists(id);
        // 删除
        classAEquipmentMapper.deleteById(id);
    }

    @Override
        public void deleteClassAEquipmentListByIds(List<Long> ids) {
        // 删除
        classAEquipmentMapper.deleteByIds(ids);
        }


    private void validateClassAEquipmentExists(Long id) {
        if (classAEquipmentMapper.selectById(id) == null) {
            throw exception(CLASS_A_EQUIPMENT_NOT_EXISTS);
        }
    }

    @Override
    public ClassAEquipmentDO getClassAEquipment(Long id) {
        return classAEquipmentMapper.selectById(id);
    }

    @Override
    public PageResult<ClassAEquipmentDO> getClassAEquipmentPage(ClassAEquipmentPageReqVO pageReqVO) {
        return classAEquipmentMapper.selectPage(pageReqVO);
    }

}