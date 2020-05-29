package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 博客文章表
 *
 * @author Captain
 * @create 2019-04-14 14:28
 */
@Data
@Entity
@Table(name = "so_article")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class ArticleEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "title", columnDefinition = "VARCHAR(255) COMMENT '博文标题'")
    private String title;

    @Column(name = "summary", columnDefinition = "VARCHAR(1024) COMMENT '博文概要'")
    private String summary;

    @Column(name = "content", columnDefinition = "LONGTEXT COMMENT '博文内容'")
    private String content;

    @Column(name = "coverImg", columnDefinition = "VARCHAR(255) COMMENT '封面url'")
    private String coverImg;

    @Column(name = "contentImg", columnDefinition = "VARCHAR(255) COMMENT '文章内图片url列表，多个以逗号分隔'")
    private String contentImg;

    @Column(name = "updateTime", columnDefinition = "DATETIME COMMENT '最后一次更新时间'")
    private Date updateTime;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '博文创建时间'")
    private Date createTime;

    @Column(name = "tags", columnDefinition = "VARCHAR(255) COMMENT '博文标签'")
    private String tags;

    @Column(name = "readCount", columnDefinition = "BIGINT COMMENT '阅读数'")
    private Long readCount = 0L;

    @Column(name = "commentCount", columnDefinition = "INT COMMENT '评论数'")
    private Integer commentCount = 0;

    @Column(name = "shareCount", columnDefinition = "INT COMMENT '转发数'")
    private Integer shareCount = 0;

    @Column(name = "hotRate", columnDefinition = "INT COMMENT '热度(赞👍)'")
    private Integer hotRate = 0;

    @Column(name = "collection", columnDefinition = "VARCHAR(32) COMMENT '专栏ID'")
    private String collection;

    @Column(name = "source", columnDefinition = "VARCHAR(255) COMMENT '文章来源 -> 【原创 | 转载：网站名称：url】'")
    private String source;

    @Column(name = "privacy", columnDefinition = "TINYINT COMMENT '博客私密设置，0：公开， 1：私密'")
    private Integer privacy = 0;
}
