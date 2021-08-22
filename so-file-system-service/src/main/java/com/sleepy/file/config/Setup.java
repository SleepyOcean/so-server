package com.sleepy.file.config;

import com.sleepy.file.FileApplication;
import com.sleepy.file.FileApplicationBuilder;
import com.sleepy.file.img.so.so_gallery.SoGalleryManager;
import com.speedment.runtime.connector.mysql.MySqlBundle;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jdbc.mysql.address}")
    private String address;
    @Value("${jdbc.mysql.port}")
    private int port;
    @Value("${jdbc.mysql.database}")
    private String database;
    @Value("${jdbc.mysql.username}")
    private String username;
    @Value("${jdbc.mysql.password}")
    private String password;

    @Bean
    public FileApplication createFileApplication() {
        return new FileApplicationBuilder().withBundle(MySqlBundle.class).withIpAddress(address).withPort(port).withSchema(database).withUsername(username).withPassword(password).build();
    }

    @Bean
    public SoGalleryManager creteGallery(FileApplication app) {
        return app.getOrThrow(SoGalleryManager.class);
    }

}