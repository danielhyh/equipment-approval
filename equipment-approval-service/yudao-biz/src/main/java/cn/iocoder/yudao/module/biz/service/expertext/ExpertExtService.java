package cn.iocoder.yudao.module.biz.service.expertext;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.expertext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.expertext.ExpertExtDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.apache.ibatis.annotations.Param;

/**
 * 专家扩展信息 Service 接口
 *
 * @author 芋道源码
 */
public interface ExpertExtService {

    /**
     * 创建专家扩展信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExpertExt(@Valid ExpertExtSaveReqVO createReqVO);

    /**
     * 更新专家扩展信息
     *
     * @param updateReqVO 更新信息
     */
    void updateExpertExt(@Valid ExpertExtSaveReqVO updateReqVO);

    /**
     * 删除专家扩展信息
     *
     * @param id 编号
     */
    void deleteExpertExt(Long id);

    /**
    * 批量删除专家扩展信息
    *
    * @param ids 编号
    */
    void deleteExpertExtListByIds(List<Long> ids);

    /**
     * 获得专家扩展信息
     *
     * @param id 编号
     * @return 专家扩展信息
     */
    ExpertExtDO getExpertExt(Long id);

    /**
     * 获得专家扩展信息分页

     * @return 专家扩展信息分页
     */
    PageResult<ExpertExtRespVO> getExpertExtPage(ExpertExtPageReqVO pageReqVO);

    List<ExpertExtRespVO> list(String keywords, String specialty);

    List<String> getSpecialty();

    List<ExpertReviewVO> selectReviewRecord(Long id);

}