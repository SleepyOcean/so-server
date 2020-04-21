package com.sleepy.blog.dto;

import com.sleepy.jpql.annotation.JpqlCol;
import lombok.Data;

/**
 * 测试DTO
 *
 * @author gehoubao
 * @create 2020-04-20 15:37
 **/
@Data
public class TestDTO {
    private String id;
    private String tagName;
    @JpqlCol("article_ids")
    private String articleId;
    private String articleAmount;
    private Long hotRateSum;
    private String createTime;
    private String title;
}