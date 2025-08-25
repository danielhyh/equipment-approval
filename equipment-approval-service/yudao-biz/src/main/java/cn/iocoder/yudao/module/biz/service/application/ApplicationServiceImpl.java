package cn.iocoder.yudao.module.biz.service.application;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServerException;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.module.biz.controller.admin.application.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 申请 Service 实现类
 *
 * @author listen
 */
@Service
@Validated
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private ApplicationMapper applicationMapper;

    @Override
    public Long createApplication(ApplicationSaveReqVO createReqVO) {
        // 插入
        ApplicationDO application = BeanUtils.toBean(createReqVO, ApplicationDO.class);
        applicationMapper.insert(application);

        // 返回
        return application.getId();
    }

    @Override
    public void updateApplication(ApplicationSaveReqVO updateReqVO) {
        // 校验存在
        validateApplicationExists(updateReqVO.getId());
        // 更新
        ApplicationDO updateObj = BeanUtils.toBean(updateReqVO, ApplicationDO.class);
        applicationMapper.updateById(updateObj);
    }

    @Override
    public void deleteApplication(Long id) {
        // 校验存在
        validateApplicationExists(id);
        // 删除
        applicationMapper.deleteById(id);
    }

    @Override
    public void deleteApplicationListByIds(List<Long> ids) {
        // 删除
        applicationMapper.deleteByIds(ids);
    }


    private void validateApplicationExists(Long id) {
        if (applicationMapper.selectById(id) == null) {
            throw exception(APPLICATION_NOT_EXISTS);
        }
    }

    @Override
    public ApplicationDO getApplication(Long id) {
        return applicationMapper.selectById(id);
    }

    @Override
    public PageResult<ApplicationPageRespVO> getApplicationPage(ApplicationPageReqVO pageReqVO) {
        // 必须使用 MyBatis Plus 的分页对象
        IPage<ApplicationPageRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        applicationMapper.page(page, pageReqVO);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public ApplicationBasicInformationVO getApplicationBasicInformation(Long id) {
        return applicationMapper.selectBasicInfo(id);
    }

    @Override
    public BusinessInfoVO businessInfo(Long id) {
        return applicationMapper.businessInfo(id);
    }

    @Override
    public void approval(ApplicationReviewVO reviewVO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String reviewType = reviewVO.getReviewType();
        Long id = reviewVO.getId();
        Integer result = reviewVO.getReviewResult();
        String opinion = reviewVO.getReviewOpinion();
        String expertIds = reviewVO.getExpertIds();
        String expertAttachments = reviewVO.getExpertAttachments();
        ApplicationDO update = new ApplicationDO();
        update.setId(id);

        if ("INITIAL".equals(reviewType)) {
            update.setInitialReviewResult(result);
            update.setInitialReviewTime(LocalDateTime.now());
            update.setInitialReviewerId(loginUserId);
            update.setInitialReviewOpinion(opinion);
            update.setAppStatus(result == 1 ? 2 : 3);
        } else if ("EXPERT".equals(reviewType)) {
            update.setExpertReviewResult(result);
            update.setExpertReviewTime(LocalDateTime.now());
            update.setExpertReviewOpinion(opinion);
            List<Long> expertIdList = Arrays.stream(StringUtils.split(expertIds))
                    .map(Long::valueOf)
                    .toList();
            update.setExpertId(expertIdList);
            if (StringUtils.isNotBlank(expertAttachments)) {
                update.setExpertAttachments(Lists.newArrayList(StringUtils.split(expertAttachments)));
            }
            update.setAppStatus(result == 1 ? 5 : 6);
        } else {
            throw new ServiceException(new ErrorCode(1199, "无效的审核类型: " + reviewType));
        }

        applicationMapper.updateById(update);
    }

}