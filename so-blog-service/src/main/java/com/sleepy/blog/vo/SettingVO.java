package com.sleepy.blog.vo;

import lombok.Data;

/**
 * 配置项VO
 *
 * @author gehoubao
 * @create 2019-09-04 16:53
 **/
@Data
public class SettingVO {
    private String id;
    private String key;
    private String value;
    private String info;
}