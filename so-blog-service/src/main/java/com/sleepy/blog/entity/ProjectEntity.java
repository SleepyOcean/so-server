package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 项目管理表
 *
 * @author gehoubao
 * @create 2019-06-26 10:49
 **/
@Data
@Entity
@Table(name = "so_project")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class ProjectEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "deadline")
    private String deadline;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "note")
    private String note;

    @Column(name = "delete_flag")
    private Integer deleteFlag;
}