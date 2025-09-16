package cn.iocoder.yudao.module.biz.dal.mysql.statistics;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface StatisticsMapper {

    Map<String, Object> applicationSummary(@Param("status") Integer status);
}
