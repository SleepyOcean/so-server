package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文章专栏实体类
 *
 * @author gehoubao
 * @create 2019-07-09 19:06
 **/
@Data
@Entity
@Table(name = "so_collection")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CollectionEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "coverUrl")
    private String coverUrl;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "deleted", columnDefinition = "TINYINT NOT NULL COMMENT '是否有效， 0-默认，1-已删除'")
    private Integer deleted;

    public CollectionEntity() {
        this.deleted = 0;
    }
}