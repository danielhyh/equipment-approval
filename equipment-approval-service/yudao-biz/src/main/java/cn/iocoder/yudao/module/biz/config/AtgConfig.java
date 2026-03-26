package cn.iocoder.yudao.module.biz.config;

import com.alibaba.gov.api.client.AtgBusClient;
import com.alibaba.gov.api.client.DefaultAtgBusClient;
import com.alibaba.gov.api.domain.AtgBusSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AtgConfig {

    // 替换为实际环境地址
    private static final String GATEWAY_URL = "http://172.29.75.154:8087/openapi";
    private static final String APP_ID = "286301";
    private static final String KEY_ID = "h9nyv3q66ms1bu8feg0t1i6kvbmosnag";
    private static final String SECRET_KEY = "oGbmfpC4BZMtrKygrz2g0BQT";


    @Bean
    public AtgBusClient createClient() {
        List<AtgBusSecretKey> secretKeys = new ArrayList<>();
        secretKeys.add(new AtgBusSecretKey(KEY_ID, SECRET_KEY));

        // 初始化客户端
        return new DefaultAtgBusClient(GATEWAY_URL, APP_ID, secretKeys);
    }
}
