package com.sleepy.security.entity;

import com.sleepy.security.pojo.SecurityRoleVO;
import lombok.Data;

import javax.persistence.*;

/**
 * 角色表
 *
 * @author gehoubao
 * @create 2020-03-31 11:25
 **/
@Data
@Entity
@Table(name = "so_role")
public class SoRoleEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name_abbr", columnDefinition = "VARCHAR(255) COMMENT '角色名称简写'")
    private String nameAbbr;

    @Column(name = "name", columnDefinition = "VARCHAR(255) COMMENT '角色名称'")
    private String name;

    @Column(name = "status", columnDefinition = "TINYINT COMMENT '用户状态，0正常，1注销'")
    private int status;

    public SoRoleEntity() {
    }

    public SoRoleEntity(SecurityRoleVO vo) {
        this.name = vo.getName();
        this.nameAbbr = vo.getNameAbbr();
    }
}