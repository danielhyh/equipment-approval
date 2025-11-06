package cn.iocoder.yudao.module.biz.service.application;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.app.application.vo.AppApplicationSaveReqVO;
import cn.iocoder.yudao.module.biz.controller.app.application.vo.ApplicationRecreateRequest;
import cn.iocoder.yudao.module.biz.dal.mysql.institutionext.InstitutionExtMapper;
import cn.iocoder.yudao.module.biz.service.devicelicense.DeviceLicenseService;
import cn.iocoder.yudao.module.biz.service.notification.CreateNotificationRequest;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.operation.OperationLogService;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @Resource
    private InstitutionExtMapper  institutionExtMapper;

    @Resource(name = "bizExecutor")
    private Executor bizExecutor;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private NotificationService notificationService;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final Map<Integer, String> actionDescMap = Map.of(1, "许可证申请", 2, "许可证补办", 3, "许可证变更", 4, "基本信息变更");

    @Override
    public Long createApplication(AppApplicationSaveReqVO createReqVO) {
        // 插入
        ApplicationDO application = BeanUtils.toBean(createReqVO, ApplicationDO.class);
        application.setAppNo("SQ-"+timeFormatter.format(LocalDateTime.now()));
        application.setAppStatus(1);//待初审
        application.setDeadline(LocalDate.now().plusDays(45));
        applicationMapper.insert(application);
        //记录操作日志
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        String loginUserNickname = SecurityFrameworkUtils.getLoginUserNickname();

        operationService.log(application.getId(), loginUserId, loginUserNickname, "发起的" + actionDescMap.get(createReqVO.getAppType()));
        //发通知
        var request = new CreateNotificationRequest();
        String institutionName = jdbcClient.sql("select institution_name from biz_institution_ext where dept_id = ?")
                .param(createReqVO.getInstitutionId())
                .query(String.class).single();
        request.setAppId(application.getId());
        request.setTitle(institutionName);
        String format = String.format("%s(%s)", createReqVO.getLicenseDeviceName(), createReqVO.getLadderConfigModel());
        request.setContent(format);
        request.setPublishNow(false);
        request.setCreator("0");
        notificationService.createNotification(request);
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
//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//        pageReqVO.setUserId(loginUserId);
        // 必须使用 MyBatis Plus 的分页对象
        IPage<ApplicationPageRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        applicationMapper.page(page, pageReqVO);
        return processField(page);
    }

    @Override
    public PageResult<ApplicationPageRespVO> getAppApplicationPage(ApplicationPageReqVO pageReqVO) {
        IPage<ApplicationPageRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        applicationMapper.page2(page, pageReqVO);
        return processField(page);
    }


    private PageResult<ApplicationPageRespVO> processField(IPage<ApplicationPageRespVO> page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (ApplicationPageRespVO record : page.getRecords()) {
            String appStatus = record.getAppStatus();
            String deadline = record.getDeadline();
            LocalDateTime dateTime = LocalDateTime.parse(deadline, formatter);
            LocalDate localDate = dateTime.toLocalDate();
            long between = ChronoUnit.DAYS.between(LocalDate.now(), localDate);
            record.setRemainingDays(String.valueOf(between));
            record.setRemainingDays(record.getRemainingDays() + "天");
            if ("5".equals(appStatus)) {
                record.setRemainingDays("-");
            }
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public ApplicationBasicInformationVO getApplicationBasicInformation(Long id) {
        ApplicationBasicInformationVO vo = applicationMapper.selectBasicInfo(id);
        if (vo == null) {
            return new ApplicationBasicInformationVO();
        }
        String querySql = """
                select c.extra from biz_application a
                left join biz_license_original b on a.id = b.application_id
                left join biz_license_duplicate c on b.id = c.original_id
                where a.id = ?
                """;
        String status = jdbcClient.sql(querySql).param(id)
                .query(String.class).optional()
                .map(JSONObject::parseObject)
                .map(obj -> obj.getInteger("reviewResult"))
                .map(res -> switch (res) {
                    case 1 -> "通过";
                    case 2 -> "驳回整改";
                    case 0 -> "不通过";
                    default -> ""; // 保留原有状态
                }).orElse(null);
        vo.setStatus(status);
        return vo;
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
        ApplicationDO applicationDO = applicationMapper.selectById(id);
        String actionDescPrefix = "发起的" + actionDescMap.get(applicationDO.getAppType());

        if ("INITIAL".equals(reviewType)) {
            update.setInitialReviewResult(result);
            update.setInitialReviewTime(LocalDateTime.now());
            update.setInitialReviewerId(loginUserId);
            update.setInitialReviewOpinion(opinion);
            update.setAppStatus(result == 1 ? 3 : 2);
            String res = result ==1 ? "初步审核已通过,待专家审核。": "初步审核未通过。";

            //如果是基本信息变更 并且审核通过
            if (4 == applicationDO.getAppType()) {
                ObjectNode extra = applicationDO.getExtra();
                extra.put("id", applicationDO.getInstitutionId());
                Map<String, String> map = objectMapper.convertValue(extra, new TypeReference<>() {});
                institutionExtMapper.updateBasicInfo(map);
                res = result ==1 ? "审核已通过": "审核未通过。";
            }
            publisherNotification(id, loginUserId, res, opinion);
            operationService.log(reviewVO.getId(), loginUserId, loginUserNickname, actionDescPrefix + res);
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
            String res = result ==1 ? "专家审核已通过。": "专家审核未通过。";
            publisherNotification(id, loginUserId, res, opinion);
            operationService.log(reviewVO.getId(), loginUserId, loginUserNickname, actionDescPrefix + res, null,"expertIdList", JSON.toJSONString(expertIdList));
        } else {
            throw new ServiceException(new ErrorCode(1199, "无效的审核类型: " + reviewType));
        }

        applicationMapper.updateById(update);
    }

    Map<Integer, String> appTypeMap = Map.of(1, "申请", 2, "补办", 3 , "变更", 4, "基本信息变更");

    private void publisherNotification(Long appId, Long userId, String reviewRes, String opinion) {
        ApplicationDO applicationDO = applicationMapper.selectById(appId);
        String licenseDeviceName = applicationDO.getLicenseDeviceName();
        CreateNotificationRequest createNotificationRequest = new CreateNotificationRequest();
        createNotificationRequest.setTitle(licenseDeviceName + "申请进度更新");
        String format = String.format("提交的%s配置许可证%s%s, 审核意见：%s。", licenseDeviceName, appTypeMap.get(applicationDO.getAppType()), reviewRes, opinion);
        createNotificationRequest.setContent(format);
        createNotificationRequest.setPublishNow(true);
        createNotificationRequest.setCreator(null);
        createNotificationRequest.setAppId(appId);
        String institutionName = jdbcClient.sql("select institution_name from biz_institution_ext where dept_id = ?")
                .param(applicationDO.getInstitutionId())
                .query(String.class).single();
        createNotificationRequest.setUnitName(institutionName);
        notificationService.createNotification(createNotificationRequest);
    }


    public void generateOriginal(ApplicationReviewVO reviewVO) {
        Long id = reviewVO.getId();
        ApplicationBasicInformationVO vo = applicationMapper.selectBasicInfo(id);
        ApplicationDO aDo = applicationMapper.selectById(reviewVO.getId());
        LocalDateTime validDate = LocalDateTime.now().plusYears(10);
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
                "陕西省"+ vo.getRegion(), aDo.getLicenseDeviceName(), vo.getDetailedAddress(), validDate
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
                """).param(id).query(JdbcClientHelper::resultSetToMap).single();

        return deviceLicenseService.generateLicenseNumber("乙", map.get("region"), map.get("licenseDeviceName"),
                map.get("ladderConfigModel"));
    }

    @Override
    public ApprovalDetailsVO approvalDetails(Long id) {
        ApprovalDetailsVO vo = applicationMapper.approvalDetails(id);
        if (vo == null) {
            return new ApprovalDetailsVO();
        }
        String expertId = vo.getExpertId();
        if (StringUtils.isNotBlank(expertId)) {
            String[] split = expertId.split(",");
            List<Map<String, String>> nameList = jdbcClient.sql("select id, name from biz_expert_ext where id in (:ids)")
                    .param("ids", Arrays.asList(split))
                    .query(JdbcClientHelper::resultSetToMap)
                    .list();
            vo.setExpertList(nameList);
        }
        return vo;
    }

    @Override
    public List<ApplicationPageRespVO> list() {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        List<ApplicationPageRespVO> list = applicationMapper.list(loginUserId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (ApplicationPageRespVO record : list) {
            String appStatus = record.getAppStatus();
            String deadline = record.getDeadline();
            LocalDateTime dateTime = LocalDateTime.parse(deadline, formatter);
            LocalDate localDate = dateTime.toLocalDate();
            long between = ChronoUnit.DAYS.between(LocalDate.now(), localDate);
            record.setRemainingDays(String.valueOf(between));
            record.setRemainingDays(record.getRemainingDays() + "天");
            if ("5".equals(appStatus)) {
                record.setRemainingDays("-");
            }
        }
        return list;
    }

    @Override
    @Transactional
    public Long recreate(ApplicationRecreateRequest req) {
        ApplicationDO applicationDO = applicationMapper.selectById(req.getAppId());
        if (applicationDO == null) {
            throw new ServiceException(APPLICATION_NOT_EXISTS);
        }

        ApplicationDO recreate = BeanUtils.toBean(req, ApplicationDO.class);
        recreate.setInstitutionId(applicationDO.getInstitutionId());
        recreate.setAppNo("SQ-"+timeFormatter.format(LocalDateTime.now()));
        recreate.setAppStatus(1);//待初审
        recreate.setDeadline(LocalDate.now().plusDays(45));
        return 0L;
    }

    @Override
    public List<Map<String, Object>> preliminaryReview() {
        return applicationMapper.preliminaryReview();
    }

}