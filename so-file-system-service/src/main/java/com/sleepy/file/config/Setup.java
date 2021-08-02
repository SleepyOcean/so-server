package com.sleepy.file.config;

import com.sleepy.file.FileApplication;
import com.sleepy.file.FileApplicationBuilder;
import com.sleepy.file.img.so.so_gallery.SoGalleryManager;
import com.speedment.runtime.connector.mysql.MySqlBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 *
 * @author gehoubao
 * @create 2021-08-01 13:35
 **/
@Configuration
public class Setup {

    @Bean
    public FileApplication createFileApplication() {
        return new FileApplicationBuilder().withBundle(MySqlBundle.class).withIpAddress("mysql.sleepyocean.cn").withPort(8000).withSchema("so").withPassword("123456").build();
    }

    @Bean
    public SoGalleryManager creteGallery(FileApplication app) {
        return app.getOrThrow(SoGalleryManager.class);
    }

}