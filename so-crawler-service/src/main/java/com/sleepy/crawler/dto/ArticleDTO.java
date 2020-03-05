package com.sleepy.crawler.dto;

import lombok.Data;

import java.util.Date;

/**
 * 文章数据 传输类
 *
 * @author gehoubao
 * @create 2020-03-05 17:25
 **/
@Data
public class ArticleDTO extends TransferDTO {
    private String title;

    private String summary;

    private String content;

    private String coverImg;

    private Date createTime;

    private String tags;

}