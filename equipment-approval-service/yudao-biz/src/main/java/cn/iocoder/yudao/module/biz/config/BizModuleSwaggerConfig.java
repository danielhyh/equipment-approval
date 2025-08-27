package cn.iocoder.yudao.module.biz.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BizModuleSwaggerConfig {


    @Bean
    public GroupedOpenApi bizGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("biz");
    }
}
