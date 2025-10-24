package cn.iocoder.yudao.module.system.service.datasync;

import cn.iocoder.yudao.module.system.service.datasync.dto.*;

public interface DataSyncService {

    /**
     * 同步用户数据
     */
    PushResultVO syncUser(SyncUserDTO user);

    /**
     * 同步部门数据
     */
    PushResultVO syncDept(SyncDeptDTO dept);


    /*
     * 批量同步数据
     */
    BatchPushResultVO batchSync(ExternalBatchPushDTO batchPushDTO);

}
