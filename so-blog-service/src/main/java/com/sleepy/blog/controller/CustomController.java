package com.sleepy.blog.controller;

import com.alibaba.fastjson.JSON;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.processor.ScheduleProcessor;
import com.sleepy.blog.task.RequestTask;
import com.sleepy.blog.vo.custom.RequestVO;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.HttpTools;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    ScheduleProcessor scheduleProcessor;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    private String key = "key";

    @PostMapping("/request/post")
    public String requestPost(@RequestBody Map vo) {
        return HttpTools.doPost(vo.get("url").toString(), JSON.toJSONString(vo.get("params")));
    }

    @PostMapping("/request/get")
    public String requestGet(@RequestBody Map vo) {
        return HttpTools.doGet(vo.get("url").toString());
    }

    @PostMapping("/task/request")
    public CommonDTO<String> requestTask(@RequestBody RequestVO vo) throws Exception {
        CommonDTO<String> result = new CommonDTO<>();
        String user = vo.getUser();
        String time = vo.getTime();
        String registerUser = stringRedisTemplate.opsForValue().get("RegisterUser");
        if (StringTools.isNotNullOrEmpty(user) && StringTools.isNotNullOrEmpty(registerUser) && Arrays.asList(registerUser.split(",")).contains(user)) {
            scheduleProcessor.addJob("requestJob-" + time.replaceAll(":", ""), "requestJobGroup", "requestTrigger-" + time.replaceAll(":", ""), "requestTriggerGroup", RequestTask.class, "0 " + time.substring(3, 5) + " " + time.substring(0, 2) + " * * ? *", vo);

            synchronized (key) {
                vo.setId(time.replaceAll(":", ""));
                stringRedisTemplate.opsForHash().put("requestTask:" + user, time.replaceAll(":", ""), JSON.toJSONString(vo));
            }

            result.setResult("定时任务创建成功");
        } else {
            CommonTools.throwUserExceptionInfo("未授权");
        }
        return result;
    }


    @PostMapping("/task/request/cancel")
    public CommonDTO<String> cancelRequestTask(@RequestBody Map vo) throws Exception {
        CommonDTO<String> result = new CommonDTO<>();
        String user = vo.get("user").toString();
        String id = vo.get("id").toString();
        String registerUser = stringRedisTemplate.opsForValue().get("RegisterUser");
        if (StringTools.isNotNullOrEmpty(registerUser) && Arrays.asList(registerUser.split(",")).contains(user)) {
            scheduleProcessor.removeJob("requestJob-" + id, "requestJobGroup",
                    "requestTrigger-" + id, "requestTriggerGroup");

            synchronized (key) {
                stringRedisTemplate.opsForHash().delete("requestTask:" + user, id);
            }
            result.setResult("定时任务取消成功");
        } else {
            CommonTools.throwUserExceptionInfo("未授权");
        }
        return result;
    }

    @GetMapping("/task/request/get")
    public CommonDTO<String> getRequestTask(@RequestParam(value = "user") String user) {
        CommonDTO<String> result = new CommonDTO<>();
        result.setExtra(CommonTools.getCustomMap(new MapModel("taskList", stringRedisTemplate.opsForHash().entries("requestTask:" + user).values())));
        return result;
    }
}