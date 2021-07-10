package com.sleepy.common.exception;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.tools.LogTools;
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
public class GlobalRequestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleServiceException(Exception e) {
        LogTools.logExceptionInfo(e);
        return JSON.toJSON(GlobalExceptionMessage.getExceptionMessage(HttpStatus.INTERNAL_ERROR.code(), e.getMessage())).toString();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleIllegalParamException(Throwable throwable) {
        LogTools.logExceptionInfo(throwable);
        return JSON.toJSON(GlobalExceptionMessage.getExceptionMessage(HttpStatus.INTERNAL_ERROR.code(), throwable.getMessage())).toString();
    }

}