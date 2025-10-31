package cn.iocoder.yudao.module.biz.dal.mysql.history;

import cn.iocoder.yudao.module.biz.service.history.dto.HistoryDuplicateLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryOriginalLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryResult;
import cn.iocoder.yudao.module.biz.service.history.dto.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HistoryMapper {

    IPage<HistoryResult> page(IPage<HistoryResult> page,@Param("req") QueryRequest request);

    HistoryOriginalLicense getOriginal(@Param("id") String id);

    HistoryDuplicateLicense getDuplicate( @Param("id") String id);
}
