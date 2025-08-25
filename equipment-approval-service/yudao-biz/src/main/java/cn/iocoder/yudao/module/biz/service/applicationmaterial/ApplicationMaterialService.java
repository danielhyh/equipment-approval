package cn.iocoder.yudao.module.biz.service.applicationmaterial;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 申请资料 Service 接口
 *
 * @author 芋道源码
 */
public interface ApplicationMaterialService {

    /**
     * 创建申请资料
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createApplicationMaterial(@Valid ApplicationMaterialSaveReqVO createReqVO);

    /**
     * 更新申请资料
     *
     * @param updateReqVO 更新信息
     */
    void updateApplicationMaterial(@Valid ApplicationMaterialSaveReqVO updateReqVO);

    /**
     * 删除申请资料
     *
     * @param id 编号
     */
    void deleteApplicationMaterial(Long id);

    /**
    * 批量删除申请资料
    *
    * @param ids 编号
    */
    void deleteApplicationMaterialListByIds(List<Long> ids);

    /**
     * 获得申请资料
     *
     * @param id 编号
     * @return 申请资料
     */
    ApplicationMaterialDO getApplicationMaterial(Long id);

    /**
     * 获得申请资料分页
     *
     * @param pageReqVO 分页查询
     * @return 申请资料分页
     */
    PageResult<ApplicationMaterialDO> getApplicationMaterialPage(ApplicationMaterialPageReqVO pageReqVO);


    List<ApplicationMaterialDO> getApplicationMaterialList(Long id);

}