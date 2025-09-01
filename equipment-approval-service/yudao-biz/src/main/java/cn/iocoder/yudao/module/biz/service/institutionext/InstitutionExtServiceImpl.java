package cn.iocoder.yudao.module.biz.service.institutionext;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.institutionext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.institutionext.InstitutionExtMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 机构扩展信息 Service 实现类
 *
 * @author Listen
 */
@Service
@Validated
public class InstitutionExtServiceImpl implements InstitutionExtService {

    @Resource
    private InstitutionExtMapper institutionExtMapper;

    @Override
    public Long createInstitutionExt(InstitutionExtSaveReqVO createReqVO) {
        // 插入
        InstitutionExtDO institutionExt = BeanUtils.toBean(createReqVO, InstitutionExtDO.class);
        institutionExtMapper.insert(institutionExt);

        // 返回
        return institutionExt.getId();
    }

    @Override
    public void updateInstitutionExt(InstitutionExtSaveReqVO updateReqVO) {
        // 校验存在
        validateInstitutionExtExists(updateReqVO.getId());
        // 更新
        InstitutionExtDO updateObj = BeanUtils.toBean(updateReqVO, InstitutionExtDO.class);
        institutionExtMapper.updateById(updateObj);
    }

    @Override
    public void deleteInstitutionExt(Long id) {
        // 校验存在
        validateInstitutionExtExists(id);
        // 删除
        institutionExtMapper.deleteById(id);
    }

    @Override
        public void deleteInstitutionExtListByIds(List<Long> ids) {
        // 删除
        institutionExtMapper.deleteByIds(ids);
        }


    private void validateInstitutionExtExists(Long id) {
        if (institutionExtMapper.selectById(id) == null) {
            throw exception(INSTITUTION_EXT_NOT_EXISTS);
        }
    }

    @Override
    public InstitutionExtDO getInstitutionExt(Long id) {
        return institutionExtMapper.selectById(id);
    }

    @Override
    public PageResult<InstitutionExtDO> getInstitutionExtPage(InstitutionExtPageReqVO pageReqVO) {
        return institutionExtMapper.selectPage(pageReqVO);
    }

    @Override
    public InstitutionExtDetailsVO getDetails(Long id) {
        return institutionExtMapper.getDetails(id);
    }

}