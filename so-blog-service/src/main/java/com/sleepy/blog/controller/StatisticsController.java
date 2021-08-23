package com.sleepy.blog.controller;

import com.sleepy.blog.dto.ChartOfBarDTO;
import com.sleepy.blog.service.StatisticsService;
import com.sleepy.common.http.CommonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计Controller
 *
 * @author gehoubao
 * @create 2019-08-29 19:26
 **/
@RestController
@CrossOrigin
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/article")
    public CommonDTO<ChartOfBarDTO> getArticleStatistics() throws Exception {
        return statisticsService.getArticleStatistics();
    }
}