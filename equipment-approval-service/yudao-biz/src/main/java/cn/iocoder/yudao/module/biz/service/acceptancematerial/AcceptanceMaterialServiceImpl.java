package cn.iocoder.yudao.module.biz.service.acceptancematerial;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.app.acceptancematerial.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.acceptancematerial.AcceptanceMaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 验收资料 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AcceptanceMaterialServiceImpl implements AcceptanceMaterialService {

    @Resource
    private AcceptanceMaterialMapper acceptanceMaterialMapper;

    @Override
    public Long createAcceptanceMaterial(AppAcceptanceMaterialSaveReqVO createReqVO) {
        // 插入
        AcceptanceMaterialDO acceptanceMaterial = BeanUtils.toBean(createReqVO, AcceptanceMaterialDO.class);
        acceptanceMaterialMapper.insert(acceptanceMaterial);

        // 返回
        return acceptanceMaterial.getId();
    }

    @Override
    public void updateAcceptanceMaterial(AppAcceptanceMaterialSaveReqVO updateReqVO) {
        // 校验存在
        validateAcceptanceMaterialExists(updateReqVO.getId());
        // 更新
        AcceptanceMaterialDO updateObj = BeanUtils.toBean(updateReqVO, AcceptanceMaterialDO.class);
        acceptanceMaterialMapper.updateById(updateObj);
    }

    @Override
    public void deleteAcceptanceMaterial(Long id) {
        // 校验存在
        validateAcceptanceMaterialExists(id);
        // 删除
        acceptanceMaterialMapper.deleteById(id);
    }

    @Override
        public void deleteAcceptanceMaterialListByIds(List<Long> ids) {
        // 删除
        acceptanceMaterialMapper.deleteByIds(ids);
        }


    private void validateAcceptanceMaterialExists(Long id) {
        if (acceptanceMaterialMapper.selectById(id) == null) {
            throw exception(ACCEPTANCE_MATERIAL_NOT_EXISTS);
        }
    }

    @Override
    public AcceptanceMaterialDO getAcceptanceMaterial(Long id) {
        return acceptanceMaterialMapper.selectById(id);
    }

    @Override
    public PageResult<AcceptanceMaterialDO> getAcceptanceMaterialPage(AppAcceptanceMaterialPageReqVO pageReqVO) {
        return acceptanceMaterialMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AcceptanceMaterialDO> list(Integer type) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        LambdaQueryWrapper<AcceptanceMaterialDO> wrapper = Wrappers.lambdaQuery(AcceptanceMaterialDO.class)
                .eq(type != null, AcceptanceMaterialDO::getMaterialType, type)
                .eq(AcceptanceMaterialDO::getCreator, loginUserId);
        return acceptanceMaterialMapper.selectList(wrapper);
    }

}