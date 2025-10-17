package cn.iocoder.yudao.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.metrics.StartupStep;

import java.util.Comparator;
import java.util.stream.StreamSupport;

/**
 * 项目的启动类
 *
 * 如果你碰到启动的问题，请认真阅读 https://doc.iocoder.cn/quick-start/ 文章
 * 如果你碰到启动的问题，请认真阅读 https://doc.iocoder.cn/quick-start/ 文章
 * 如果你碰到启动的问题，请认真阅读 https://doc.iocoder.cn/quick-start/ 文章
 *
 * @author 芋道源码
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package}
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}.server", "${yudao.info.base-package}.module"})
public class YudaoServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(YudaoServerApplication.class, args);
//        BufferingApplicationStartup startup = new BufferingApplicationStartup(20480);
//        ConfigurableApplicationContext context = new SpringApplicationBuilder(YudaoServerApplication.class)
//                .applicationStartup(startup)
//                .run(args);
    }

}
