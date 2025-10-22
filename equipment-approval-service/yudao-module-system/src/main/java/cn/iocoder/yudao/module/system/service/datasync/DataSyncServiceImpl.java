package cn.iocoder.yudao.module.system.service.datasync;

import cn.hutool.core.collection.ListUtil;
import cn.iocoder.yudao.module.system.dal.dataobject.datasync.SyncRecordDO;
import cn.iocoder.yudao.module.system.dal.mysql.datasync.SyncRecordMapper;
import cn.iocoder.yudao.module.system.dal.mysql.dept.DeptMapper;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

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
    private PlatformTransactionManager transactionManager;

    @Resource(name = "syncExecutor")
    private Executor syncExecutor;

    @Resource
    private SyncRecordMapper syncRecordMapper;

    // 每批处理的数据量
    private static final int BATCH_SIZE = 100;



    @Override
    public void syncUsers(JSONArray users) {
        String batchNo  = generateBatchNo("USER");
        List<JSONObject> javaList = users.toJavaList(JSONObject.class);
        executeSyncTask(javaList, batchNo, "USER", this::processBatchUsers);
    }

    @Override
    public void syncDepts(JSONArray depts) {
        String batchNo  = generateBatchNo("DEPT");
        List<JSONObject> javaList = depts.toJavaList(JSONObject.class);
        executeSyncTask(javaList, batchNo, "DEPT", this::processBatchDepts);
    }

    /**
     * 批量处理用户数据
     */
    public BatchResult processBatchUsers(List<JSONObject> users) {
        // 创建独立的事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            if (users.isEmpty()) {
                transactionManager.commit(status);
                return new BatchResult(0, 0, null);
            }
            //TODO 后续实现
            // 转换为实体
            // 查询已存在的用户
            // 分离新增和更新
            // 批量插入
            // 批量更新
            // 提交事务
            transactionManager.commit(status);

            log.debug("批次处理成功，数据量：{}", users.size());
            return new BatchResult(users.size(), 0, null);

        } catch (Exception e) {
            // 回滚事务
            transactionManager.rollback(status);
            log.error("批次处理失败，数据量：{}", users.size(), e);
            return new BatchResult(0, users.size(), "批次处理失败: " + e.getMessage());
        }
    }

    /**
     * 批量处理部门数据
     */
    public BatchResult processBatchDepts(List<JSONObject> depts) {
        // 创建独立的事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            if (depts.isEmpty()) {
                transactionManager.commit(status);
                return new BatchResult(0, 0, null);
            }
            //TODO 后续实现
            // 转换为实体
            // 查询已存在的用户
            // 分离新增和更新
            // 批量插入
            // 批量更新
            // 提交事务
            transactionManager.commit(status);

            log.debug("批次处理成功，数据量：{}", depts.size());
            return new BatchResult(depts.size(), 0, null);

        } catch (Exception e) {
            // 回滚事务
            transactionManager.rollback(status);
            log.error("批次处理失败，数据量：{}", depts.size(), e);
            return new BatchResult(0, depts.size(), "批次处理失败: " + e.getMessage());
        }

    }

    /**
     * 执行同步任务的通用方法
     */
    private <T> void executeSyncTask(List<T> dataList, String batchNo, String syncType,
            BatchProcessor<T> processor) {

        long startTime = System.currentTimeMillis();

        // 创建同步记录
        SyncRecordDO record = createSyncRecord(batchNo, syncType, dataList.size());
        syncRecordMapper.insert(record);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<String> errorMessages = Collections.synchronizedList(new ArrayList<>());

        try {
            // 分批处理数据
            List<List<T>> batches = ListUtil.partition(dataList, BATCH_SIZE);

            // 使用 CompletableFuture 并行处理各批次
            List<CompletableFuture<BatchResult>> futures = batches.stream()
                    .map(batch -> CompletableFuture.supplyAsync(() ->
                            processor.process(batch), syncExecutor))
                    .toList();

            // 等待所有批次完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 汇总结果
            for (CompletableFuture<BatchResult> future : futures) {
                BatchResult result = future.get();
                successCount.addAndGet(result.getSuccessCount());
                failCount.addAndGet(result.getFailCount());
                if (result.getErrorMsg() != null) {
                    errorMessages.add(result.getErrorMsg());
                }
            }

            // 更新同步记录
            updateSyncRecord(record, successCount.get(), failCount.get(),
                    startTime, errorMessages);

        } catch (Exception e) {
            log.error("数据同步异常", e);
            updateSyncRecordFailed(record, startTime, e.getMessage());
            throw new RuntimeException("数据同步失败", e);
        }
    }

    /**
     * 生成批次号
     */
    private String generateBatchNo(String type) {
        return type + "_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 更新同步记录
     */
    private void updateSyncRecord(SyncRecordDO record, int successCount,
                                  int failCount, long startTime,
                                  List<String> errorMessages) {
        record.setSuccessCount(successCount);
        record.setFailCount(failCount);
        record.setEndTime(LocalDateTime.now());
        record.setCostTime(System.currentTimeMillis() - startTime);

        if (failCount == 0) {
            record.setSyncStatus(1); // 成功
        } else if (successCount > 0) {
            record.setSyncStatus(3); // 部分成功
        } else {
            record.setSyncStatus(2); // 失败
        }

        if (!errorMessages.isEmpty()) {
            record.setErrorMsg(String.join("; ", errorMessages));
        }

        syncRecordMapper.updateById(record);
    }

    /**
     * 更新同步记录为失败
     */
    private void updateSyncRecordFailed(SyncRecordDO record, long startTime, String errorMsg) {
        record.setSyncStatus(2);
        record.setEndTime(LocalDateTime.now());
        record.setCostTime(System.currentTimeMillis() - startTime);
        record.setErrorMsg(errorMsg);
        syncRecordMapper.updateById(record);
    }

    /**
     * 创建同步记录
     */
    private SyncRecordDO createSyncRecord(String batchNo, String syncType, int totalCount) {
        SyncRecordDO record = new SyncRecordDO();
        record.setSyncBatchNo(batchNo);
        record.setSyncType(syncType);
        record.setSyncStatus(0); // 进行中
        record.setTotalCount(totalCount);
        record.setSuccessCount(0);
        record.setFailCount(0);
        record.setStartTime(LocalDateTime.now());
        return record;
    }

    /**
     * 批处理器接口
     */
    @FunctionalInterface
    interface BatchProcessor<T> {
        BatchResult process(List<T> batch);
    }

    /**
     * 批处理结果
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class BatchResult {
        private int successCount;
        private int failCount;
        private String errorMsg;
    }
}
