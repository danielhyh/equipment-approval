package cn.iocoder.yudao.module.biz.service.history.dto;

import lombok.Data;

@Data
public class QueryRequest {

    private String licenseDevice;

    private String ladderConfig;

    private String area;

    private String keyword;

    private Integer pageNum;

    private Integer pageSize;
}
