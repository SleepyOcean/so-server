package com.sleepy.blog.service.impl;

import com.sleepy.blog.repository.ArticleRepository;
import com.sleepy.blog.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试接口服务实现类
 *
 * @author gehoubao
 * @create 2019-04-19 15:22
 **/
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>(1);
        result.put("data", articleRepository.findAll());
        log.info("测试log_info打印");
        log.debug("测试log_debug打印");
        return result;
    }
}