package cn.iocoder.yudao.module.biz.service.supplementaryinfo;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.app.supplementaryinfo.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.supplementaryinfo.SupplementaryInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.supplementaryinfo.SupplementaryInfoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 补充信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SupplementaryInfoServiceImpl implements SupplementaryInfoService {

    @Resource
    private SupplementaryInfoMapper supplementaryInfoMapper;

    @Override
    public Long createSupplementaryInfo(AppSupplementaryInfoSaveReqVO createReqVO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        // 插入
        SupplementaryInfoDO supplementaryInfo = BeanUtils.toBean(createReqVO, SupplementaryInfoDO.class);
        supplementaryInfo.setCreator(loginUserId.toString());
        supplementaryInfoMapper.insert(supplementaryInfo);

        // 返回
        return supplementaryInfo.getId();
    }

    @Override
    public void updateSupplementaryInfo(AppSupplementaryInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateSupplementaryInfoExists(updateReqVO.getId());
        // 更新
        SupplementaryInfoDO updateObj = BeanUtils.toBean(updateReqVO, SupplementaryInfoDO.class);
        supplementaryInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteSupplementaryInfo(Long id) {
        // 校验存在
        validateSupplementaryInfoExists(id);
        // 删除
        supplementaryInfoMapper.deleteById(id);
    }

    @Override
        public void deleteSupplementaryInfoListByIds(List<Long> ids) {
        // 删除
        supplementaryInfoMapper.deleteByIds(ids);
        }


    private void validateSupplementaryInfoExists(Long id) {
        if (supplementaryInfoMapper.selectById(id) == null) {
            throw exception(SUPPLEMENTARY_INFO_NOT_EXISTS);
        }
    }

    @Override
    public SupplementaryInfoDO getSupplementaryInfo(Long id) {
        return supplementaryInfoMapper.selectById(id);
    }

    @Override
    public PageResult<SupplementaryInfoDO> getSupplementaryInfoPage(AppSupplementaryInfoPageReqVO pageReqVO) {
        return supplementaryInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SupplementaryInfoDO> list(Integer type) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        LambdaQueryWrapper<SupplementaryInfoDO> queryWrapper = Wrappers.lambdaQuery(SupplementaryInfoDO.class)
                .eq(type != null, SupplementaryInfoDO::getInfoType, type)
                .eq(SupplementaryInfoDO::getCreator, loginUserId);
        return supplementaryInfoMapper.selectList(queryWrapper);
    }

}