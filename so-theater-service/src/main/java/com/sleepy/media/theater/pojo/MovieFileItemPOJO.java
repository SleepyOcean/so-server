package com.sleepy.media.theater.pojo;

import lombok.Data;

import java.util.List;

/**
 * 电影文件对象定义
 *
 * @author gehoubao
 * @create 2021-04-16 19:41
 **/
@Data
public class MovieFileItemPOJO {
    /**
     * 电影中文名
     */
    String nameCN;
    /**
     * 电影父目录名
     */
    String parentDirName;
    /**
     * 电影文件名
     */
    String fileName;
    /**
     * 电影父目录的完整路径。电影完整路径 = fullPath + fileName
     */
    String fullPath;
    /**
     * 电影目录下的电影文件数量。一般只有一个，大于一个的一般是合集，需要单独处理
     */
    int videoCount;
    /**
     * 电影目录下所有文件名列表
     */
    List<String> fileNameList;
    /**
     * 电影目录下所有子目录名列表
     */
    List<String> subDirNameList;


}