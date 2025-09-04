package cn.iocoder.yudao.module.biz.service.application;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.app.application.vo.AppApplicationSaveReqVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.institutionext.InstitutionExtDO;
import cn.iocoder.yudao.module.biz.dal.mysql.institutionext.InstitutionExtMapper;
import cn.iocoder.yudao.module.biz.service.devicelicense.DeviceLicenseService;
import cn.iocoder.yudao.module.biz.service.operation.OperationLogService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import cn.iocoder.yudao.module.biz.controller.admin.application.vo.*;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.*;

/**
 * 申请 Service 实现类
 *
 * @author listen
 */
@Service
@Validated
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private ApplicationMapper applicationMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private DeviceLicenseService  deviceLicenseService;

    @Resource
    private OperationLogService operationService;

    @Resource(name = "bizExecutor")
    private Executor bizExecutor;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public Long createApplication(AppApplicationSaveReqVO createReqVO) {
        // 插入
        ApplicationDO application = BeanUtils.toBean(createReqVO, ApplicationDO.class);
        application.setAppNo("SQ-"+timeFormatter.format(LocalDateTime.now()));
        application.setAppStatus(1);//待初审
        applicationMapper.insert(application);
        //记录操作日志
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String loginUserNickname = SecurityFrameworkUtils.getLoginUserNickname();
        operationService.log(application.getId(), loginUserId, loginUserNickname, "发起申请");
        // 返回
        return application.getId();
    }

    @Override
    public void updateApplication(AppApplicationSaveReqVO updateReqVO) {
        // 校验存在
        validateApplicationExists(updateReqVO.getId());
        // 更新
        ApplicationDO updateObj = BeanUtils.toBean(updateReqVO, ApplicationDO.class);
        applicationMapper.updateById(updateObj);
    }

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
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        pageReqVO.setUserId(loginUserId);
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
        String loginUserNickname = SecurityFrameworkUtils.getLoginUserNickname();
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
            update.setAppStatus(result == 1 ? 3 : 2);
            operationService.log(reviewVO.getId(), loginUserId, loginUserNickname, result ==1 ? "初步审核已通过,待专家审核": "初步审核未通过");
        } else if ("EXPERT".equals(reviewType)) {
            update.setExpertReviewResult(result);
            update.setExpertReviewTime(LocalDateTime.now());
            update.setExpertReviewOpinion(opinion);
            List<Long> expertIdList = Arrays.stream(StringUtils.split(expertIds,","))
                    .map(Long::valueOf)
                    .toList();
            update.setExpertId(expertIdList);
            if (StringUtils.isNotBlank(expertAttachments)) {
                update.setExpertAttachments(Lists.newArrayList(StringUtils.split(expertAttachments, ",")));
            }
            update.setAppStatus(result == 1 ? 5 : 4);
            if (result == 1) {
                update.setLicenseNo(reviewVO.getLicenseCode());
                update.setLicenseGenerateDate(reviewVO.getLicenseGenerateDate());
                //专家审核通过后异步执行创建正本，同事修改许可证序列号表状态为已使用
                CompletableFuture.runAsync(() -> generateOriginal(reviewVO), bizExecutor)
                        .exceptionally(throwable -> {
                            log.error("生成正本失败,reviewVO:{}", JSON.toJSONString(reviewVO), throwable);
                            return null;
                        });

            }
            operationService.log(reviewVO.getId(), loginUserId, loginUserNickname, result ==1 ? "专家审核通过": "专家审核未通过", null,"expertIdList", JSON.toJSONString(expertIdList));
        } else {
            throw new ServiceException(new ErrorCode(1199, "无效的审核类型: " + reviewType));
        }

        applicationMapper.updateById(update);
    }


    public void generateOriginal(ApplicationReviewVO reviewVO) {
        Long id = reviewVO.getId();
        ApplicationBasicInformationVO vo = applicationMapper.selectBasicInfo(id);
        ApplicationDO aDo = applicationMapper.selectById(reviewVO.getId());
        String sql = """
                INSERT INTO biz_license_original (
                                application_id,
                                license_no,
                                config_unit_name,
                                ownership_nature,
                                issuing_authority,
                                unified_social_credit_code,
                                ladder_config_model,
                                issue_date,
                                legal_person,
                                equipment_config_address,
                                license_device_name,
                                detailed_address,
                                valid_date
                            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        //插入正本表
        jdbcClient.sql(sql).params(
                id, reviewVO.getLicenseCode(), vo.getInstitutionName(), vo.getOwnershipNature(), "陕西省卫生健康委员会",
                vo.getUnifiedSocialCreditCode(), aDo.getLadderConfigModel(), reviewVO.getLicenseGenerateDate(), vo.getLegalPerson(),
                "陕西省"+ vo.getRegion(), aDo.getLicenseDeviceName(), vo.getDetailedAddress()
        ).update();
        //修改序列号表状态
        jdbcClient.sql("update biz_device_license set status = 'USED' where license_number = ?")
                .param(reviewVO.getLicenseCode()).update();
    }

    @Override
    public String getLicenseNumber(Long id) {
        Map<String, String> map = jdbcClient.sql("""
                SELECT
                  ba.license_device_name,
                  ladder_config_model,
                  region
                FROM
                  biz_application ba
                  LEFT JOIN biz_institution_ext bie ON ba.institution_id = bie.dept_id
                WHERE
                  ba.id = ?
                """).param(id).query(this::resultSetToMap).single();

        return deviceLicenseService.generateLicenseNumber("乙", map.get("region"), map.get("licenseDeviceName"),
                map.get("ladderConfigModel"));
    }

    @Override
    public ApprovalDetailsVO approvalDetails(Long id) {
        ApprovalDetailsVO vo = applicationMapper.approvalDetails(id);
        String expertId = vo.getExpertId();
        if (StringUtils.isNotBlank(expertId)) {
            String[] split = expertId.split(",");
            List<String> nameList = jdbcClient.sql("select nickname from system_users where id in (:ids)")
                    .param("ids", Arrays.asList(split))
                    .query(String.class)
                    .list();
            vo.setExpertList(nameList);
        }
        return vo;
    }

    private Map<String, String> resultSetToMap(ResultSet rs, int rowNum) throws SQLException {
        Map<String, String> row = new HashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
            Object value = rs.getObject(i);
            row.put(columnName, value != null ? value.toString() : null);
        }
        return row;
    }

}