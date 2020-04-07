package com.sleepy.blog.controller;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.HttpTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<String, Object> timeTaskList = new ConcurrentHashMap<>(16);

    @PostMapping("/task/request")
    public String requestTask(@RequestBody Map vo) throws ParseException {
        long dayS = 24 * 60 * 60 * 1000;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '" + vo.get("time").toString() + "'");
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
        if (System.currentTimeMillis() > startTime.getTime()) {
            startTime = new Date(startTime.getTime() + dayS);
        }

        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Map<String, Object> params = CommonTools.getCustomMap(new MapModel("msgtype", "text"), new MapModel("text", CommonTools.getCustomMap(new MapModel("content", vo.get("msg").toString()))));
                HttpTools.doPost(vo.get("url").toString(), JSON.toJSONString(params));
                log.info("【定时任务 - 请求】发送定时消息，[{}] 内容： {}", vo.get("url").toString(), vo.get("msg").toString());
            }
        };

        synchronized (timeTaskList) {
            Timer tmp = (Timer) timeTaskList.get(vo.get("name"));
            if (tmp != null) {
                tmp.cancel();
            }
            t.scheduleAtFixedRate(task, startTime, dayS);
            timeTaskList.put(vo.get("name").toString(), t);
        }
        return "定时任务创建成功";
    }


    @GetMapping("/task/request/cancel/{id}")
    public String cancelRequestTask(@PathVariable("id") String id) throws ParseException {
        synchronized (timeTaskList) {
            ((Timer) timeTaskList.get(id)).cancel();
            timeTaskList.remove(id);
        }
        return "定时任务取消成功";
    }

    @GetMapping("/task/request/get")
    public String getRequestTask() {
        List<String> request = new ArrayList<>(timeTaskList.keySet());
        request.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String s1 = o1.substring(0, 5);
                String s2 = o2.substring(0, 5);
                if (s1.compareTo(s2) > 0) {
                    return 1;
                } else if (s1.compareTo(s2) < 0) {
                    return -1;
                }
                return 0;
            }
        });
        return JSON.toJSONString(request);
    }
}