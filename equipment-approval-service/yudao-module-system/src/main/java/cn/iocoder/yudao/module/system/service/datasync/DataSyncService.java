package cn.iocoder.yudao.module.system.service.datasync;

import cn.iocoder.yudao.module.system.service.datasync.dto.SyncDeptDTO;
import cn.iocoder.yudao.module.system.service.datasync.dto.SyncResult;
import cn.iocoder.yudao.module.system.service.datasync.dto.SyncUserDTO;
import com.alibaba.fastjson.JSONArray;

public interface DataSyncService {

    /**
     * 同步用户数据
     */
    SyncResult syncUser(SyncUserDTO user);

    /**
     * 同步部门数据
     */
    SyncResult syncDept(SyncDeptDTO dept);
}
