package com.sleepy.common.config;

import com.sleepy.common.aop.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 公共配置
 *
 * @author gehoubao
 * @create 2019-12-30 10:36
 **/
@Configuration
public class CommonConfig implements WebMvcConfigurer {
    @Autowired
    RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**").excludePathPatterns("/swagger-ui.html");
    }
}