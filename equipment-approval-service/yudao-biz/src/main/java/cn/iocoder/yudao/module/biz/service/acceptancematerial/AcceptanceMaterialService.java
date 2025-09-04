package cn.iocoder.yudao.module.biz.service.acceptancematerial;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 验收资料 Service 接口
 *
 * @author 芋道源码
 */
public interface AcceptanceMaterialService {

    /**
     * 创建验收资料
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAcceptanceMaterial(@Valid AppAcceptanceMaterialSaveReqVO createReqVO);

    /**
     * 更新验收资料
     *
     * @param updateReqVO 更新信息
     */
    void updateAcceptanceMaterial(@Valid AppAcceptanceMaterialSaveReqVO updateReqVO);

    /**
     * 删除验收资料
     *
     * @param id 编号
     */
    void deleteAcceptanceMaterial(Long id);

    /**
    * 批量删除验收资料
    *
    * @param ids 编号
    */
    void deleteAcceptanceMaterialListByIds(List<Long> ids);

    /**
     * 获得验收资料
     *
     * @param id 编号
     * @return 验收资料
     */
    AcceptanceMaterialDO getAcceptanceMaterial(Long id);

    /**
     * 获得验收资料分页
     *
     * @param pageReqVO 分页查询
     * @return 验收资料分页
     */
    PageResult<AcceptanceMaterialDO> getAcceptanceMaterialPage(AppAcceptanceMaterialPageReqVO pageReqVO);

    List<AcceptanceMaterialDO> list(Integer type);

}