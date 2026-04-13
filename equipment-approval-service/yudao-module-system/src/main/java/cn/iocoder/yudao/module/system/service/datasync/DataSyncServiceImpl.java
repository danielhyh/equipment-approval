package cn.iocoder.yudao.module.system.service.datasync;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.JdbcClientHelper;
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
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataSyncServiceImpl implements DataSyncService {

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

    private final Map<String, String> cityMap = new ConcurrentHashMap<>(256);

    @PostConstruct
    public void init() {
        List<Map<String, String>> list = jdbcClient.sql("select code, name from system_regions where level = 2")
                .query(JdbcClientHelper::resultSetToMap)
                .list();
        list.forEach(row -> {
            cityMap.put(row.get("code"), row.get("name"));
        });
    }

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
                        saveOrUpdateDept(dept, userType, true);
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

    private void saveOrUpdateDept(SyncDeptDTO dept, UserTypeEnum userType, boolean notBatch) {
        DeptDO existDept = deptMapper.selectOne(DeptDO::getExternalId, dept.getOrgId());
        DeptDO deptDO = new DeptDO();
        Long parentId = null;
        if (StrUtil.isNotBlank(dept.getParent()) && notBatch) {
            DeptDO parentDept = deptMapper.selectOne(DeptDO::getExternalId, dept.getParent());
            parentId = Optional.ofNullable(parentDept)
                    .map(DeptDO::getId)
                    .orElse(null);
        }
        deptDO.setParentId(parentId != null ? parentId : 0L);
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
                    address, detailed_address, contact_person, contact_phone,
                    subject, management_level, category, hos_class, hos_grade
                ) VALUES (
                    :deptId, :institutionName, :unifiedSocialCreditCode,
                    :institutionLevel, :region, :licenseNo, :legalPerson,
                    :address, :detailedAddress, :contactPerson, :contactPhone,
                    :subject, :managementLevel, :category, :hosClass, :hosGrade
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
                    contact_phone = :contactPhone,
                    subject = :subject,
                    management_level = :managementLevel,
                    category = :category,
                    hos_class = :hosClass,
                    hos_grade = :hosGrade,
                    city = :city
                WHERE dept_id = :deptId
                """;

        jdbcClient.sql(updateSql)
                .params(buildInstitutionParams(deptId, dept))
                .update();
    }

    Map<String, String> levelMap = Map.of("1","一级","2","二级","3","三级");

    Map<String, String> classMap = Map.of("1", "特等", "2", "甲等", "3","乙等","4" ,"丙等");
    // 构建参数 Map
    private Map<String, Object> buildInstitutionParams(Long deptId, SyncDeptDTO dept) {
        String region = Optional.ofNullable(dept.getDeptAddressCode())
                .map(code -> AreaUtils.getArea(Integer.valueOf(code)))
                .map(Area::getName)
                .orElse(null);

        String institutionLevel = (dept.getYydjJ() != null && dept.getYydjD() != null)
                ? levelMap.getOrDefault(dept.getYydjJ(), "未评") + classMap.getOrDefault(dept.getYydjD(), "")
                : null;
        String address = Optional.ofNullable(dept.getGbQhdm())
                .map(Integer::parseInt)
                .map(AreaUtils::getArea)
                .map(Area::getName)
                .orElse(null);

        // 办医主体：JJLX 为 11 或 12 则公立，否则民营
        String subject = null;
        if (StrUtil.isNotBlank(dept.getJjlx())) {
            subject = ("11".equals(dept.getJjlx()) || "12".equals(dept.getJjlx())) ? "公立" : "民营";
        }

        // 管理级次：根据 ZBDW 和 LSGX 判断
        String managementLevel = null;
        if (StrUtil.isNotBlank(dept.getZbdw()) && StrUtil.isNotBlank(dept.getLsgx())) {
            if (("1".equals(dept.getZbdw()) || "2".equals(dept.getZbdw()))) {
                switch (dept.getLsgx()) {
                    case "1" -> managementLevel = "中央级";
                    case "2" -> managementLevel = "省级";
                    case "3" -> managementLevel = "市级";
                    case "4", "5" -> managementLevel = "县级";
                }
            }
        }

        // 医院性质：根据 DEPT_CLASS 前两位判断
        String category = null;
        if (StrUtil.isNotBlank(dept.getDeptClass()) && dept.getDeptClass().length() >= 2) {
            String prefix = dept.getDeptClass().substring(0, 2);
            if ("A1".equals(prefix)) {
                category = "综合";
            } else if ("A5".equals(prefix)) {
                category = "专科";
            }
        }

        // 医院等级-级：根据 YYDJ_J 判断
        String hosClass = null;
        if (StrUtil.isNotBlank(dept.getYydjJ())) {
            hosClass = switch (dept.getYydjJ()) {
                case "3" -> "三级";
                case "2" -> "二级";
                case "9" -> "未定级";
                default -> null;
            };
        }

        // 医院等级-等：根据 YYDJ_D 判断
        String hosGrade = null;
        if (StrUtil.isNotBlank(dept.getYydjD())) {
            hosGrade = classMap.getOrDefault(dept.getYydjD(), null);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("deptId", deptId);
        params.put("institutionName", dept.getCaption());
        params.put("unifiedSocialCreditCode", dept.getDeptShxydm());
        params.put("institutionLevel", institutionLevel);
        params.put("region", region);
        params.put("licenseNo", dept.getDeptZyxkzdjh());
        params.put("legalPerson", dept.getFzr());
        params.put("address", address);
        params.put("detailedAddress", dept.getTxDz());
        params.put("contactPerson", dept.getGovernor());
        params.put("contactPhone", dept.getTel());
        params.put("subject", subject);
        params.put("managementLevel", managementLevel);
        params.put("category", category);
        params.put("hosClass", hosClass);
        params.put("hosGrade", hosGrade);
        if (StrUtil.isNotEmpty(dept.getDeptAddressCode())) {
            String cityCode = StrUtil.sub(dept.getDeptAddressCode(), 0, 4) + "00";
            params.put("city", cityMap.get(cityCode));
        }
        return params;
    }



    @Override
    public BatchPushResultVO batchSync(ExternalBatchPushDTO dto) {
        BatchPushResultVO result = new BatchPushResultVO();
        List<String> adminOrgId = dto.getUsers().stream().filter(user -> user.getUserId() != null && user.getUserId().length() == 10)
                .map(SyncUserDTO::getOrgId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // ========== 配置线程池 ==========
        int threadPoolSize = 20;
        ExecutorService executor = new ThreadPoolExecutor(
                threadPoolSize,
                threadPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        try {
            // ========== 多线程处理机构 ==========
            if (CollUtil.isNotEmpty(dto.getDepts())) {
                List<CompletableFuture<DeptResult>> deptFutures = dto.getDepts().stream()
                        .map(dept -> CompletableFuture.supplyAsync(() -> {
                            try {
                                transactionTemplate.execute(status -> {
                                    Integer userType = adminOrgId.contains(dept.getOrgId())
                                            ? UserTypeEnum.ADMIN.getValue()
                                            : UserTypeEnum.MEMBER.getValue();
                                    saveOrUpdateDept(dept, UserTypeEnum.valueOf(userType), false);
                                    return null;
                                });
                                return new DeptResult(true, dept.getOrgId(), null);
                            } catch (Exception e) {
                                log.error("机构同步失败: {}", dept.getOrgId(), e);
                                return new DeptResult(false, dept.getOrgId(), "处理失败: " + e.getMessage());
                            }
                        }, executor))
                        .toList();

                // 等待所有机构处理完成
                CompletableFuture.allOf(deptFutures.toArray(new CompletableFuture[0])).join();

                // 统计结果
                deptFutures.forEach(future -> {
                    try {
                        DeptResult deptResult = future.get();
                        if (deptResult.isSuccess()) {
                            result.addDeptSuccess();
                        } else {
                            result.addDeptFail(deptResult.getOrgId(), deptResult.getErrorMsg());
                        }
                    } catch (Exception e) {
                        log.error("获取机构处理结果失败", e);
                    }
                });
                updateDeptParentId();
            }
            // ========== 多线程处理用户（必须在机构处理完成后）==========
            if (CollUtil.isNotEmpty(dto.getUsers())) {
                List<CompletableFuture<UserResult>> userFutures = dto.getUsers().stream()
                        .map(user -> CompletableFuture.supplyAsync(() -> {
                            try {
                                transactionTemplate.execute(status -> {
                                    saveOrUpdateUser(user);
                                    return null;
                                });
                                return new UserResult(true, user.getUserId(), null);
                            } catch (Exception e) {
                                log.error("用户同步失败: {}", user.getUserId(), e);
                                return new UserResult(false, user.getUserId(), e.getMessage());
                            }
                        }, executor))
                        .toList();

                // 等待所有用户处理完成
                CompletableFuture.allOf(userFutures.toArray(new CompletableFuture[0])).join();

                // 统计结果
                userFutures.forEach(future -> {
                    try {
                        UserResult userResult = future.get();
                        if (userResult.isSuccess()) {
                            result.addUserSuccess();
                        } else {
                            result.addUserFail(userResult.getUserId(), userResult.getErrorMsg());
                        }
                    } catch (Exception e) {
                        log.error("获取用户处理结果失败", e);
                    }
                });
            }

        } finally {
            // 关闭线程池
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
//
//
//        // 记录批量日志
        PushRecordDO recordDO = new PushRecordDO();
        recordDO.setPushType("batch");
        recordDO.setOperation("batch_sync");
        recordDO.setStatus(result.hasError() ? 1 : 0);
        recordDO.setRequestData(String.format("机构%d条（成功%d，失败%d），用户%d条（成功%d，失败%d）",
                CollUtil.size(dto.getDepts()), result.getDeptSuccessCount().get(), result.getDeptFailCount().get(),
                CollUtil.size(dto.getUsers()), result.getUserSuccessCount().get(), result.getUserFailCount().get()));
        recordDO.setPushTime(LocalDateTime.now());
        recordDO.setProcessTime(LocalDateTime.now());
        if (result.hasError()) {
            recordDO.setErrorMsg(result.getErrorSummary());
        }
        syncRecordMapper.insert(recordDO);

        return result;
    }


    private void updateDeptParentId() {
        // 1. 查询所有部门
        List<DeptDO> deptList = deptMapper.selectList();
        if (CollUtil.isEmpty(deptList)) {
            return;
        }

        // 2. 建立 externalId 到 DeptDO 的映射
        Map<String, DeptDO> externalIdMap = deptList.stream()
                .filter(dept -> StrUtil.isNotBlank(dept.getExternalId()))
                .collect(Collectors.toMap(DeptDO::getExternalId, Function.identity(), (v1, v2) -> v1));

        // 3. 需要更新的部门列表
        List<DeptDO> updateList = new ArrayList<>();

        // 4. 找到所有根节点（externalPid 为空或为 "0"）
        List<DeptDO> rootDepts = deptList.stream()
                .filter(dept -> StrUtil.isBlank(dept.getExternalPid()) || "--".equals(dept.getExternalPid()))
                .toList();

        // 5. 从根节点开始递归处理
        for (DeptDO rootDept : rootDepts) {
            processChildDepts(rootDept, DeptDO.PARENT_ID_ROOT, externalIdMap, updateList);
        }

        // 6. 处理找不到父节点的孤立部门（防止遗漏）
        for (DeptDO dept : deptList) {
            if (!updateList.contains(dept) &&
                    StrUtil.isNotBlank(dept.getExternalPid())) {
                log.warn("部门[{}]的父部门externalPid[{}]找不到，设置为根节点",
                        dept.getName(), dept.getExternalPid());
                if (!DeptDO.PARENT_ID_ROOT.equals(dept.getParentId())) {
                    dept.setParentId(DeptDO.PARENT_ID_ROOT);
                    updateList.add(dept);
                }
            }
        }

        // 7. 批量更新数据库
        if (!CollUtil.isEmpty(updateList)) {
            log.info("需要更新部门parentId的数量: {}", updateList.size());
//            updateList.forEach(dept -> deptMapper.updateById(dept));
            // 或者批量更新：deptMapper.updateBatchById(updateList);
            deptMapper.updateBatch(updateList, 1000);
        }

    }


    /**
     * 递归处理子部门
     *
     * @param currentDept 当前部门
     * @param parentId 父部门ID
     * @param externalIdMap externalId 到部门的映射
     * @param updateList 需要更新的部门列表
     */
    private void processChildDepts(DeptDO currentDept, Long parentId,
                                   Map<String, DeptDO> externalIdMap,
                                   List<DeptDO> updateList) {
        // 更新当前部门的 parentId
        if (!parentId.equals(currentDept.getParentId())) {
            currentDept.setParentId(parentId);
            updateList.add(currentDept);
        }

        // 查找所有以当前部门的 externalId 作为 externalPid 的子部门
        String currentExternalId = currentDept.getExternalId();
        if (StrUtil.isBlank(currentExternalId)) {
            return;
        }

        // 遍历所有部门，找到子部门
        for (DeptDO dept : externalIdMap.values()) {
            if (currentExternalId.equals(dept.getExternalPid()) &&
                    !dept.getId().equals(currentDept.getId())) { // 避免自己是自己的父节点
                // 递归处理子部门
                processChildDepts(dept, currentDept.getId(), externalIdMap, updateList);
            }
        }
    }

    @Data
    @AllArgsConstructor
    private static class DeptResult {
        private boolean success;
        private String orgId;
        private String errorMsg;
    }

    @Data
    @AllArgsConstructor
    private static class UserResult {
        private boolean success;
        private String userId;
        private String errorMsg;
    }

}
