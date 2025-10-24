package cn.iocoder.yudao.module.system.service.datasync.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalBatchPushDTO {

    private List<SyncDeptDTO> depts;
    private List<SyncUserDTO> users;
}
