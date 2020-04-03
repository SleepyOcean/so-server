package com.sleepy.security.entity;

import com.sleepy.security.pojo.SecurityUserVO;
import lombok.Data;

import javax.persistence.*;

/**
 * 用户表
 *
 * @author gehoubao
 * @create 2020-03-31 11:24
 **/
@Data
@Entity
@Table(name = "so_user")
public class SoUserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT '用户名'")
    private String name;

    @Column(name = "phone", columnDefinition = "VARCHAR(32) COMMENT '手机号'")
    private String phone;

    @Column(name = "mail", columnDefinition = "VARCHAR(64) COMMENT '邮箱'")
    private String mail;

    @Column(name = "password", columnDefinition = "VARCHAR(64) COMMENT '密码'")
    private String password;

    @Column(name = "roles", columnDefinition = "VARCHAR(255) COMMENT '关联角色，多个以逗号分隔'")
    private String roles;

    @Column(name = "status", columnDefinition = "TINYINT COMMENT '用户状态，0正常，1注销'")
    private int status;

    public SoUserEntity() {
    }

    public SoUserEntity(SecurityUserVO vo) {
        this.name = vo.getUserName();
        if (null != vo.getPhone() && "".equals(vo.getPhone())) {
            this.phone = vo.getPhone();
        }
        if (null != vo.getMail() && "".equals(vo.getMail())) {
            this.mail = vo.getMail();
        }
        this.password = vo.getPassword();
    }
}