package com.sleepy.media.theater.vo;

import lombok.Data;

/**
 * 视频接口参数VO
 *
 * @author gehoubao
 * @create 2021-07-04 12:03
 **/
@Data
public class VideoVO {

    /**
     * 本地电影整理参数定义
     */
    String regularType;
    String targetPath;
    String metaPath;
    String targetMetaMapPath;
}