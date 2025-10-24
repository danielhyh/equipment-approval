package cn.iocoder.yudao.module.system.service.datasync.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchPushResultVO {

    private Integer deptSuccessCount = 0;
    private Integer deptFailCount = 0;
    private List<String> deptFailMessages = new ArrayList<>();

    private Integer userSuccessCount = 0;
    private Integer userFailCount = 0;
    private List<String> userFailMessages = new ArrayList<>();

    /**
     * 添加机构成功
     */
    public void addDeptSuccess() {
        this.deptSuccessCount++;
    }

    /**
     * 添加机构失败
     */
    public void addDeptFail(String externalId, String errorMessage) {
        this.deptFailCount++;
        this.deptFailMessages.add(String.format("机构[%s]: %s", externalId, errorMessage));
    }

    /**
     * 添加用户成功
     */
    public void addUserSuccess() {
        this.userSuccessCount++;
    }

    /**
     * 添加用户失败
     */
    public void addUserFail(String externalId, String errorMessage) {
        this.userFailCount++;
        this.userFailMessages.add(String.format("用户[%s]: %s", externalId, errorMessage));
    }

    /**
     * 是否有错误
     */
    public boolean hasError() {
        return deptFailCount > 0 || userFailCount > 0;
    }

    /**
     * 获取错误汇总信息
     */
    public String getErrorSummary() {
        if (!hasError()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (deptFailCount > 0) {
            sb.append("机构失败:").append(deptFailCount).append("条; ");
        }
        if (userFailCount > 0) {
            sb.append("用户失败:").append(userFailCount).append("条; ");
        }
        return sb.toString();
    }
}
