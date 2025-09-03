package cn.iocoder.yudao.module.biz.service.supplementaryinfo;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo.SupplementaryInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 补充信息 Service 接口
 *
 * @author 芋道源码
 */
public interface SupplementaryInfoService {

    /**
     * 创建补充信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSupplementaryInfo(@Valid AppSupplementaryInfoSaveReqVO createReqVO);

    /**
     * 更新补充信息
     *
     * @param updateReqVO 更新信息
     */
    void updateSupplementaryInfo(@Valid AppSupplementaryInfoSaveReqVO updateReqVO);

    /**
     * 删除补充信息
     *
     * @param id 编号
     */
    void deleteSupplementaryInfo(Long id);

    /**
    * 批量删除补充信息
    *
    * @param ids 编号
    */
    void deleteSupplementaryInfoListByIds(List<Long> ids);

    /**
     * 获得补充信息
     *
     * @param id 编号
     * @return 补充信息
     */
    SupplementaryInfoDO getSupplementaryInfo(Long id);

    /**
     * 获得补充信息分页
     *
     * @param pageReqVO 分页查询
     * @return 补充信息分页
     */
    PageResult<SupplementaryInfoDO> getSupplementaryInfoPage(AppSupplementaryInfoPageReqVO pageReqVO);

    List<SupplementaryInfoDO> list(Integer type);

}