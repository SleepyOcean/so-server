package com.sleepy.blog.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限验证控制器
 *
 * @author gehoubao
 * @create 2019-04-19 15:34
 **/
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    @ApiOperation("退出登录")
    @GetMapping("/logoutSuccess")
    public void logoutSuccess() {
    }

    @ApiOperation("登录成功")
    @GetMapping("/loginInfo")
    public void loginSuccess() {
    }
}