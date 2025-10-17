package cn.iocoder.yudao.module.biz.service.utils;

import com.google.common.base.CaseFormat;

import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JdbcClientHelper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Map<String, String> resultSetToMap(ResultSet rs, int rowNum) throws SQLException {
        Map<String, String> row = new HashMap<>();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String columnName = rs.getMetaData().getColumnName(i);
            columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
            Object value = rs.getObject(i);
            String data = "";
            if (value instanceof NClob clob) {
                data = clob.getSubString(1, (int) clob.length());
            } else if (value instanceof LocalDateTime localDateTime) {
                data = localDateTime.format(dateTimeFormatter);
            } else {
                data = value != null ? value.toString() : "";
            }
            row.put(columnName, data);
        }
        return row;
    }
}
