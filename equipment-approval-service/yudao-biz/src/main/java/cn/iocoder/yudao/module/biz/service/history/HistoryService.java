package cn.iocoder.yudao.module.biz.service.history;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.biz.dal.mysql.history.HistoryMapper;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryDuplicateLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryOriginalLicense;
import cn.iocoder.yudao.module.biz.service.history.dto.HistoryResult;
import cn.iocoder.yudao.module.biz.service.history.dto.QueryRequest;
import cn.iocoder.yudao.module.biz.service.utils.JdbcClientHelper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Resource
    private HistoryMapper historyMapper;

    @Resource
    private JdbcClient jdbcClient;

    public PageResult<HistoryResult> pageResult(QueryRequest request) {
        IPage<HistoryResult> page = new Page<>(request.getPageNum(), request.getPageSize());
        historyMapper.page(page, request);
        //SX0326
        List<Map<String, String>> statusDict = jdbcClient.sql("select itemcode, itemname from history_dict where dicttype = 'SX0326'")
                .query(JdbcClientHelper::resultSetToMap)
                .list();
        //SX0376
        List<Map<String, String>> deviceStatusDict = jdbcClient.sql("select dictcode, itemname from history_dict where dicttype = 'SX0376'")
                .query(JdbcClientHelper::resultSetToMap)
                .list();
        HashMap<String, String> statusDictMap = statusDict.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        map -> map.get("itemcode"),
                        map -> map.get("itemname"),
                        (oldValue, newValue) -> oldValue,
                        HashMap::new
                ));
        HashMap<String, String> deviceStatusDictMap = deviceStatusDict.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        map -> map.get("dictcode"),
                        map -> map.get("itemname"),
                        (oldValue, newValue) -> oldValue,
                        HashMap::new
                ));
        page.getRecords().forEach(item -> {
            item.setDeviceStatus(deviceStatusDictMap.getOrDefault(item.getDeviceStatus(), ""));
            item.setOriginalEntryStatus(statusDictMap.getOrDefault(item.getOriginalEntryStatus(), ""));
            item.setCopyEntryStatus(statusDictMap.getOrDefault(item.getCopyEntryStatus(), ""));
        });
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    public HistoryOriginalLicense getOriginal(String id) {
        return historyMapper.getOriginal(id);
    }

    public HistoryDuplicateLicense getDuplicate(String id) {
        return historyMapper.getDuplicate(id);
    }
}
