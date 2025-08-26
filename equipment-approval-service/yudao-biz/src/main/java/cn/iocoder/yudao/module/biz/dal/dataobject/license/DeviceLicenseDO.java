package cn.iocoder.yudao.module.biz.dal.dataobject.license;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("biz_device_license")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLicenseDO {

    private Long id;

    private String licenseNumber;

    private String provinceCode;

    private String categoryCode;

    private String stepType;

    private String deviceClass;

    private String manufacturer;

    private LocalDateTime createTime;
}
