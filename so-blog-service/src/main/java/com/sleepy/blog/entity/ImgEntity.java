package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 图片实体类
 *
 * @author gehoubao
 * @create 2019-10-31 9:30
 **/
@Data
@Entity
@Table(name = "so_img")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class ImgEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "type", columnDefinition = "varchar(16) comment '图片类别，如封面、照片、壁纸等等，用于图片保存路径的分配'")
    private String type;
    @Column(name = "path", columnDefinition = "varchar(255) comment '本地存储相对路径'")
    private String path;
    @Column(name = "alias", columnDefinition = "varchar(255) comment '图片别名'")
    private String alias;
    @Column(name = "tags", columnDefinition = "varchar(255) comment '图片标签（多个以，分隔）'")
    private String tags;
    @Column(name = "archive", columnDefinition = "varchar(255) comment '图片归档（多个以，分隔）'")
    private String archive;
    @Column(name = "associateAttribute", columnDefinition = "varchar(255) comment '关联属性，格式：【图片出处】：【图片使用位置】：是否可以单独删除'")
    private String associateAttribute;
    @Column(name = "describeInfo", columnDefinition = "varchar(255) comment '描述信息'")
    private String describeInfo;
    @Column(name = "uploadTime", columnDefinition = "DATETIME COMMENT '上传时间'")
    private Date uploadTime;

    @Column(name = "createTime", columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;
    @Column(name = "location", columnDefinition = "varchar(255) COMMENT '位置信息'")
    private String location;
    @Column(name = "resolutionRatio", columnDefinition = "varchar(16) COMMENT '分辨率'")
    private String resolutionRatio;
    @Column(name = "imgSize", columnDefinition = "varchar(8) COMMENT '图片大小'")
    private String imgSize;
    @Column(name = "imgFormat", columnDefinition = "varchar(8) COMMENT '图片格式'")
    private String imgFormat;
}