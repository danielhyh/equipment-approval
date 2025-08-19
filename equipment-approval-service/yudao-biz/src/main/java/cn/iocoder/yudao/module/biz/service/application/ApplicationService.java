package cn.iocoder.yudao.module.biz.service.application;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.application.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 申请 Service 接口
 *
 * @author listen
 */
public interface ApplicationService {

    /**
     * 创建申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createApplication(@Valid ApplicationSaveReqVO createReqVO);

    /**
     * 更新申请
     *
     * @param updateReqVO 更新信息
     */
    void updateApplication(@Valid ApplicationSaveReqVO updateReqVO);

    /**
     * 删除申请
     *
     * @param id 编号
     */
    void deleteApplication(Long id);

    /**
    * 批量删除申请
    *
    * @param ids 编号
    */
    void deleteApplicationListByIds(List<Long> ids);

    /**
     * 获得申请
     *
     * @param id 编号
     * @return 申请
     */
    ApplicationDO getApplication(Long id);

    /**
     * 获得申请分页
     *
     * @param pageReqVO 分页查询
     * @return 申请分页
     */
    PageResult<ApplicationPageRespVO> getApplicationPage(ApplicationPageReqVO pageReqVO);

}