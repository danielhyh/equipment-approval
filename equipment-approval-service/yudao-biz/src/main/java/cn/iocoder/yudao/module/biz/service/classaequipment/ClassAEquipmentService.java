package cn.iocoder.yudao.module.biz.service.classaequipment;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.classaequipment.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 甲类设备 Service 接口
 *
 * @author 芋道源码
 */
public interface ClassAEquipmentService {

    /**
     * 创建甲类设备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createClassAEquipment(@Valid ClassAEquipmentSaveReqVO createReqVO);

    /**
     * 更新甲类设备
     *
     * @param updateReqVO 更新信息
     */
    void updateClassAEquipment(@Valid ClassAEquipmentSaveReqVO updateReqVO);

    /**
     * 删除甲类设备
     *
     * @param id 编号
     */
    void deleteClassAEquipment(Long id);

    /**
    * 批量删除甲类设备
    *
    * @param ids 编号
    */
    void deleteClassAEquipmentListByIds(List<Long> ids);

    /**
     * 获得甲类设备
     *
     * @param id 编号
     * @return 甲类设备
     */
    ClassAEquipmentDO getClassAEquipment(Long id);

    /**
     * 获得甲类设备分页
     *
     * @param pageReqVO 分页查询
     * @return 甲类设备分页
     */
    PageResult<ClassAEquipmentDO> getClassAEquipmentPage(ClassAEquipmentPageReqVO pageReqVO);

}