package com.sleepy.blog.service.impl;

import com.sleepy.blog.dto.ChartOfBarDTO;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.repository.ArticleRepository;
import com.sleepy.blog.service.StatisticsService;
import com.sleepy.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计ServiceImpl
 *
 * @author gehoubao
 * @create 2019-08-29 19:28
 **/
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    public static final String INDEX_NAME = "so_blog";
    public static final String ARTICLE_TYPE_NAME = "so_article";

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public CommonDTO<ChartOfBarDTO> getArticleStatistics() throws Exception {
        CommonDTO<ChartOfBarDTO> result = new CommonDTO<>();
        List<ChartOfBarDTO> resultList = articleRepository.getArticleStatistic(DateUtil.getDateWithCurrent(-3, Calendar.MONTH), new Date());

        Map<String, Object> extra = new HashMap<>(2);
        extra.put("xAxis", resultList.stream().map(o -> o.getXAxis().substring(0, 10)).collect(Collectors.toList()));
        extra.put("yAxis", resultList.stream().map(o -> o.getYAxis()).collect(Collectors.toList()));
        result.setExtra(extra);
        return result;
    }
}