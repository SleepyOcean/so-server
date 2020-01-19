package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文章标签实体类
 *
 * @author gehoubao
 * @create 2019-07-09 19:06
 **/
@Data
@Entity
@Table(name = "so_tag")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class TagEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "article_ids")
    private String articleId;
}