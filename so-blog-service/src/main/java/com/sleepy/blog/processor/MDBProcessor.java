package com.sleepy.blog.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 内存数据库处理器
 *
 * @author gehoubao
 * @create 2020-04-09 10:26
 **/
@Component
public class MDBProcessor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void setCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public Object getCache(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setCache(String prefix, String key, String value) {
        stringRedisTemplate.opsForValue().set(prefix + ":" + key, value);
    }

    public Object getCache(String prefix, String key) {
        return stringRedisTemplate.opsForValue().get(prefix + ":" + key);
    }
}