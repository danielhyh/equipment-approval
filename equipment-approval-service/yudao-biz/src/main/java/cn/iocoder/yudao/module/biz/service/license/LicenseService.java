package cn.iocoder.yudao.module.biz.service.license;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.validation.ValidationUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.*;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppDuplicateSubmitRequest;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppLicensePageRespVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.acceptancematerial.AcceptanceMaterialDO;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.module.biz.dal.mysql.acceptancematerial.AcceptanceMaterialMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.classaequipment.ClassAEquipmentMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.license.LicenseMapper;
import cn.iocoder.yudao.module.biz.service.devicelicense.DeviceLicenseService;
import cn.iocoder.yudao.module.biz.service.notification.CreateNotificationRequest;
import cn.iocoder.yudao.module.biz.service.notification.NotificationService;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LicenseService {

    private final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    @Resource
    private LicenseMapper licenseMapper;

    @Resource
    private ClassAEquipmentMapper classAEquipmentMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private DeviceLicenseService deviceLicenseService;

    @Resource
    private ApplicationMapper applicationMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private NotificationService  notificationService;

    public PageResult<AppLicensePageRespVO> licensePage(Integer pageSize, Integer pageNum, String type) {
        IPage<AppLicensePageRespVO> page = new Page<>(pageNum, pageSize);
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Long deptId = jdbcClient.sql("select dept_id from system_users where id = ?")
                .param(loginUserId)
                .query(Long.class)
                .single();
        licenseMapper.licensePage(page, type, loginUserId, deptId);
        List<AppLicensePageRespVO> records = page.getRecords();
        //设置设备验收状态
        setStatus(records, AppLicensePageRespVO::getDuplicateId, AppLicensePageRespVO::setStatus);
        return new PageResult<>(records, page.getTotal());
    }

    private <T> void setStatus(List<T> records, Function<T, Long> idExtractor, BiConsumer<T, String> statusSetter) {
        if (records.isEmpty()) {
            return;
        }
        List<Long> list = records.stream().map(idExtractor).filter(Objects::nonNull).toList();
        if (list.isEmpty()) {
            return;
        }
        List<String> extraJsonList = jdbcClient.sql("select extra from biz_license_duplicate where id in (:ids)")
                .param("ids", list)
                .query(String.class).list().stream().filter(Objects::nonNull).toList();
        if (extraJsonList.isEmpty()) {
            return;
        }
        Map<Long, ObjectNode> jsonMap = extraJsonList.stream().map(s -> {
            try {
                return objectMapper.readValue(s, ObjectNode.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toMap(o -> o.get("id").asLong(), v -> v));
        records.forEach(record -> {
            ObjectNode objectNode = jsonMap.getOrDefault(idExtractor.apply(record), null);
            if (objectNode != null) {
                String status = switch (objectNode.get("reviewResult").asInt()) {
                    case 1 -> "通过";
                    case 2 -> "驳回整改";
                    case 0 -> "不通过";
                    default -> ""; // 保留原有状态
                };
                statusSetter.accept(record, status);
            }
        });
    }


    public PageResult<LicensePageVO> page(LicensePageRequestVO param) {
        IPage<LicensePageVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        licenseMapper.page(page, param);
        List<LicensePageVO> records = page.getRecords();
        setStatus(records, LicensePageVO::getDuplicateId, LicensePageVO::setStatus);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    public OriginalLicenseVO getOriginalById(Long id) {
        return licenseMapper.getOriginalById(id);
    }

    public DuplicateLicenseVO getDuplicateById(Long id) {
        DuplicateLicenseVO duplicateLicenseVO = licenseMapper.getDuplicateById(id);
        if (duplicateLicenseVO == null) {
            return new DuplicateLicenseVO();
        }
        Integer reviewResult = Optional.ofNullable(duplicateLicenseVO.getExtra())
                .map(JSONObject::parseObject)
                .map(obj -> obj.getInteger("reviewResult"))
                .orElse(null);
        if (!Objects.equals(reviewResult, 1)) {
            duplicateLicenseVO.setInfoSubmitDate(null);
            duplicateLicenseVO.setInstallationDate(null);
            duplicateLicenseVO.setProductionEnterprise( null);
            duplicateLicenseVO.setProductSerialNo( null);
            duplicateLicenseVO.setSpecificModel(null);
            return duplicateLicenseVO;
        }

        return duplicateLicenseVO;
    }

    public Boolean updateOriginal(OriginalLicenseVO originalLicenseVO) {
        return licenseMapper.updateOriginalLicense(originalLicenseVO) > 0;
    }

    public Boolean updateDuplicate(DuplicateLicenseVO duplicateLicenseVO) {
        return licenseMapper.updateDuplicateLicense(duplicateLicenseVO) > 0;
    }

    @Transactional
    public boolean insertDuplicateLicense(AppDuplicateSubmitRequest request) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        boolean res = licenseMapper.insertDuplicateLicense(request, loginUserId) > 0;
        logger.info("副本插入成功, DuplicateId:{}", request.getId());
        insertEquipment(request);
        return res;
    }

    @Transactional
    public void insertEquipment(AppDuplicateSubmitRequest req) {
        ClassAEquipmentDO equipmentDO = getClassAEquipmentDO(req);
        String sql = """
                select a.institution_name, a.contact_person,
                       a.contact_phone, a.unified_social_credit_code, a.legal_person,
                       a.ownership_nature, a.detailed_address,
                       b.id as id,b.license_device_name from biz_institution_ext a
                left join biz_application b on a.dept_id = b.institution_id
                left join biz_license_original c on b.id = c.application_id
                where c.id = ?
                """;
        Map<String, String> res = jdbcClient.sql(sql).param(req.getOriginalId()).query(JdbcClientHelper::resultSetToMap).single();
        Optional<Long> optional = jdbcClient.sql("select equipment_id from biz_license_duplicate where id = ?").param(req.getId()).query(Long.class).optional();
        equipmentDO.setId(optional.orElse(null));
        equipmentDO.setConfigUnitName(res.get("institutionName"));
        equipmentDO.setContactPerson(res.get("contactPerson"));
        equipmentDO.setContactPhone(res.get("contactPhone"));
        equipmentDO.setUnifiedSocialCreditCode(res.get("unifiedSocialCreditCode"));
        equipmentDO.setLegalPerson(res.get("legalPerson"));
        equipmentDO.setLicenseDeviceName(res.get("licenseDeviceName"));
        equipmentDO.setOwnershipNature(res.get("ownershipNature"));
        equipmentDO.setEquipmentConfigAddress(res.get("detailedAddress"));
        classAEquipmentMapper.insertOrUpdate(equipmentDO);

        //更新application表 equipmentId
        jdbcClient.sql("update biz_application set equipment_id = :eid where id = :id")
                .param("eid", equipmentDO.getId())
                .param("id", res.get("id"))
                .update();
        logger.info("修改application表:{} equipmentId:{}", res.get("id"), equipmentDO.getId());
        //biz_license_duplicate equipment_id
        jdbcClient.sql("update biz_license_duplicate set equipment_id = ? where id = ?")
                .params(equipmentDO.getId(), req.getId()).update();
        logger.info("修改biz_license_duplicate表:{} equipmentId:{}", req.getId(), equipmentDO.getId());
    }

    private static ClassAEquipmentDO getClassAEquipmentDO(AppDuplicateSubmitRequest req) {
        ClassAEquipmentDO equipmentDO = new ClassAEquipmentDO();
        equipmentDO.setProductionEnterprise(req.getProductionEnterprise());
        equipmentDO.setSpecificModel(req.getSpecificModel());
        equipmentDO.setInstallationDate(req.getInstallationDate());
        equipmentDO.setSerialNumber(req.getProductSerialNo());
        equipmentDO.setPurchasePrice(new BigDecimal(req.getPurchasePrice()));
        equipmentDO.setStatus(1);
        equipmentDO.setEquipmentUsers(req.getEquipmentUsers());
        equipmentDO.setSpecialDescription(req.getSpecialDescription());
        equipmentDO.setType(2);
        return equipmentDO;
    }

    //模拟申请流程 创建申请 设置申请类型为5(线下申请) 但是不用审核，直接成功，随后根据数据生成正本副本，以及生成正副本中途需要的操作
    public void offlineProcessLicense(OfflineLicenseInsertRequest request) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();

        transactionTemplate.execute(status -> {
            try {
                OriginalLicenseVO originalLicense = request.getOriginalLicense();

                // 判断是新增还是修改
                if (request.getOriginalId() != null) {
//                    // 修改操作
//                    logger.info("执行许可证修改操作，正本ID: {}", request.getOriginalId());
//
//                    // 更新正本许可证
//                    originalLicense.setId(request.getOriginalId());
//                    originalLicense.setEquipmentConfigAddress("陕西省" + originalLicense.getEquipmentConfigAddress());
//
//                    int updateResult = licenseMapper.updateOriginalLicense(originalLicense);
//                    if (updateResult <= 0) {
//                        throw new RuntimeException("线下办理更新正本许可证失败");
//                    }
                    Long originalId = request.getOriginalId();
//
//                    logger.info("线下办理更新正本许可证成功，正本ID: {}", originalId);

                    // 如果有副本信息，则更新副本
                    if (request.getDuplicateLicense() != null) {
                        DuplicateLicenseVO duplicateLicense = request.getDuplicateLicense();
                        ValidationUtils.validate(duplicateLicense);
                        duplicateLicense.setId(request.getDuplicateId());
                        AppDuplicateSubmitRequest duplicateSubmitRequest = BeanUtils.toBean(duplicateLicense, AppDuplicateSubmitRequest.class);
                        duplicateSubmitRequest.setOriginalId(originalId);
                        insertEquipment(duplicateSubmitRequest);
                        int updateDuplicateResult = 0;
                        if (request.getDuplicateId() == null) {
                            updateDuplicateResult = licenseMapper.insertDuplicateLicense(duplicateSubmitRequest, loginUserId);
                        } else {
                            updateDuplicateResult = licenseMapper.updateDuplicateLicense(duplicateLicense);
                        }

                        if (updateDuplicateResult <= 0) {
                            throw new RuntimeException("线下办理更新副本许可证失败");
                        }
                        logger.info("线下办理更新副本许可证成功，副本ID: {}", duplicateSubmitRequest.getId());
                    }

                } else {
                    // 新增操作
                    logger.info("执行许可证新增操作");

                    // 1. 创建线下申请记录
                    ApplicationDO application = new ApplicationDO();
                    application.setAppNo("SQ-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                    application.setInstitutionId(originalLicense.getInstitutionId() != null ?
                            originalLicense.getInstitutionId() : getInstitutionIdByName(originalLicense.getConfigUnitName()));
                    application.setAppType(5); // 线下申请
                    application.setLicenseDeviceName(originalLicense.getLicenseDeviceName());
                    application.setLadderConfigModel(originalLicense.getLadderConfigModel());
                    application.setConfigReason("线下申请");
                    application.setAppStatus(5); // 直接设为专家审核通过
                    application.setInitialReviewResult(1); // 初审通过
                    application.setExpertReviewResult(1); // 专家审核通过
                    application.setInitialReviewTime(java.time.LocalDateTime.now());
                    application.setExpertReviewTime(java.time.LocalDateTime.now());
                    Date issueDate = originalLicense.getIssueDate();
                    LocalDate localDate = issueDate.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
                    application.setLicenseGenerateDate(localDate);
                    application.setDeadline(java.time.LocalDate.now().plusDays(45));

                    // 插入申请记录
                    applicationMapper.insert(application);
                    Long applicationId = application.getId();

                    // 获取刚插入的申请ID
                    logger.info("线下办理创建线下申请记录，申请ID: {}", application.getId());

                    // 2. 生成正本许可证
                    originalLicense.setEquipmentConfigAddress("陕西省" + originalLicense.getEquipmentConfigAddress());

                    // 生成许可证编号
                    String licenseCode = deviceLicenseService.generateLicenseNumber("乙",
                            originalLicense.getEquipmentConfigAddress(),
                            originalLicense.getLicenseDeviceName(),
                            originalLicense.getLadderConfigModel());
                    originalLicense.setLicenseNo(licenseCode);
                    // 插入正本记录
                    int originalResult = licenseMapper.insertOriginalLicense(originalLicense, loginUserId, applicationId);
                    if (originalResult <= 0) {
                        throw new RuntimeException("线下办理插入正本许可证失败");
                    }
                    Long originalId = originalLicense.getId();
                    // 获取正本ID
                    logger.info("线下办理创建正本许可证，正本ID: {}", originalId);

                    // 更新序列号表状态
                    jdbcClient.sql("UPDATE biz_device_license SET status = 'USED' WHERE license_number = ?")
                            .param(licenseCode).update();


                    // 3. 生成副本许可证
                    DuplicateLicenseVO duplicateLicense = request.getDuplicateLicense();
                    if (duplicateLicense != null) {
                        ValidationUtils.validate(duplicateLicense);
                        duplicateLicense.setId(null); // 确保ID为空
                        AppDuplicateSubmitRequest duplicateSubmitRequest = BeanUtils.toBean(duplicateLicense, AppDuplicateSubmitRequest.class);
                        duplicateSubmitRequest.setOriginalId(originalId);
                        // 插入副本记录
                        insertDuplicateLicense(duplicateSubmitRequest);
                        logger.info("线下办理副本插入成功, ID: {}", duplicateSubmitRequest.getId());
                    }


                    logger.info("线下许可证处理完成 - 申请ID: {}, 正本ID: {}", applicationId, originalId);
                }
            } catch (Exception e) {
                status.setRollbackOnly();
                logger.info("线下办理处理失败", e);
                throw new ServiceException(1111, "线下办理处理失败: " + e.getMessage());
            }
            return null;
        });
    }


    /**
     * 根据机构名称获取机构ID
     */
    private Long getInstitutionIdByName(String institutionName) {
        try {
            return jdbcClient.sql("SELECT dept_id FROM biz_institution_ext WHERE institution_name = ?")
                    .param(institutionName)
                    .query(Long.class).single();
        } catch (Exception e) {
            logger.warn("未找到机构: {}", institutionName);
            return null;
        }
    }

    public void approval(DuplicateApprovalRequest request) {

        String loginUserNickname = SecurityFrameworkUtils.getLoginUserNickname();
        String query = """
                select b.status from biz_acceptance_material b left join biz_license_original c
                on b.application_id = c.application_id
                left join biz_license_duplicate a on c.id = a.original_id
                where a.id = ?
                """;
        List<String> statusList = jdbcClient.sql(query).param(request.getId()).query(String.class).list();
        if (statusList.contains("待审核")) {
            throw new ServiceException(1111, "请审批验收资料");
        }
        String extra;
        try {
            extra = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            logger.error("序列化 DuplicateApprovalRequest 失败，请求数据：{}", request, e);
            throw new RuntimeException(e);
        }

        int res = jdbcClient.sql("update biz_license_duplicate set acceptance_status = ?, extra = ? where id = ?")
                .param(request.getReviewResult())
                .param(extra)
                .param(request.getId())
                .update();
        if (res > 0){
            String sql = """
                    select b.application_id, b.license_device_name from biz_license_duplicate a
                    left join biz_license_original b on a.original_id = b.id
                    where a.id = ?
                    """;
            Map<String, String> map = jdbcClient.sql(sql).param(request.getId()).query(JdbcClientHelper::resultSetToMap).single();
            Long appId = Long.parseLong(map.get("applicationId"));
            String deviceName = map.get("licenseDeviceName");
            CreateNotificationRequest req = new CreateNotificationRequest();
            req.setTitle(deviceName+"设备验收审批结果");
            String result = request.getReviewResult() == 1 ? "通过" : "不通过";

            req.setContent(String.format("%s设备验收审批结果为：%s。审核意见为：%s。", deviceName, result, request.getReviewOpinion()));
            req.setCreator(loginUserNickname);
            req.setPublishNow(true);
            req.setAppId(appId);
            notificationService.createNotification(req);
            //TODO 增加操作记录
        }
    }

    public DuplicateApprovalDetails approvalDetails(Long id) {
        Optional<String> extra = jdbcClient.sql("select extra from biz_license_duplicate where id = ?")
                .param(id)
                .query(String.class)
                .optional();
        if (extra.isEmpty()) {
            return new  DuplicateApprovalDetails();
        }
        DuplicateApprovalDetails ret;
        try {
            ret = objectMapper.readValue(extra.orElse(null), DuplicateApprovalDetails.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isNotBlank(ret.getExpertIds())) {
            String[] split = ret.getExpertIds().split(",");
            List<Map<String, String>> nameList = jdbcClient.sql("select id, name from biz_expert_ext where id in (:ids)")
                    .param("ids", Arrays.asList(split))
                    .query(JdbcClientHelper::resultSetToMap)
                    .list();
            ret.setExpertList(nameList);
        }
        return ret;
    }
}
