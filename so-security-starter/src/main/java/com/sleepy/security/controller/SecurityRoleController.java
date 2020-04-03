package com.sleepy.security.controller;

import com.sleepy.security.pojo.SecurityRoleVO;
import com.sleepy.security.service.SecurityRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:16:54
 */
@RestController
@CrossOrigin
@RequestMapping("/security/role")
public class SecurityRoleController {
    @Autowired
    SecurityRoleService securityRoleService;

    @PostMapping("/add")
    public String newRole(@RequestBody SecurityRoleVO vo) throws IOException {
        return securityRoleService.newRole(vo);
    }

    @GetMapping("/get")
    public String getRole() throws IOException {
        return securityRoleService.getRole(new SecurityRoleVO());
    }

    @GetMapping("/testAuth")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public String testAuth() throws IOException {
        return "[超级管理员]权限接口";
    }

    @GetMapping("/testNormal")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String testNormal() throws IOException {
        return "[管理员]权限接口";
    }

    @GetMapping("/test")
    public String test() throws IOException {
        return "无权限接口";
    }
}
