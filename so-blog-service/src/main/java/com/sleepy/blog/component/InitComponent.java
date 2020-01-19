package com.sleepy.blog.component;

import com.sleepy.blog.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 自定义初始化组件
 *
 * @author gehoubao
 * @create 2019-09-04 16:34
 **/
@Component
public class InitComponent implements CommandLineRunner {
    @Autowired
    CacheService cacheService;

    @Override
    public void run(String... args) throws Exception {
        cacheService.setSettingCache();
    }
}