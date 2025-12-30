package cn.iocoder.yudao.server;

import cn.idev.excel.EasyExcel;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.service.datasync.DataSyncService;
import cn.iocoder.yudao.module.system.service.datasync.dto.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestImport {

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
}
