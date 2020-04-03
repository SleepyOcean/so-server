package com.sleepy.security.pojo;

import lombok.Data;

/**
 * 用户VO
 *
 * @author gehoubao
 * @create 2020-03-31 18:13
 **/
@Data
public class SecurityUserVO {
    private String userName;
    private String password;
    private String name;
    private String phone;
    private String mail;
}