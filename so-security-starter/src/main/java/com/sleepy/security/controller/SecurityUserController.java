package com.sleepy.security.controller;

import com.sleepy.security.pojo.SecurityUserVO;
import com.sleepy.security.service.SecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:06:55
 */
@RestController
@CrossOrigin
@RequestMapping("/security/user")
public class SecurityUserController {
    @Autowired
    SecurityUserService securityUserService;

    @PostMapping("/login")
    public String login(@RequestBody SecurityUserVO vo) throws IOException {
        return securityUserService.newUser(vo);
    }

    @PostMapping("/add")
    public String newUser(@RequestBody SecurityUserVO vo) throws IOException {
        return securityUserService.newUser(vo);
    }
}
