package cn.iocoder.yudao.module.system.service.datasync;

import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.dal.dataobject.datasync.PushRecordDO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
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

import java.time.LocalDateTime;

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

    @Override
    public PushResultVO syncUser(SyncUserDTO user) {
        PushRecordDO log = new PushRecordDO();
        log.setPushType("user");
        log.setExternalId(user.getUserId());
        log.setOperation(user.getOperation());
        log.setRequestData(JSONUtil.toJsonStr(user));
        log.setPushTime(LocalDateTime.now());

        try {
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

            log.setStatus(1);
            log.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(log);

            return PushResultVO.success(user.getUserId());

        } catch (Exception e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            log.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(log);

            return PushResultVO.fail(user.getUserId(), e.getMessage());
        }
    }

    private void deleteUser(String userId) {
        externalUserMapper.delete(ExternalUser::getId, userId);
    }

    private void saveOrUpdateUser(SyncUserDTO user) {
        //TODO user表加字段
        ExternalUser externalUser = BeanUtils.toBean(user, ExternalUser.class);
        externalUserMapper.insertOrUpdate(externalUser);
    }

    @Override
    public PushResultVO syncDept(SyncDeptDTO dept) {
        PushRecordDO log = new PushRecordDO();
        log.setPushType("dept");
        log.setExternalId(dept.getOrgId());
        log.setOperation(dept.getOperation());
        log.setRequestData(JSONUtil.toJsonStr(dept));
        log.setPushTime(LocalDateTime.now());

        try {
            switch (dept.getOperation()) {
                case "create":
                case "update":
                    saveOrUpdateDept(dept);
                    break;
                case "delete":
                    deleteDept(dept.getOrgId());
                    break;
                default:
                    throw new IllegalArgumentException("不支持的操作类型: " + dept.getOperation());
            }

            log.setStatus(1);
            log.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(log);

            return PushResultVO.success(dept.getOrgId());

        } catch (Exception e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            log.setProcessTime(LocalDateTime.now());
            syncRecordMapper.insert(log);

            return PushResultVO.fail(dept.getOrgId(), e.getMessage());
        }
    }

    private void deleteDept(String orgId) {

    }

    private void saveOrUpdateDept(SyncDeptDTO dept) {
        DeptDO deptDO = new DeptDO();
        deptDO.setName(dept.getCaption());
        deptDO.setExternalId(dept.getOrgId());
        deptDO.setExternalPid(dept.getParent());
        deptDO.setPhone(dept.getTxDhhm());
        deptDO.setEmail(dept.getTxDz());
    }

    @Override
    public BatchPushResultVO batchSync(ExternalBatchPushDTO batchPushDTO) {
        return null;
    }

}
