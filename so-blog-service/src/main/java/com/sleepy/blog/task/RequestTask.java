package com.sleepy.blog.task;

import com.alibaba.fastjson.JSON;
import com.sleepy.blog.vo.custom.RequestVO;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.HttpTools;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Map;

/**
 * 定时请求任务
 *
 * @author gehoubao
 * @create 2020-04-09 14:59
 **/
@Slf4j
public class RequestTask implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        RequestVO vo = (RequestVO) jobExecutionContext.getJobDetail().getJobDataMap().get("params");
        Map<String, Object> params = CommonTools.getCustomMap(new MapModel("msgtype", "text"),
                new MapModel("text", CommonTools.getCustomMap(new MapModel("content", vo.getMsg()), new MapModel("mentioned_mobile_list", vo.getMention()))));
        HttpTools.doPost(vo.getUrl(), JSON.toJSONString(params));
        log.info("【定时任务 - 请求】发送定时消息，[{}] 内容： {}", vo.getUrl(), vo.getMsg());
    }
}