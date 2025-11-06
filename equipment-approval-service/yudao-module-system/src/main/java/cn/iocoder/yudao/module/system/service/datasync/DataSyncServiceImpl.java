package cn.iocoder.yudao.module.system.service.datasync;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.datasync.PushRecordDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.dataobject.user.ExternalUser;
import cn.iocoder.yudao.module.system.dal.mysql.datasync.SyncRecordMapper;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.ExternalUserMapper;
import cn.iocoder.yudao.module.system.service.datasync.dto.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DataSyncServiceImpl implements DataSyncService{

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private SyncRecordMapper syncRecordMapper;

    @Resource
    private ExternalUserMapper externalUserMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public PushResultVO syncUser(SyncUserDTO user) {
        PushRecordDO recordDO = new PushRecordDO();
        recordDO.setPushType("user");
        recordDO.setExternalId(user.getUserId());
        recordDO.setOperation(user.getOperation());
        recordDO.setRequestData(JSONUtil.toJsonStr(user));
        recordDO.setPushTime(LocalDateTime.now());

        try {
            transactionTemplate.execute(status -> {
                switch (user.getOperation()) {
                    case "create":
                    case "update":
                        saveOrUpdateUser(user);
                        break;
                    case "delete":
                        deleteUser(user.getUserId());
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的操作类型: " + user.getOperation());
                }
                return null;
            });


            recordDO.setStatus(1);
            recordDO.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(recordDO);

            return PushResultVO.success(user.getUserId());

        } catch (Exception e) {
            recordDO.setStatus(0);
            recordDO.setErrorMsg(e.getMessage());
            recordDO.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(recordDO);
            log.error("同步用户失败,用户id:{}, 操作:{}", user.getUserId(), user.getOperation());
            log.error("同步用户异常", e);
            return PushResultVO.fail(user.getUserId(), e.getMessage());
        }
    }

    private void deleteUser(String userId) {
        externalUserMapper.delete(ExternalUser::getId, userId);
    }

    private void saveOrUpdateUser(SyncUserDTO user) {
        //TODO user表加字段
        ExternalUser externalUser = BeanUtils.toBean(user, ExternalUser.class);
        externalUser.setId(user.getUserId());
        externalUser.setCreateTime(LocalDateTime.now());
        externalUserMapper.insertOrUpdate(externalUser);
    }

    @Override
    public PushResultVO syncDept(SyncDeptDTO dept, UserTypeEnum userType) {
        PushRecordDO recordDO = new PushRecordDO();
        recordDO.setPushType("dept");
        recordDO.setExternalId(dept.getOrgId());
        recordDO.setOperation(dept.getOperation());
        recordDO.setRequestData(JSONUtil.toJsonStr(dept));
        recordDO.setPushTime(LocalDateTime.now());

        try {
            transactionTemplate.execute(status -> {
                switch (dept.getOperation()) {
                    case "create":
                    case "update":
                        saveOrUpdateDept(dept, userType);
                        break;
                    case "delete":
                        deleteDept(dept.getOrgId());
                        break;
                    default:
                        throw new IllegalArgumentException("不支持的操作类型: " + dept.getOperation());
                }
                return null;
            });


            recordDO.setStatus(1);
            recordDO.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(recordDO);

            return PushResultVO.success(dept.getOrgId());

        } catch (Exception e) {
            recordDO.setStatus(0);
            recordDO.setErrorMsg(e.getMessage());
            recordDO.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(recordDO);
            log.error("同步机构失败,机构id:{}, 操作:{}", dept.getOrgId(), dept.getOperation());
            log.error("同步机构异常", e);
            return PushResultVO.fail(dept.getOrgId(), e.getMessage());
        }
    }

    private void deleteDept(String orgId) {
        DeptDO dept = deptMapper.selectOne(DeptDO::getExternalId, orgId);
        if (dept != null && dept.getId() != null) {
            jdbcClient.sql("delete from system_dept where id = ?").param(dept.getId()).update();
            jdbcClient.sql("delete from biz_institution_ext where dept_id = ?").param(dept.getId()).update();
            AdminUserDO adminUserDO = adminUserMapper.selectOne(AdminUserDO::getDeptId, dept.getId());
            Optional.ofNullable(adminUserDO)
                    .map(AdminUserDO::getId)
                    .map(id -> jdbcClient.sql("delete from system_users where id = ?").param(id).update());
        }
    }

    private void saveOrUpdateDept(SyncDeptDTO dept, UserTypeEnum userType) {
        DeptDO existDept = deptMapper.selectOne(DeptDO::getExternalId, dept.getOrgId());
        DeptDO deptDO = new DeptDO();
        Long parentId = null;
        if (StrUtil.isNotBlank(dept.getParent())) {
            DeptDO parentDept = deptMapper.selectOne(DeptDO::getExternalId, dept.getParent());
            parentId = Optional.ofNullable(parentDept)
                    .map(DeptDO::getId)
                    .orElse(null);
        }
        deptDO.setParentId(parentId!= null ? parentId:0L);
        deptDO.setName(dept.getCaption());
        deptDO.setExternalPid(dept.getParent());
        deptDO.setExternalId(dept.getOrgId());
        deptDO.setPhone(dept.getTxDhhm());
        deptDO.setStatus(0);
        if (existDept != null) {
            deptDO.setId(existDept.getId());
            deptMapper.updateById(deptDO);
            AdminUserDO existUser = adminUserMapper.selectOne(AdminUserDO::getDeptId, deptDO.getId());
            if (existUser != null) {
                existUser.setDeptId(deptDO.getId());
                existUser.setNickname(deptDO.getName());
                existUser.setUsername(deptDO.getName());
                adminUserMapper.updateById(existUser);
                updateInstitutionExt(deptDO.getId(), dept);
            }
        } else {
            deptDO.setCreateTime(dept.getCreateDate() != null ? dept.getCreateDate() : LocalDateTime.now());
            deptMapper.insert(deptDO);
            insertInstitutionExt(deptDO.getId(), dept);
            //插入部门时 为每个部门创建一个用户，确保同部门外部用户单点登录时登录同一个账号
            AdminUserDO existUser = adminUserMapper.selectOne(AdminUserDO::getDeptId, deptDO.getId());
            if (existUser == null) {
                AdminUserDO userDO = new AdminUserDO();
                userDO.setDeptId(deptDO.getId());
                userDO.setNickname(deptDO.getName());
                userDO.setUsername(deptDO.getName());
                userDO.setType(userType.getValue());
                userDO.setStatus(CommonStatusEnum.ENABLE.getStatus());
                userDO.setCreateTime(LocalDateTime.now());
                //所有密码设置为123456
                userDO.setPassword(BCrypt.hashpw("123456"));
                userDO.setDeleted(false);
                adminUserMapper.insert(userDO);
            }
        }


    }
    private void insertInstitutionExt(Long deptId, SyncDeptDTO dept) {
        String insertSql = """
            INSERT INTO biz_institution_ext (
                dept_id, institution_name, unified_social_credit_code,
                institution_level, region, license_no, legal_person, 
                address, detailed_address, contact_person, contact_phone
            ) VALUES (
                :deptId, :institutionName, :unifiedSocialCreditCode,
                :institutionLevel, :region, :licenseNo, :legalPerson,
                :address, :detailedAddress, :contactPerson, :contactPhone
            )
            """;

        jdbcClient.sql(insertSql)
                .params(buildInstitutionParams(deptId, dept))
                .update();
    }

    private void updateInstitutionExt(Long deptId, SyncDeptDTO dept) {
        String updateSql = """
            UPDATE biz_institution_ext SET
                institution_name = :institutionName,
                unified_social_credit_code = :unifiedSocialCreditCode,
                institution_level = :institutionLevel,
                region = :region,
                license_no = :licenseNo,
                legal_person = :legalPerson,
                address = :address,
                detailed_address = :detailedAddress,
                contact_person = :contactPerson,
                contact_phone = :contactPhone
            WHERE dept_id = :deptId
            """;

        jdbcClient.sql(updateSql)
                .params(buildInstitutionParams(deptId, dept))
                .update();
    }

    // 构建参数 Map
    private Map<String, Object> buildInstitutionParams(Long deptId, SyncDeptDTO dept) {
        String region = Optional.ofNullable(dept.getDeptAddressCode())
                .map(code -> AreaUtils.getArea(Integer.valueOf(code)))
                .map(Area::getName)
                .orElse(null);

        String institutionLevel = (dept.getYydjJ() != null && dept.getYydjD() != null)
                ? dept.getYydjJ() + dept.getYydjD()
                : null;

        Map<String, Object> params = new HashMap<>();
        params.put("deptId", deptId);
        params.put("institutionName", dept.getCaption());
        params.put("unifiedSocialCreditCode", dept.getDeptShxydm());
        params.put("institutionLevel", institutionLevel);
        params.put("region", region);
        params.put("licenseNo", dept.getDeptZyxkzdjh());
        params.put("legalPerson", dept.getFzr());
        params.put("address", dept.getTxDz());
        params.put("detailedAddress", dept.getTxDz());
        params.put("contactPerson", dept.getGovernor());
        params.put("contactPhone", dept.getTel());
        return params;
    }


//    private void insertInstitutionExt(Long deptId, SyncDeptDTO dept) {
//        String insertSql = """
//                INSERT INTO biz_institution_ext (
//                    dept_id, institution_name, unified_social_credit_code,
//                    institution_level, region,license_no, legal_person, address, detailed_address, contact_person,
//                    contact_phone
//                ) VALUES
//                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
//                """;
//        String area = Optional.ofNullable(dept.getDeptAddressCode())
//                .map(code -> AreaUtils.getArea(Integer.valueOf(code)))
//                .map(Area::getName)
//                .orElse(null);
//        String institutionLevel = null;
//        if (dept.getYydjJ() != null && dept.getYydjD() != null) {
//            institutionLevel = dept.getYydjJ() + dept.getYydjD();
//        }
//        jdbcClient.sql(insertSql).params(deptId, dept.getCaption(), dept.getDeptShxydm(), institutionLevel, area,
//                dept.getDeptZyxkzdjh(), dept.getFzr(), dept.getTxDz(), dept.getTxDz(), dept.getGovernor(), dept.getTel()).update();
//    }
//
//    private void updateInstitutionExt(Long deptId, SyncDeptDTO dept) {
//        String updateSql = """
//                UPDATE biz_institution_ext SET institution_name = ?, unified_social_credit_code = ?, institution_level = ?,
//                  region = ?,  license_no = ?, legal_person = ?, address = ?,
//                  detailed_address = ?, contact_person = ?, contact_phone = ? WHERE dept_id = ?
//                """;
//        String area = Optional.ofNullable(dept.getDeptAddressCode())
//                .map(code -> AreaUtils.getArea(Integer.valueOf(code)))
//                .map(Area::getName)
//                .orElse(null);
//        String institutionLevel = null;
//        if (dept.getYydjJ() != null && dept.getYydjD() != null) {
//            institutionLevel = dept.getYydjJ() + dept.getYydjD();
//        }
//        jdbcClient.sql(updateSql).params(dept.getCaption(), dept.getDeptShxydm(), institutionLevel, area,
//                dept.getDeptZyxkzdjh(), dept.getFzr(), dept.getTxDz(), dept.getTxDz(), dept.getGovernor(), dept.getTel(), deptId).update();
//    }


    @Override
    public BatchPushResultVO batchSync(ExternalBatchPushDTO dto) {
        BatchPushResultVO result = new BatchPushResultVO();
        List<String> adminOrgId = dto.getUsers().stream().filter(user -> user.getUserId() != null && user.getUserId().contains("_"))
                .map(SyncUserDTO::getOrgId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // ========== 方案：多轮迭代处理机构 ==========
        if (CollUtil.isNotEmpty(dto.getDepts())) {
            List<SyncDeptDTO> pendingDepts = new ArrayList<>(dto.getDepts());
            List<SyncDeptDTO> failedDepts = new ArrayList<>();
            int maxRounds = 10;  // 最多10轮，避免死循环
            int round = 0;

            while (!pendingDepts.isEmpty() && round < maxRounds) {
                round++;
                List<SyncDeptDTO> nextRoundDepts = new ArrayList<>();

                log.info("批量同步机构 - 第{}轮，待处理{}条", round, pendingDepts.size());

                for (SyncDeptDTO dept : pendingDepts) {
                    try {
                        // 检查父级是否存在（如果需要父级的话）
                        if (StrUtil.isNotBlank(dept.getParent())) {
                            DeptDO parentDept = deptMapper.selectOne(DeptDO::getExternalId, dept.getParent());
                            if (parentDept == null) {
                                // 父级还不存在，放到下一轮处理
                                nextRoundDepts.add(dept);
                                log.debug("机构{}的父级{}不存在，延迟到下一轮", dept.getOrgId(), dept.getParent());
                                continue;
                            }
                        }
                        // 👇 加事务：确保单条记录的原子性
                        transactionTemplate.execute(status -> {
                            Integer userType;
                            if (adminOrgId.contains(dept.getOrgId())) {
                                userType = UserTypeEnum.ADMIN.getValue();
                            } else {
                                userType = UserTypeEnum.MEMBER.getValue();
                            }
                            saveOrUpdateDept(dept, UserTypeEnum.valueOf(userType));
                            return null;
                        });
                        result.addDeptSuccess();

                    } catch (Exception e) {
                        log.error("机构同步失败: {}", dept.getOrgId(), e);
                        failedDepts.add(dept);
                    }
                }

                pendingDepts = nextRoundDepts;
            }

            // 处理完所有轮次后仍未成功的
            if (!pendingDepts.isEmpty()) {
                log.warn("批量同步完成，仍有{}条机构无法处理（父级缺失）", pendingDepts.size());
                for (SyncDeptDTO dept : pendingDepts) {
                    result.addDeptFail(dept.getOrgId(),
                            "父级机构不存在: " + dept.getParent());
                }
            }

            // 记录彻底失败的
            for (SyncDeptDTO dept : failedDepts) {
                result.addDeptFail(dept.getOrgId(), "处理失败");
            }
        }

        // ========== 处理用户（同样需要检查机构是否存在）==========
        if (CollUtil.isNotEmpty(dto.getUsers())) {
            for (SyncUserDTO user : dto.getUsers()) {
                try {
                    // 检查用户所属机构是否存在
                    if (StrUtil.isNotBlank(user.getOrgId())) {
                        DeptDO dept = deptMapper.selectOne(DeptDO::getExternalId, user.getOrgId());

                        if (dept == null) {
                            result.addUserFail(user.getUserId(),
                                    "所属机构不存在: " + user.getOrgId());
                            continue;
                        }
                    }

                    // 👇 加事务
                    transactionTemplate.execute(status -> {
                        saveOrUpdateUser(user);
                        return null;
                    });
                    result.addUserSuccess();

                } catch (Exception e) {
                    log.error("用户同步失败: {}", user.getUserId(), e);
                    result.addUserFail(user.getUserId(), e.getMessage());
                }
            }
        }


        // 记录批量日志
        PushRecordDO recordDO = new PushRecordDO();
        recordDO.setPushType("batch");
        recordDO.setOperation("batch_sync");
        recordDO.setStatus(result.hasError() ?  1 : 0);
        recordDO.setRequestData(String.format("机构%d条（成功%d，失败%d），用户%d条（成功%d，失败%d）",
                CollUtil.size(dto.getDepts()), result.getDeptSuccessCount(), result.getDeptFailCount(),
                CollUtil.size(dto.getUsers()), result.getUserSuccessCount(), result.getUserFailCount()));
        recordDO.setPushTime(LocalDateTime.now());
        recordDO.setProcessTime(LocalDateTime.now());
        if (result.hasError()) {
            recordDO.setErrorMsg(result.getErrorSummary());
        }
        syncRecordMapper.insert(recordDO);

        return result;
    }

}
