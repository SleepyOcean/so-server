package com.sleepy.blog.service.impl;

import com.sleepy.blog.dto.ChartOfBarDTO;
import com.sleepy.blog.dto.TestDTO;
import com.sleepy.blog.repository.ArticleRepository;
import com.sleepy.blog.service.TestService;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.jpql.JpqlExecutor;
import com.sleepy.jpql.JpqlResultSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
    @Autowired
    JpqlExecutor jpqlExecutor;

    @Override
    public Map<String, Object> test() {
        return test2();
    }

    private Map<String, Object> test0() {
        JpqlResultSet<ChartOfBarDTO> result = null;
        result = jpqlExecutor.execPageable("testJpql.customSQL",
                CommonTools.getCustomMap(new MapModel("tagNames", Arrays.asList("数据库", "数据分析", "大数据"))),
                TestDTO.class, PageRequest.of(0, 30));
        List<ChartOfBarDTO> list = result.getResultList();
        return CommonTools.getCustomMap(new MapModel("result", list), new MapModel("total", result.getTotal()));
    }

    private Map<String, Object> test1() {
        JpqlResultSet<ChartOfBarDTO> result = null;
        result = jpqlExecutor.exec("testJpql.customArticle",
                CommonTools.getCustomMap(new MapModel("startTime", "2020-01-08 00:00:00")),
                TestDTO.class);
        List<ChartOfBarDTO> list = result.getResultList();
        return CommonTools.getCustomMap(new MapModel("result", list), new MapModel("total", result.getTotal()));
    }

    private Map<String, Object> test2() {
        JpqlResultSet<ChartOfBarDTO> result = null;
        result = jpqlExecutor.execPageable("testJpql.customArticle2",
                CommonTools.getCustomMap(new MapModel("startTime", "2020-01-08 00:00:00")),
                TestDTO.class, PageRequest.of(0, 30));
        List<ChartOfBarDTO> list = result.getResultList();
        return CommonTools.getCustomMap(new MapModel("result", list), new MapModel("total", result.getTotal()));
    }
}