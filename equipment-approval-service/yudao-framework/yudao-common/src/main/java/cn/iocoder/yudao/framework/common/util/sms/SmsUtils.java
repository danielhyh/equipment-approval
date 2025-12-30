package cn.iocoder.yudao.framework.common.util.sms;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.iocoder.yudao.framework.common.util.sm3.IccSm3Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmsUtils {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static boolean sendMsgRedirect(String content, List<String> phones) {
        return sendMsgRedirect(content, phones, null);
    }


    public static boolean sendMsgRedirect(String content, List<String> phones, Map<String, Object> varMap) {
        String url = "http://59.218.239.191:30020/msg/send/direct";
        String account = "gjwstjzbxtuser";
        String password = "2ypwKONeQf";
        String bizCode = "YW-20251111-M952V";
        String event = "国家卫生统计直报系统";
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> params = new TreeMap<>();
        params.put("bizCode", bizCode);
        params.put("msgType", 0);
        params.put("eventType", event);
        params.put("eventName", event);
        params.put("content", content);
        params.put("toList", phones);
        params.put("customVarMap", varMap);

        String xAuth = Base64.getEncoder().encodeToString(String.join(":", account, now.format(formatter)).getBytes());
        String toBeSigned = String.join("", account, password, now.format(formatter));
        String xSign = SecureUtil.sha1().digestHex(toBeSigned);
        String sign;
        try {
            sign = IccSm3Util.generateSignature(JSON.toJSONString(params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("X-Auth", xAuth)
                .header("X-Sign", xSign)
                .header("X-SM3", sign)
                .header("accept", "application/json")
                .contentType("application/json")
                .body(JSON.toJSONString(params))
                .execute();
        String body = httpResponse.body();
        httpResponse.close();
        System.out.println(body);
        return Optional.ofNullable(body)
                .map(JSONObject::parseObject)
                .map(obj -> "成功".equals(obj.getString("msg")))
                .orElse(false);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>(Map.of("name", "张三", "age", 18));
        map.put("time", null);
        System.out.println(JSON.toJSONString( map));
    }

    private static String getGMTStr() {
        // 获取GMT时区
        ZoneId gmtZone = ZoneId.of("GMT");

        // 获取当前时间在GMT时区的表示
        ZonedDateTime gmtTime = ZonedDateTime.now(gmtZone);

        // 定义GMT格式的日期时间格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
                .withZone(gmtZone);

        // 格式化为GMT字符串
        return formatter.format(gmtTime);
    }
}
