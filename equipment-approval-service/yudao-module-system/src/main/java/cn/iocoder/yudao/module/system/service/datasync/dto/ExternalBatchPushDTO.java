package cn.iocoder.yudao.module.system.service.datasync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalBatchPushDTO {

    private List<SyncDeptDTO> depts;
    private List<SyncUserDTO> users;
}
