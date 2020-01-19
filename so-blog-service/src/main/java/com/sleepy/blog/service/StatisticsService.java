package com.sleepy.blog.service;

import com.sleepy.blog.dto.ChartOfBarDTO;
import com.sleepy.blog.dto.CommonDTO;

/**
 * 统计Service
 *
 * @author ghb
 * @create 2019-08-29 19:27
 **/
public interface StatisticsService {

    /**
     * 文章统计
     *
     * @return
     * @throws Exception
     */
    CommonDTO<ChartOfBarDTO> getArticleStatistics() throws Exception;
}
