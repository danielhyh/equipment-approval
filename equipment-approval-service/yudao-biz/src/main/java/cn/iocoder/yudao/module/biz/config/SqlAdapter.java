package cn.iocoder.yudao.module.biz.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlAdapter {

    public static String adaptSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }

        String modifiedSql = sql;

        // 1. 处理LIMIT语句 (MySQL -> 达梦使用ROWNUM)
        //modifiedSql = handleLimit(modifiedSql);

        // 2. 处理字符串函数
        modifiedSql = handleStringFunctions(modifiedSql);

        // 3. 处理日期函数
        modifiedSql = handleDateFunctions(modifiedSql);

        // 4. 处理自增主键
        modifiedSql = handleAutoIncrement(modifiedSql);

        // 5. 处理反引号
        modifiedSql = handleBackticks(modifiedSql);

        // 6. 处理IF函数
        modifiedSql = handleIfFunction(modifiedSql);

        // 7. 处理GROUP_CONCAT
        modifiedSql = handleGroupConcat(modifiedSql);

        return modifiedSql;
    }

    private static String handleLimit(String sql) {
        // MySQL: SELECT * FROM table LIMIT 10
        // 达梦: SELECT * FROM table WHERE ROWNUM <= 10
        Pattern limitPattern = Pattern.compile("\\s+LIMIT\\s+(\\d+)(?:\\s*,\\s*(\\d+))?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = limitPattern.matcher(sql);

        if (matcher.find()) {
            String limit = matcher.group(1);
            String offset = matcher.group(2);

            if (offset != null) {
                // LIMIT offset, limit -> 使用子查询
                int offsetNum = Integer.parseInt(limit);
                int limitNum = Integer.parseInt(offset);
                int endRow = offsetNum + limitNum;

                // 使用ROWNUM实现分页
                String baseSql = sql.substring(0, matcher.start());
                return String.format(
                        "SELECT * FROM (SELECT ROWNUM AS RN, T.* FROM (%s) T WHERE ROWNUM <= %d) WHERE RN > %d",
                        baseSql, endRow, offsetNum
                );
            } else {
                // 简单的LIMIT n
                if (sql.toUpperCase().contains("WHERE")) {
                    return matcher.replaceAll(" AND ROWNUM <= " + limit);
                } else {
                    return matcher.replaceAll(" WHERE ROWNUM <= " + limit);
                }
            }
        }
        return sql;
    }

    private static String handleStringFunctions(String sql) {
        // IFNULL -> NVL
        sql = sql.replaceAll("(?i)IFNULL\\s*\\(", "NVL(");

        // SUBSTRING -> SUBSTR
        sql = sql.replaceAll("(?i)SUBSTRING\\s*\\(", "SUBSTR(");

        // LOCATE -> INSTR (参数顺序不同，需要特殊处理)
        Pattern locatePattern = Pattern.compile("(?i)LOCATE\\s*\\(([^,]+),([^)]+)\\)");
        Matcher matcher = locatePattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            // LOCATE(substr, str) -> INSTR(str, substr)
            matcher.appendReplacement(sb, "INSTR(" + matcher.group(2) + "," + matcher.group(1) + ")");
        }
        matcher.appendTail(sb);
        sql = sb.toString();

        return sql;
    }

    private static String handleDateFunctions(String sql) {
        // NOW() -> SYSDATE
        sql = sql.replaceAll("(?i)NOW\\s*\\(\\s*\\)", "SYSDATE");

        // CURDATE() -> TRUNC(SYSDATE)
        sql = sql.replaceAll("(?i)CURDATE\\s*\\(\\s*\\)", "TRUNC(SYSDATE)");

        // DATE_FORMAT -> TO_CHAR
        Pattern dateFormatPattern = Pattern.compile("(?i)DATE_FORMAT\\s*\\(([^,]+),\\s*'([^']+)'\\)");
        Matcher matcher = dateFormatPattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String dateExpr = matcher.group(1);
            String format = convertMySQLDateFormat(matcher.group(2));
            matcher.appendReplacement(sb, "TO_CHAR(" + dateExpr + ", '" + format + "')");
        }
        matcher.appendTail(sb);
        sql = sb.toString();

        // DATE_ADD -> 使用INTERVAL
        sql = sql.replaceAll("(?i)DATE_ADD\\s*\\(([^,]+),\\s*INTERVAL\\s+(\\d+)\\s+DAY\\s*\\)",
                "$1 + $2");
        sql = sql.replaceAll("(?i)DATE_SUB\\s*\\(([^,]+),\\s*INTERVAL\\s+(\\d+)\\s+DAY\\s*\\)",
                "$1 - $2");

        return sql;
    }

    private static String convertMySQLDateFormat(String mysqlFormat) {
        // MySQL格式转换为Oracle/达梦格式
        return mysqlFormat
                .replace("%Y", "YYYY")
                .replace("%y", "YY")
                .replace("%m", "MM")
                .replace("%d", "DD")
                .replace("%H", "HH24")
                .replace("%h", "HH")
                .replace("%i", "MI")
                .replace("%s", "SS")
                .replace("%W", "DAY")
                .replace("%M", "MONTH");
    }

    private static String handleAutoIncrement(String sql) {
        // 处理INSERT语句中的自增字段
        if (sql.toUpperCase().contains("INSERT")) {
            // 移除AUTO_INCREMENT关键字
            sql = sql.replaceAll("(?i)\\s+AUTO_INCREMENT", "");

            // 如果是创建表语句，需要创建序列
            if (sql.toUpperCase().contains("CREATE TABLE")) {
                //TODO 这里可以记录需要创建的序列，后续处理
            }
        }
        return sql;
    }

    private static String handleBackticks(String sql) {
        // 将反引号替换为双引号（达梦使用双引号作为标识符引用）
        return sql.replace("`", "\"");
    }

    private static String handleIfFunction(String sql) {
        // IF(condition, true_value, false_value) -> CASE WHEN condition THEN true_value ELSE false_value END
        Pattern ifPattern = Pattern.compile("(?i)IF\\s*\\(([^,]+),([^,]+),([^)]+)\\)");
        Matcher matcher = ifPattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String replacement = String.format("CASE WHEN %s THEN %s ELSE %s END",
                    matcher.group(1).trim(),
                    matcher.group(2).trim(),
                    matcher.group(3).trim());
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String handleGroupConcat(String sql) {
        // GROUP_CONCAT -> LISTAGG (达梦8支持LISTAGG)
        Pattern groupConcatPattern = Pattern.compile(
                "(?i)GROUP_CONCAT\\s*\\(([^)]+)\\)");
        Matcher matcher = groupConcatPattern.matcher(sql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String expr = matcher.group(1);
            // 简单处理，实际可能需要更复杂的解析
            String replacement = String.format("LISTAGG(%s, ',') WITHIN GROUP (ORDER BY %s)",
                    expr, expr);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
