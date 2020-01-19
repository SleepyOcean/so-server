package com.sleepy.blog.controller;

import com.sleepy.blog.processor.ScheduleProcessor;
import com.sleepy.blog.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 测试
 *
 * @author Captain
 * @create 2019-04-13 15:45
 */
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    ScheduleProcessor scheduleProcessor;

    @GetMapping("/test")
    public Map<String, Object> test() {
        return testService.test();
    }

    @GetMapping("/backupSQL")
    public String backupSql() {
        scheduleProcessor.backupSoImg();
        scheduleProcessor.backupSoProject();
        scheduleProcessor.backupSoSetting();
        return "success";
    }
}
