package cn.iocoder.yudao.module.biz.service.license;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.*;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppDuplicateSubmitRequest;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppLicensePageRespVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.classaequipment.ClassAEquipmentMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.license.LicenseMapper;
import cn.iocoder.yudao.module.biz.service.devicelicense.DeviceLicenseService;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

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

    public PageResult<AppLicensePageRespVO> licensePage(Integer pageSize, Integer pageNum, String type) {
        IPage<AppLicensePageRespVO> page = new Page<>(pageNum, pageSize);
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        licenseMapper.licensePage(page, type, loginUserId);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }


    public PageResult<LicensePageVO> page(LicensePageRequestVO param) {
        IPage<LicensePageVO> page = new Page<>(param.getPageNum(), param.getPageSize());
        licenseMapper.page(page, param);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    public OriginalLicenseVO getOriginalById(Long id) {
        return licenseMapper.getOriginalById(id);
    }

    public DuplicateLicenseVO getDuplicateById(Long id) {
        return licenseMapper.getDuplicateById(id);
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
        equipmentDO.setConfigUnitName(res.get("institutionName"));
        equipmentDO.setContactPerson(res.get("contactPerson"));
        equipmentDO.setContactPhone(res.get("contactPhone"));
        equipmentDO.setUnifiedSocialCreditCode(res.get("unifiedSocialCreditCode"));
        equipmentDO.setLegalPerson(res.get("legalPerson"));
        equipmentDO.setLicenseDeviceName(res.get("licenseDeviceName"));
        equipmentDO.setOwnershipNature(res.get("ownershipNature"));
        equipmentDO.setEquipmentConfigAddress(res.get("detailedAddress"));
        classAEquipmentMapper.insert(equipmentDO);

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
        
        // 1. 创建线下申请记录
        ApplicationDO application = new ApplicationDO();
        application.setAppNo("SQ-" + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        application.setInstitutionId(request.getOriginalLicense().getConfigUnitName() != null ? 
            getInstitutionIdByName(request.getOriginalLicense().getConfigUnitName()) : null);
        application.setAppType(5); // 线下申请
        application.setLicenseDeviceName(request.getOriginalLicense().getLicenseDeviceName());
        application.setLadderConfigModel(request.getOriginalLicense().getLadderConfigModel());
        application.setConfigReason("线下申请");
        application.setAppStatus(5); // 直接设为专家审核通过
        application.setInitialReviewResult(1); // 初审通过
        application.setExpertReviewResult(1); // 专家审核通过
        application.setInitialReviewTime(java.time.LocalDateTime.now());
        application.setExpertReviewTime(java.time.LocalDateTime.now());
        Date issueDate = request.getOriginalLicense().getIssueDate();
        LocalDate localDate = issueDate.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
        application.setLicenseGenerateDate(localDate);
        application.setDeadline(java.time.LocalDate.now().plusDays(45));
        transactionTemplate.execute(status ->{
           try {
               // 插入申请记录
               applicationMapper.insert(application);
               Long applicationId = application.getId();

               // 获取刚插入的申请ID
               logger.info("线下办理创建线下申请记录，申请ID: {}", application.getId());

               // 2. 生成正本许可证
               OriginalLicenseVO originalLicense = request.getOriginalLicense();
               originalLicense.setEquipmentConfigAddress("陕西省" + originalLicense.getEquipmentConfigAddress());
               originalLicense.setId(null); // 确保ID为空，让数据库自动生成

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
               duplicateLicense.setId(null); // 确保ID为空
               AppDuplicateSubmitRequest duplicateSubmitRequest = BeanUtils.toBean(duplicateLicense, AppDuplicateSubmitRequest.class);
               duplicateSubmitRequest.setOriginalId(originalId);
               // 插入副本记录
               insertDuplicateLicense(duplicateSubmitRequest);
               logger.info("线下办理副本插入成功, ID: {}", duplicateSubmitRequest.getId());
               //插入装备

               logger.info("线下许可证处理完成 - 申请ID: {}, 正本ID: {}", applicationId, originalId);
           } catch (Exception e) {
               status.setRollbackOnly();
               logger.info("线下办理插入失败",e);
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
}
