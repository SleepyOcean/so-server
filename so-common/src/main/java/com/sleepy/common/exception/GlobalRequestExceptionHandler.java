package com.sleepy.common.exception;

import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.http.CommonDTO;
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
    public CommonDTO handleServiceException(Exception e) {
        LogTools.logExceptionInfo(e);
        return CommonDTO.create(HttpStatus.INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public CommonDTO handleIllegalParamException(Throwable throwable) {
        LogTools.logExceptionInfo(throwable);
        return CommonDTO.create(HttpStatus.INTERNAL_ERROR, throwable.getMessage());
    }

}