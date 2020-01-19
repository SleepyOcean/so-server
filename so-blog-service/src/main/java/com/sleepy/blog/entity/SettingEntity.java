package com.sleepy.blog.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 设置实体类
 *
 * @author gehoubao
 * @create 2019-09-03 16:18
 **/
@Data
@Entity
@Table(name = "so_setting")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SettingEntity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "configKey", columnDefinition = "varchar(255) comment '配置项的键'")
    private String configKey;
    @Column(name = "configValue", columnDefinition = "varchar(255) comment '配置项的值'")
    private String configValue;
    @Column(name = "configInfo", columnDefinition = "varchar(255) comment '配置项的备注'")
    private String configInfo;
}