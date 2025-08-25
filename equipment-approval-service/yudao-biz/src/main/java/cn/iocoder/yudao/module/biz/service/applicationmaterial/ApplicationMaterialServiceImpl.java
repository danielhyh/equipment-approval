package cn.iocoder.yudao.module.biz.service.applicationmaterial;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.applicationmaterial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.applicationmaterial.ApplicationMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.applicationmaterial.ApplicationMaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 申请资料 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ApplicationMaterialServiceImpl implements ApplicationMaterialService {

    @Resource
    private ApplicationMaterialMapper applicationMaterialMapper;

    @Override
    public Long createApplicationMaterial(ApplicationMaterialSaveReqVO createReqVO) {
        // 插入
        ApplicationMaterialDO applicationMaterial = BeanUtils.toBean(createReqVO, ApplicationMaterialDO.class);
        applicationMaterialMapper.insert(applicationMaterial);

        // 返回
        return applicationMaterial.getId();
    }

    @Override
    public void updateApplicationMaterial(ApplicationMaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateApplicationMaterialExists(updateReqVO.getId());
        // 更新
        ApplicationMaterialDO updateObj = BeanUtils.toBean(updateReqVO, ApplicationMaterialDO.class);
        applicationMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteApplicationMaterial(Long id) {
        // 校验存在
        validateApplicationMaterialExists(id);
        // 删除
        applicationMaterialMapper.deleteById(id);
    }

    @Override
        public void deleteApplicationMaterialListByIds(List<Long> ids) {
        // 删除
        applicationMaterialMapper.deleteByIds(ids);
        }


    private void validateApplicationMaterialExists(Long id) {
        if (applicationMaterialMapper.selectById(id) == null) {
            throw exception(APPLICATION_MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public ApplicationMaterialDO getApplicationMaterial(Long id) {
        return applicationMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<ApplicationMaterialDO> getApplicationMaterialPage(ApplicationMaterialPageReqVO pageReqVO) {
        return applicationMaterialMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ApplicationMaterialDO> getApplicationMaterialList(Long id) {
        return applicationMaterialMapper.selectList("application_id", id);
    }

}