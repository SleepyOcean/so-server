package com.sleepy.blog.service;

/**
 * 缓存服务
 *
 * @author ghb
 * @create 2019-09-04 15:21
 **/
public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    void setCache(String key, String value);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    String getCache(String key);

    /**
     * 配置项缓存
     */
    void setSettingCache();
}
