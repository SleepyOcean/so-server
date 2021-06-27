package com.sleepy.media.theater.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 本地视频entity
 *
 * @author gehoubao
 * @create 2021-05-23 18:40
 **/
@Data
@Entity
@Table(name = "so_local_video")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class LocalVideoEntity {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "name", columnDefinition = "varchar(128) comment '视频原名'")
    private String name;

    @Column(name = "nameCN", columnDefinition = "varchar(128) comment '视频中文名'")
    private String nameCN;

    @Column(name = "ratio", columnDefinition = "varchar(128) comment '视频清晰度'")
    private String ratio;

    @Column(name = "publishYear", columnDefinition = "INT COMMENT '上映时间'")
    private Integer publishYear;

    @Column(name = "updateTime", columnDefinition = "DATETIME COMMENT '添加时间'")
    private Date updateTime;

    @Column(name = "localVideoPath", columnDefinition = "varchar(255) comment '视频本地路径'")
    private String localVideoPath;

    @Column(name = "localPosterPath", columnDefinition = "varchar(255) comment '视频海报本地路径'")
    private String localPosterPath;

    @Column(name = "type", columnDefinition = "TINYINT COMMENT '视频分类'")
    private String type;

    @Column(name = "favorite", columnDefinition = "TINYINT COMMENT '收藏，0：未收藏， 1：已收藏'")
    private Integer favorite = 0;

    @Column(name = "soVideoId", columnDefinition = "varchar(255) comment '视频详情关联ID'")
    private String soVideoId;

}