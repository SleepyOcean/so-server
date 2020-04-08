package com.sleepy.blog.controller;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.tools.HttpTools;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/request/post")
    public String requestPost(@RequestBody Map vo) {
        return HttpTools.doPost(vo.get("url").toString(), JSON.toJSONString(vo.get("params")));
    }

    @PostMapping("/request/get")
    public String requestGet(@RequestBody Map vo) {
        return HttpTools.doGet(vo.get("url").toString());
    }
}