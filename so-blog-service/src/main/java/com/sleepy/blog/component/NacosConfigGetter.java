package com.sleepy.blog.component;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Nacos配置获取器
 *
 * @author gehoubao
 * @create 2021-08-23 14:32
 **/
@Component
public class NacosConfigGetter {

    @Value("${nacos.config.server-addr}")
    private String SERVER_ADDR;

    public String get(String groupID, String dataID) {
        Properties properties = new Properties();
        // nacos服务器地址
        properties.put(PropertyKeyConst.SERVER_ADDR, SERVER_ADDR);
        ConfigService configService = null;
        String content = "";
        try {
            configService = NacosFactory.createConfigService(properties);
            // 根据dataId、group定位到具体配置文件，获取其内容. 方法中的三个参数分别是: dataId, group, 超时时间
            content = configService.getConfig(dataID, groupID, 3000L);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        // 因为我的配置内容是JSON数组字符串，这里将字符串转为JSON数组
        return content;
    }
}