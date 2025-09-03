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
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class LicenseService {

    @Resource
    private LicenseMapper licenseMapper;

    @Resource
    private ClassAEquipmentMapper classAEquipmentMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource(name = "bizExecutor")
    private Executor bizExecutor;

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

    public boolean insertDuplicateLicense(AppDuplicateSubmitRequest request) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        CompletableFuture.runAsync(() -> insertEquipment(request), bizExecutor)
                .exceptionally(throwable -> {
                    log.error("【异步任务失败】插入设备异常，request={}", JSON.toJSONString(request), throwable);
                    return null;
                });


        return licenseMapper.insertDuplicateLicense(request, loginUserId) > 0;
    }

    public void insertEquipment(AppDuplicateSubmitRequest req) {
        ClassAEquipmentDO equipmentDO = getClassAEquipmentDO(req);
        String sql = """
                select a.*, b.id as app_id,b.license_device_name from biz_institution_ext a
                left join biz_application b on a.dept_id = b.institution_id
                left join biz_license_original c on b.id = c.application_id
                where c.id = ?
                """;
        Map<String, String> res = jdbcClient.sql(sql).param(req.getOriginalId()).query((rs, rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, String> map = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String camelKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
                Object val = rs.getObject(i);
                map.put(camelKey, val != null ? val.toString() : "");
            }
            return map;
        }).single();
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
                .param("id", res.get("appId"))
                .update();
    }

    private static ClassAEquipmentDO getClassAEquipmentDO(AppDuplicateSubmitRequest req) {
        ClassAEquipmentDO equipmentDO = new ClassAEquipmentDO();
        equipmentDO.setProductionEnterprise(req.getProductionEnterprise());
        equipmentDO.setSpecificModel(req.getSpecificModel());
        equipmentDO.setInstallationDate(req.getInstallationDate());
        equipmentDO.setPurchasePrice(new BigDecimal(req.getPurchasePrice()));
        equipmentDO.setStatus(1);
        equipmentDO.setEquipmentUsers(req.getEquipmentUsers());
        equipmentDO.setSpecialDescription(req.getSpecialDescription());
        return equipmentDO;
    }
}
