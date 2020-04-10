package com.sleepy.blog.controller;

import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.service.CustomService;
import com.sleepy.blog.vo.custom.RequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 自定义控制器
 *
 * @author gehoubao
 * @create 2020-04-07 15:23
 **/
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    CustomService customService;

    @PostMapping("/request/post")
    public String requestPost(@RequestBody Map vo) {
        return customService.requestPost(vo);
    }

    @PostMapping("/request/get")
    public String requestGet(@RequestBody Map vo) {
        return customService.requestGet(vo);
    }

    @PostMapping("/task/request")
    public CommonDTO<String> requestTask(@RequestBody RequestVO vo) throws Exception {
        return customService.requestTask(vo);
    }


    @PostMapping("/task/request/cancel")
    public CommonDTO<String> cancelRequestTask(@RequestBody Map vo) throws Exception {
        return customService.cancelRequestTask(vo);
    }

    @GetMapping("/task/request/get")
    public CommonDTO<String> getRequestTask(@RequestParam(value = "user") String user) {
        return customService.getRequestTask(user);
    }
}