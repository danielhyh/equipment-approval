package cn.iocoder.yudao.module.biz.config;

import cn.iocoder.yudao.framework.common.util.spring.SpringUtils;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.enums.SeataMode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

@Configuration
public class DatasourceConfig implements BeanPostProcessor {
    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String masterUrl;

    @Value("${spring.datasource.dynamic.datasource.master.username}")
    private String masterUsername;

     @Value("${spring.datasource.dynamic.datasource.master.password}")
    private String masterPassword;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean,@NonNull String beanName) throws BeansException {
        if (bean instanceof DynamicRoutingDataSource datasource && "dm".equals(SpringUtils.getProperty("db-type"))) {
            DataSource build = DataSourceBuilder.create()
                    .url(masterUrl)
                    .username(masterUsername)
                    .password(masterPassword)
                    .build();
            DataSource proxy = (DataSource) Proxy.newProxyInstance(
                    datasource.getClass().getClassLoader(),
                    new  Class[]{DataSource.class},
                    new DmDatasourceProxy(build)
            );
            ItemDataSource item = new ItemDataSource("master", proxy, proxy, false, false, SeataMode.AT);
            datasource.addDataSource("master", item);
        }
        return bean;
    }
}
