package cn.iocoder.yudao.module.system.service.datasync.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class BatchPushResultVO {

    // 使用 AtomicInteger 保证计数的原子性
    private final AtomicInteger deptSuccessCount = new AtomicInteger(0);
    private final AtomicInteger deptFailCount = new AtomicInteger(0);
    // 使用线程安全的集合
    private final List<String> deptFailMessages = Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger userSuccessCount = new AtomicInteger(0);
    private final AtomicInteger userFailCount = new AtomicInteger(0);
    private final List<String> userFailMessages = Collections.synchronizedList(new ArrayList<>());

    /**
     * 添加机构成功
     */
    public void addDeptSuccess() {
        this.deptSuccessCount.incrementAndGet();
    }

    /**
     * 添加机构失败
     */
    public void addDeptFail(String externalId, String errorMessage) {
        this.deptFailCount.incrementAndGet();
        this.deptFailMessages.add(String.format("机构[%s]: %s", externalId, errorMessage));
    }

    /**
     * 添加用户成功
     */
    public void addUserSuccess() {
        this.userSuccessCount.incrementAndGet();
    }

    /**
     * 添加用户失败
     */
    public void addUserFail(String externalId, String errorMessage) {
        this.userFailCount.incrementAndGet();
        this.userFailMessages.add(String.format("用户[%s]: %s", externalId, errorMessage));
    }

    /**
     * 是否有错误
     */
    public boolean hasError() {
        return deptFailCount.get() > 0 || userFailCount.get() > 0;
    }

    /**
     * 获取错误汇总信息
     */
    public String getErrorSummary() {
        if (!hasError()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int deptFails = deptFailCount.get();
        int userFails = userFailCount.get();

        if (deptFails > 0) {
            sb.append("机构失败:").append(deptFails).append("条; ");
        }
        if (userFails > 0) {
            sb.append("用户失败:").append(userFails).append("条; ");
        }
        return sb.toString();
    }
}
