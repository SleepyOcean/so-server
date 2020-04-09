package com.sleepy.common.exception;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.constant.HttpStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局请求异常处理类
 *
 * @author gehoubao
 * @create 2020-04-02 18:26
 **/
@ControllerAdvice
@Slf4j
public class GlobalRequestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleServiceException(Exception e) {
        e.printStackTrace();
        log.error("【全局异常提示】请求出错 {}", e.getMessage());
        return JSON.toJSON(GlobalExceptionMessage.getExceptionMessage(HttpStatusCode.INTERNAL_ERROR, e.getMessage())).toString();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleIllegalParamException(Throwable throwable) {
        throwable.printStackTrace();
        log.error("【全局异常提示】请求出错 {}", throwable.getMessage());
        return JSON.toJSON(GlobalExceptionMessage.getExceptionMessage(HttpStatusCode.INTERNAL_ERROR, throwable.getMessage())).toString();
    }

}