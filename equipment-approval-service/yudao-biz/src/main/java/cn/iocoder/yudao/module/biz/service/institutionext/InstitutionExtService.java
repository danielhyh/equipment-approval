package cn.iocoder.yudao.module.biz.service.institutionext;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 机构扩展信息 Service 接口
 *
 * @author Listen
 */
public interface InstitutionExtService {

    /**
     * 创建机构扩展信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInstitutionExt(@Valid InstitutionExtSaveReqVO createReqVO);

    /**
     * 更新机构扩展信息
     *
     * @param updateReqVO 更新信息
     */
    void updateInstitutionExt(@Valid InstitutionExtSaveReqVO updateReqVO);

    /**
     * 删除机构扩展信息
     *
     * @param id 编号
     */
    void deleteInstitutionExt(Long id);

    /**
    * 批量删除机构扩展信息
    *
    * @param ids 编号
    */
    void deleteInstitutionExtListByIds(List<Long> ids);

    /**
     * 获得机构扩展信息
     *
     * @param id 编号
     * @return 机构扩展信息
     */
    InstitutionExtDO getInstitutionExt(Long id);

    /**
     * 获得机构扩展信息分页
     *
     * @param pageReqVO 分页查询
     * @return 机构扩展信息分页
     */
    PageResult<InstitutionExtDO> getInstitutionExtPage(InstitutionExtPageReqVO pageReqVO);

    InstitutionExtDetailsVO getDetails(Long id);

}