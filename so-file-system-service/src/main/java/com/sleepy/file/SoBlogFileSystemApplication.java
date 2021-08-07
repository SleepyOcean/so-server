package com.sleepy.file;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.sleepy.**"})
@EnableNacosConfig
@NacosPropertySource(dataId = "filesystem", groupId = "SO_SERVER", autoRefreshed = true)
public class SoBlogFileSystemApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");

        SpringApplication.run(SoBlogFileSystemApplication.class, args);
    }

}
