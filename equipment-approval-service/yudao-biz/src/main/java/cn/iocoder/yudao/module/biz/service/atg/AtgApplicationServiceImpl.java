package cn.iocoder.yudao.module.biz.service.atg;

import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationCreateReqVO;
import cn.iocoder.yudao.module.biz.controller.admin.atg.vo.AtgApplicationUpdateReqVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.application.ApplicationDO;
import cn.iocoder.yudao.module.biz.dal.mysql.application.ApplicationMapper;
import cn.iocoder.yudao.module.biz.service.devicelicense.DeviceLicenseService;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.biz.enums.ErrorCodeConstants.APPLICATION_NOT_EXISTS;

/**
 * 高效通办系统 - 申请 Service 实现
 */
@Service
@Validated
@Slf4j
public class AtgApplicationServiceImpl implements AtgApplicationService {

    @Resource
    private ApplicationMapper applicationMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private DeviceLicenseService deviceLicenseService;

    @Resource
    private ObjectMapper objectMapper;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAtgApplication(AtgApplicationCreateReqVO reqVO) {
        ApplicationDO application = new ApplicationDO();
        application.setAppNo("ATG-" + timeFormatter.format(LocalDateTime.now()));
        application.setAppType(reqVO.getAppType() != null ? reqVO.getAppType() : 1);
        application.setAppStatus(5);
        application.setInstitutionId(reqVO.getInstitutionId());
        application.setSource(1);
        application.setDeadline(LocalDate.now().plusDays(45));

        ObjectNode extra = objectMapper.createObjectNode();
        extra.put("projId", reqVO.getProjId());
        if (reqVO.getApplicantName() != null) {
            extra.put("applicantName", reqVO.getApplicantName());
        }
        if (reqVO.getContactPhone() != null) {
            extra.put("contactPhone", reqVO.getContactPhone());
        }
        if (reqVO.getAttachments() != null) {
            extra.put("attachments", reqVO.getAttachments());
        }
        if (reqVO.getFormInfo() != null) {
            extra.put("formInfo", reqVO.getFormInfo());
        }
        if (reqVO.getInstitutionName() != null) {
            extra.put("institutionName", reqVO.getInstitutionName());
        }
        application.setExtra(extra);
        application.setCreator("0");
        application.setUpdater("0");

        applicationMapper.insert(application);
        log.info("高办系统创建申请成功, id={}, projId={}", application.getId(), reqVO.getProjId());
        return application.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String completeAndGenerateLicense(AtgApplicationUpdateReqVO reqVO) {
        // 1. 校验
        ApplicationDO application = applicationMapper.selectById(reqVO.getId());
        if (application == null) {
            throw exception(APPLICATION_NOT_EXISTS);
        }
        if (!Integer.valueOf(1).equals(application.getSource())) {
            throw new RuntimeException("该申请不是高办系统来源");
        }

        // 2. 保存设备信息
        ApplicationDO updateDO = new ApplicationDO();
        updateDO.setId(reqVO.getId());
        updateDO.setLicenseDeviceName(reqVO.getLicenseDeviceName());
        updateDO.setLadderConfigModel(reqVO.getLadderConfigModel());

        ObjectNode extra = application.getExtra() != null ? application.getExtra() : objectMapper.createObjectNode();
        if (reqVO.getEquipmentConfigAddress() != null) {
            extra.put("equipmentConfigAddress", reqVO.getEquipmentConfigAddress());
        }
        if (reqVO.getContactPerson() != null) {
            extra.put("contactPerson", reqVO.getContactPerson());
        }
        if (reqVO.getContactPhone() != null) {
            extra.put("contactPhone", reqVO.getContactPhone());
        }
        updateDO.setExtra(extra);
        applicationMapper.updateById(updateDO);

        // 3. 生成许可证编号
        Map<String, String> regionInfo = jdbcClient.sql("""
                SELECT region FROM biz_institution_ext WHERE dept_id = ?
                """).param(application.getInstitutionId()).query(JdbcClientHelper::resultSetToMap).single();

        String licenseNo = deviceLicenseService.generateLicenseNumber("乙", regionInfo.get("region"),
                reqVO.getLicenseDeviceName(), reqVO.getLadderConfigModel());

        // 4. 更新许可证信息
        ApplicationDO licenseDO = new ApplicationDO();
        licenseDO.setId(reqVO.getId());
        licenseDO.setLicenseNo(licenseNo);
        licenseDO.setLicenseGenerateDate(LocalDate.now());
        licenseDO.setLicenseValidDate(LocalDate.now().plusYears(10));
        applicationMapper.updateById(licenseDO);

        // 5. 查询机构信息并插入正本
        String equipmentConfigAddress = reqVO.getEquipmentConfigAddress() != null ? reqVO.getEquipmentConfigAddress() : "";

        Map<String, String> instInfo = jdbcClient.sql("""
                SELECT institution_name, unified_social_credit_code, legal_person,
                       ownership_nature, detailed_address, city
                FROM biz_institution_ext WHERE dept_id = ?
                """).param(application.getInstitutionId()).query(JdbcClientHelper::resultSetToMap).single();

        String sql = """
                INSERT INTO biz_license_original (
                    application_id, license_no, config_unit_name, ownership_nature,
                    issuing_authority, unified_social_credit_code, ladder_config_model,
                    issue_date, legal_person, equipment_config_address,
                    license_device_name, detailed_address, valid_date
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcClient.sql(sql).params(
                reqVO.getId(), licenseNo, instInfo.get("institutionName"), instInfo.get("ownershipNature"),
                "陕西省卫生健康委员会", instInfo.get("unifiedSocialCreditCode"),
                reqVO.getLadderConfigModel(), LocalDate.now(),
                instInfo.get("legalPerson"),
                equipmentConfigAddress.isEmpty() ? "陕西省" + instInfo.get("city") : equipmentConfigAddress,
                reqVO.getLicenseDeviceName(), instInfo.get("detailedAddress"),
                LocalDateTime.now().plusYears(10)
        ).update();

        // 6. 修改序列号表状态
        jdbcClient.sql("update biz_device_license set status = 'USED' where license_number = ?")
                .param(licenseNo).update();

        log.info("高办系统补充信息并生成许可证成功, id={}, licenseNo={}", reqVO.getId(), licenseNo);
        return licenseNo;
    }
}
