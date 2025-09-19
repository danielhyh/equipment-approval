package cn.iocoder.yudao.module.biz.service.utils;

import com.google.common.base.CaseFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JdbcClientHelper {


    public static Map<String, String> resultSetToMap(ResultSet rs, int rowNum) throws SQLException {
        Map<String, String> row = new HashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
            Object value = rs.getObject(i);
            row.put(columnName, value != null ? value.toString() : null);
        }
        return row;
    }
}
