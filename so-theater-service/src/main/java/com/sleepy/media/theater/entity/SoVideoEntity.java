package com.sleepy.media.theater.entity;

import com.sleepy.media.theater.pojo.DownloadLinkPOJO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * 影片实体类
 *
 * @author sleepyocean
 * @since 2020-11-21 15:24:59
 */
@Data
@Document(indexName = "theater", refreshInterval = "0s")
public class SoVideoEntity {

    @Id
    private Long id;
    /**
     * 影片名称
     */
    private String name;
    /**
     * 原始影片名称
     */
    private String originalName;
    /**
     * 影片中文名
     */
    private String chineseName;
    /**
     * 上映年份
     */
    private String publishYear;
    /**
     * 上映日期
     */
    private String date;
    /**
     * 出品国家
     */
    private String country;
    /**
     * 豆瓣链接
     */
    private String linkDouban;
    /**
     * 豆瓣评分
     */
    private String scoreDouban;
    /**
     * 评分
     */
    private String score;
    /**
     * 影片时长
     */
    private String runningTime;
    /**
     * 影片别名
     */
    private String alias;
    /**
     * 导演
     */
    private String director;
    /**
     * 编剧
     */
    private String scriptwriter;
    /**
     * 演员
     */
    private String actor;
    /**
     * 影片类型
     */
    private String type;
    /**
     * 影片介绍
     */
    private List<String> intro;
    /**
     * 影片默认海报
     */
    private String postUrl;
    /**
     * 影片横板海报
     */
    private List<String> postUrlHorizon;
    /**
     * 影片竖版海报
     */
    private List<String> postUrlVertical;
    /**
     * 影片剧照
     */
    private List<String> captureUrls;
    /**
     * 预告片
     */
    private List<String> trailerUrls;
    /**
     * 记录更新时间
     */
    private String updateTime;
    /**
     * 备注
     */
    private String note;
    /**
     * 下载链接
     */
    private List<DownloadLinkPOJO> downloadLinks;
}