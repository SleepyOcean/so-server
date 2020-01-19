package com.sleepy.blog.vo;

import lombok.Data;

/**
 * 项目管理VO
 *
 * @author gehoubao
 * @create 2019-06-26 10:53
 **/
@Data
public class ProjectVO {
    private String id;

    private String projectName;

    private String moduleName;

    private String deadline;

    private String createTime;

    private String updateTime;

    private Integer status;

    private String note;
}