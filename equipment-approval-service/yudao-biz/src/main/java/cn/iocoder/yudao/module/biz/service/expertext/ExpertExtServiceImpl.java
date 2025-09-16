package cn.iocoder.yudao.module.biz.service.expertext;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.biz.controller.admin.expertext.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.expertext.ExpertExtDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.expertext.ExpertExtMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 专家扩展信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ExpertExtServiceImpl implements ExpertExtService {

    @Resource
    private ExpertExtMapper expertExtMapper;

    @Override
    public Long createExpertExt(ExpertExtSaveReqVO createReqVO) {
        // 插入
        ExpertExtDO expertExt = BeanUtils.toBean(createReqVO, ExpertExtDO.class);
        expertExt.setStatus(1);
        expertExtMapper.insert(expertExt);

        // 返回
        return expertExt.getId();
    }

    @Override
    public void updateExpertExt(ExpertExtSaveReqVO updateReqVO) {
        // 校验存在
        validateExpertExtExists(updateReqVO.getId());
        // 更新
        ExpertExtDO updateObj = BeanUtils.toBean(updateReqVO, ExpertExtDO.class);
        expertExtMapper.updateById(updateObj);
    }

    @Override
    public void deleteExpertExt(Long id) {
        // 校验存在
        validateExpertExtExists(id);
        // 删除
        expertExtMapper.deleteById(id);
    }

    @Override
        public void deleteExpertExtListByIds(List<Long> ids) {
        // 删除
        expertExtMapper.deleteByIds(ids);
        }


    private void validateExpertExtExists(Long id) {
        if (expertExtMapper.selectById(id) == null) {
            throw exception(EXPERT_EXT_NOT_EXISTS);
        }
    }

    @Override
    public ExpertExtDO getExpertExt(Long id) {
        return expertExtMapper.selectById(id);
    }

    @Override
    public PageResult<ExpertExtRespVO> getExpertExtPage(ExpertExtPageReqVO pageReqVO) {
        IPage<ExpertExtRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        expertExtMapper.page(page, pageReqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<ExpertExtRespVO> list(String keywords, String specialty) {
        return expertExtMapper.list(keywords, specialty);
    }

    @Override
    public List<String> getSpecialty() {
        return expertExtMapper.getSpecialty();
    }

    @Override
    public List<ExpertReviewVO> selectReviewRecord(Long id) {
        return expertExtMapper.selectReviewRecord(id);
    }

}