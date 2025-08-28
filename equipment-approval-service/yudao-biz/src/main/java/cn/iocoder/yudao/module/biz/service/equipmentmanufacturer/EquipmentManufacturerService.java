package cn.iocoder.yudao.module.biz.service.equipmentmanufacturer;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.equipmentmanufacturer.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.equipmentmanufacturer.EquipmentManufacturerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 设备生产企业 Service 接口
 *
 * @author 芋道源码
 */
public interface EquipmentManufacturerService {

    /**
     * 创建设备生产企业
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEquipmentManufacturer(@Valid EquipmentManufacturerSaveReqVO createReqVO);

    /**
     * 更新设备生产企业
     *
     * @param updateReqVO 更新信息
     */
    void updateEquipmentManufacturer(@Valid EquipmentManufacturerSaveReqVO updateReqVO);

    /**
     * 删除设备生产企业
     *
     * @param id 编号
     */
    void deleteEquipmentManufacturer(Long id);

    /**
    * 批量删除设备生产企业
    *
    * @param ids 编号
    */
    void deleteEquipmentManufacturerListByIds(List<Long> ids);

    /**
     * 获得设备生产企业
     *
     * @param id 编号
     * @return 设备生产企业
     */
    EquipmentManufacturerDO getEquipmentManufacturer(Long id);

    /**
     * 获得设备生产企业分页
     *
     * @param pageReqVO 分页查询
     * @return 设备生产企业分页
     */
    PageResult<EquipmentManufacturerDO> getEquipmentManufacturerPage(EquipmentManufacturerPageReqVO pageReqVO);

}