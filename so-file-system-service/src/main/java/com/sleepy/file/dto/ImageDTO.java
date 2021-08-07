package com.sleepy.file.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 图片实体类
 *
 * @author gehoubao
 * @create 2020-03-06 16:14
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageDTO {

    private String imageId;
    /**
     * 图片类别，如封面、照片、壁纸等等，用于图片保存路径的分配
     */
    private String type;
    /**
     * 本地存储相对路径
     */
    private String path;
    /**
     * 图片别名
     */
    private String alias;
    /**
     * 图片标签（多个以，分隔）
     */
    private String tags;
    /**
     * 图片归档（多个以，分隔）
     */
    private String archive;
    /**
     * 关联属性，格式：【图片出处】：【图片使用位置】：是否可以单独删除
     */
    private String associateAttribute;
    /**
     * 描述信息
     */
    private String describeInfo;
    /**
     * 上传时间
     */
    private String uploadTime;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 位置信息
     */
    private String location;
    /**
     * 分辨率
     */
    private String resolutionRatio;
    /**
     * 图片大小
     */
    private String imgSize;
    /**
     * 图片格式
     */
    private String imgFormat;
}