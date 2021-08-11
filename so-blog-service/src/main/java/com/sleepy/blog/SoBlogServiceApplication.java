package com.sleepy.blog;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author KD8832
 */
@EnableJpaRepositories(basePackages = {"com.sleepy"})
@EntityScan(basePackages = {"com.sleepy"})
@SpringBootApplication(scanBasePackages = {"com.sleepy.blog", "com.sleepy.jpql", "com.sleepy.common", "com.sleepy.security"})
@EnableSwagger2
@EnableNacosConfig
@NacosPropertySource(dataId = "blogsystem", groupId = "SO_SERVER", autoRefreshed = true)
public class SoBlogServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SoBlogServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 解决log日志冲突
        System.setProperty("nacos.logging.default.config.enabled", "false");
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(SoBlogServiceApplication.class);
    }
}
