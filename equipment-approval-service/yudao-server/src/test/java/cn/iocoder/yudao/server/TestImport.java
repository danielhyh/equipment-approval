package cn.iocoder.yudao.server;

import cn.idev.excel.EasyExcel;
import cn.iocoder.yudao.framework.common.util.JdbcClientHelper;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.service.datasync.DataSyncService;
import cn.iocoder.yudao.module.system.service.datasync.dto.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@SpringBootTest
public class TestImport {

    @Resource
    private JdbcClient jdbcClient;

    @Resource
    private DataSyncService dataSyncService;

    @Test
    void test() {
        String orgFilePath = "C:\\Users\\WangLe\\Desktop\\机构&用户\\机构.CSV";
        String userFilePath = "C:\\Users\\WangLe\\Desktop\\机构&用户\\大型设备用户.CSV";

        List<ExcelDeptDTO> orgList = EasyExcel.read(orgFilePath, ExcelDeptDTO.class, null)
                .headRowNumber(1)
                .sheet(0)
                .doReadSync();
        List<ExcelUserDTO> userList = EasyExcel.read(userFilePath, ExcelUserDTO.class, null)
                .headRowNumber(1)
                .sheet(0)
                .doReadSync();
        System.out.println(orgList.size());
        System.out.println(userList.size());
        List<SyncDeptDTO> bean = BeanUtils.toBean(orgList, SyncDeptDTO.class);
        List<SyncUserDTO> bean1 = BeanUtils.toBean(userList, SyncUserDTO.class);
        //3 甲
        dataSyncService.batchSync(new ExternalBatchPushDTO(bean, bean1));
//        dataSyncService.syncDept()
    }

    @Test
    void test1() {
        String orgFilePath = "C:\\Users\\13496\\Desktop\\设备机构.xlsx";

        List<ExcelDeptDTO> orgList = EasyExcel.read(orgFilePath, ExcelDeptDTO2.class, null)
                .headRowNumber(2)
                .sheet(0)
                .doReadSync();
        System.out.println(orgList.size());
        List<SyncDeptDTO> bean = BeanUtils.toBean(orgList, SyncDeptDTO.class);
        System.out.println(bean.size());
        //3 甲
       dataSyncService.batchSync(new ExternalBatchPushDTO(bean, new ArrayList<>()));
    }

    /**
     * 插入一条高效通办系统来源的测试数据
     * institution_id=2714 (洛川博爱医院), app_type=1, app_status=5, source=1
     * 设备名称和阶梯机型为空，模拟高办系统回调后待管理员补充信息的场景
     */
    @Test
    void insertAtgTestData() {
        String extra = """
            {
                "projId": "PROJ202604280001",
                "institutionName": "洛川博爱医院",
                "applicantName": "张三",
                "contactPhone": "13800138000",
                "formInfo": "高效通办系统表单数据",
                "attachments": [
                    {"name": "乙类大型医用设备配置许可申请表.pdf", "url": "https://example.com/files/apply-form.pdf", "size": 1258000},
                    {"name": "营业执照.jpg", "url": "https://example.com/files/license.jpg", "size": 856000},
                    {"name": "技术条件材料.docx", "url": "https://example.com/files/tech-doc.docx", "size": 2340000}
                ]
            }
            """;
        jdbcClient.sql("""
            INSERT INTO biz_application (
                app_no, institution_id, app_type, app_status, source,
                extra, creator, create_time, updater, update_time, deleted
            ) VALUES (
                :appNo, :institutionId, :appType, :appStatus, :source,
                :extra, :creator, SYSDATE, :updater, SYSDATE, 0
            )
            """)
            .param("appNo", "ATG-20260428160000")
            .param("institutionId", 2714L)
            .param("appType", 1)
            .param("appStatus", 5)
            .param("source", 1)
            .param("extra", extra)
            .param("creator", "0")
            .param("updater", "0")
            .update();
        System.out.println("高办系统测试数据插入成功");
    }

    @Test
    void updateInstitutionExtCity() {
        List<Map<String, String>> list = jdbcClient.sql("select code, name from system_regions where level = 2")
                .query(JdbcClientHelper::resultSetToMap)
                .list();
        Map<String, String> cityMap = new ConcurrentHashMap<>();
        list.forEach(row -> {
            cityMap.put(row.get("code"), row.get("name"));
        });
//        String orgFilePath = "C:\\Users\\13496\\Desktop\\设备机构.xlsx";
//
//        List<ExcelDeptDTO> orgList = EasyExcel.read(orgFilePath, ExcelDeptDTO2.class, null)
//                .headRowNumber(2)
//                .sheet(0)
//                .doReadSync();
//        List<SyncDeptDTO> listDept = BeanUtils.toBean(orgList, SyncDeptDTO.class);
        String orgFilePath = "C:\\Users\\13496\\Desktop\\other\\机构&用户\\机构.CSV";
        List<ExcelDeptDTO> orgList = EasyExcel.read(orgFilePath, ExcelDeptDTO.class, null)
                .headRowNumber(1)
                .sheet(0)
                .doReadSync();
        List<SyncDeptDTO> listDept = BeanUtils.toBean(orgList, SyncDeptDTO.class);
        //根据listDept中的orgId对应的deptAddressCode更新biz_institution_ext表中的city字段
        // 构建 orgId -> city 的映射
        Map<String, String> orgCityMap = new ConcurrentHashMap<>();
        for (SyncDeptDTO dept : listDept) {
            if (dept.getOrgId() != null && dept.getDeptAddressCode() != null) {
                String cityCode = dept.getDeptAddressCode().substring(0, 4) + "00";
                String cityName = cityMap.get(cityCode);
                if (cityName != null) {
                    orgCityMap.put(dept.getOrgId(), cityName);
                }
            }
        }
        // 多线程更新 biz_institution_ext 的 city
        ExecutorService executor = new ThreadPoolExecutor(
                10, 10, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        try {
            List<CompletableFuture<Void>> futures = orgCityMap.entrySet().stream()
                    .map(entry -> CompletableFuture.runAsync(() -> {
                        jdbcClient.sql("""
                            UPDATE biz_institution_ext SET city = :city
                            WHERE dept_id = (SELECT id FROM system_dept WHERE external_id = :orgId LIMIT 1)
                            """)
                            .param("city", entry.getValue())
                            .param("orgId", entry.getKey())
                            .update();
                    }, executor))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executor.shutdown();
        }
        System.out.println("更新完成，共处理 " + orgCityMap.size() + " 条记录");
    }
}
