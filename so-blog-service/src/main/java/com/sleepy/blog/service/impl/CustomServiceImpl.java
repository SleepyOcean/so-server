package com.sleepy.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.processor.ScheduleProcessor;
import com.sleepy.blog.service.CustomService;
import com.sleepy.blog.task.RequestTask;
import com.sleepy.blog.vo.custom.RequestVO;
import com.sleepy.common.exception.UserOperationIllegalException;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.HttpTools;
import com.sleepy.common.tools.LogTools;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * 自定义接口服务
 *
 * @author gehoubao
 * @create 2020-04-10 13:04
 **/
@Slf4j
@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    ScheduleProcessor scheduleProcessor;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @NacosValue(value = "${so.schedule.auto-send-message.key:key}", autoRefreshed = true)
    private String key;

    @Override
    public String requestGet(Map vo) {
        return HttpTools.doGet(vo.get("url").toString());
    }

    @Override
    public String requestPost(Map vo) {
        return HttpTools.doPost(vo.get("url").toString(), JSON.toJSONString(vo.get("params")));
    }

    @Override
    public CommonDTO<String> requestTask(RequestVO vo) throws UserOperationIllegalException {
        CommonDTO<String> result = new CommonDTO<>();
        String user = vo.getUser();
        String time = vo.getTime();
        String cron = "0 " + time.substring(3, 5) + " " + time.substring(0, 2) + " * * ? *";
        if (StringTools.isNotNullOrEmpty(vo.getCron())) {
            cron = vo.getCron();
        }
        String registerUser = stringRedisTemplate.opsForValue().get("RegisterUser");
        if (StringTools.isNotNullOrEmpty(user) && StringTools.isNotNullOrEmpty(registerUser) && Arrays.asList(registerUser.split(",")).contains(user)) {
            scheduleProcessor.addJob("requestJob-" + time.replaceAll(":", ""),
                    "requestJobGroup",
                    "requestTrigger-" + time.replaceAll(":", ""),
                    "requestTriggerGroup", RequestTask.class,
                    cron, vo);

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

    @Override
    public CommonDTO<String> cancelRequestTask(Map vo) throws UserOperationIllegalException {
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

    @Override
    public CommonDTO<String> getRequestTask(String user) {
        CommonDTO<String> result = new CommonDTO<>();
        result.setExtra(CommonTools.getCustomMap(new MapModel("taskList", stringRedisTemplate.opsForHash().entries("requestTask:" + user).values())));
        return result;
    }

    @Override
    public void recoverScheduleTask() {
        String registerUser = stringRedisTemplate.opsForValue().get("RegisterUser");
        if (StringTools.isNotNullOrEmpty(registerUser)) {
            String[] users = registerUser.split(",");
            for (int i = 0; i < users.length; i++) {
                stringRedisTemplate.opsForHash().entries("requestTask:" + users[i]).values().forEach(vo -> {
                    try {
                        requestTask(JSON.toJavaObject(JSON.parseObject(vo.toString()), RequestVO.class));
                    } catch (UserOperationIllegalException e) {
                        LogTools.logExceptionInfo(e);
                    }
                });
            }
        }
        log.info("定时任务 - 恢复成功");
    }

}