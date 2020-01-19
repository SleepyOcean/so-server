package com.sleepy.blog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章DTO
 *
 * @author gehoubao
 * @create 2019-07-08 16:17
 **/
@Data
public class PostDTO {
    @ApiModelProperty("博文ID")
    private String id;

    @ApiModelProperty("博客标题")
    private String title;

    @ApiModelProperty("博文摘要")
    private String summary;

    @ApiModelProperty("博文内容")
    private String content;

    @ApiModelProperty("最后一次更新时间")
    private String updateTime;

    @ApiModelProperty("博文创建时间")
    private Date createTime;

    @ApiModelProperty("博文标签")
    private String tags;

    @ApiModelProperty("阅读数")
    private Long readCount;

    @ApiModelProperty("评论数")
    private Long commentCount;

    @ApiModelProperty("转发数")
    private Long shareCount;

    @ApiModelProperty("热度")
    private Long hotRate;

    @ApiModelProperty("专栏")
    private String collection;

    @ApiModelProperty("文章来源 -> 【原创 | 转载：网站名称：url】")
    private String source;

    @ApiModelProperty("博客私密设置，0：公开， 1：私密")
    private Integer privacy;

    @ApiModelProperty("搜索结果")
    private List<String> searchResult;
}