package cn.iocoder.yudao.module.system.service.datasync;

import com.alibaba.fastjson.JSONArray;

public interface DataSyncService {

    /**
     * 同步用户数据
     */
    void syncUsers(JSONArray users);

    /**
     * 同步部门数据
     */
    void syncDepts(JSONArray depts);
}
