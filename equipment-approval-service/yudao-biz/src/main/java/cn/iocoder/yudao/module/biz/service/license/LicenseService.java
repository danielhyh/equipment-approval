package cn.iocoder.yudao.module.biz.service.license;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.DuplicateLicenseVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.LicensePageRequestVO;
import cn.iocoder.yudao.module.biz.controller.admin.license.vo.OriginalLicenseVO;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppDuplicateSubmitRequest;
import cn.iocoder.yudao.module.biz.controller.app.license.vo.AppLicensePageRespVO;
import cn.iocoder.yudao.module.biz.dal.dataobject.classaequipment.ClassAEquipmentDO;
import cn.iocoder.yudao.module.biz.dal.mysql.classaequipment.ClassAEquipmentMapper;
import cn.iocoder.yudao.module.biz.dal.mysql.license.LicenseMapper;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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

    @Transactional
    public boolean insertDuplicateLicense(AppDuplicateSubmitRequest request) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        boolean res = licenseMapper.insertDuplicateLicense(request, loginUserId) > 0;
        insertEquipment(request);
        return res;
    }

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
}
