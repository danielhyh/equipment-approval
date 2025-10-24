package cn.iocoder.yudao.module.system.service.datasync;

import cn.iocoder.yudao.module.system.dal.mysql.datasync.SyncRecordMapper;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import cn.iocoder.yudao.module.system.service.datasync.dto.SyncDeptDTO;
import cn.iocoder.yudao.module.system.service.datasync.dto.SyncResult;
import cn.iocoder.yudao.module.system.service.datasync.dto.SyncUserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

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

    @Override
    public SyncResult syncUser(SyncUserDTO user) {
        SyncResult syncResult = new SyncResult();
        return syncResult;
    }

    @Override
    public SyncResult syncDept(SyncDeptDTO dept) {
        SyncResult syncResult = new SyncResult();
        return syncResult;
    }

}
