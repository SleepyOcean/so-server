package com.sleepy.blog.vo.custom;

import lombok.Data;

import java.util.List;

/**
 * 请求转发VO
 *
 * @author gehoubao
 * @create 2020-04-09 18:11
 **/
@Data
public class RequestVO {
    private String user;
    private String name;
    private String url;
    private List<String> mention;
    private String msg;
    private String time;
    private String cron;
    private String id;
}