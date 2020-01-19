package com.sleepy.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * 日志控制VO
 *
 * @author gehoubao
 * @create 2019-08-29 15:12
 **/
@Data
public class LoggerVO {
    /**
     * 日志指定对象
     */
    private List<String> loggers;
    /**
     * 日志调整级别
     */
    private String level;
}