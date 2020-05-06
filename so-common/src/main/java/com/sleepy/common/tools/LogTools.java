package com.sleepy.common.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 日志工具类
 *
 * @author gehoubao
 * @create 2020-05-06 10:35
 **/
@Slf4j
public class LogTools {

    /**
     * 打印 Exception类 异常信息
     *
     * @param e 异常对象
     */
    public static void logExceptionInfo(Exception e) {
        log.error("异常信息(message): {}, 异常类型(class): {}, 异常堆栈(stackTrack): {}", e.getMessage(), e.getClass().getName(), Arrays.asList(e.getStackTrace()).toString());
    }

    /**
     * 打印 Throwable类 异常信息
     *
     * @param t 异常对象
     */
    public static void logExceptionInfo(Throwable t) {
        log.error("异常信息(message): {}, 异常类型(class): {}, 异常堆栈(stackTrack): {}", t.getMessage(), t.getClass().getName(), Arrays.asList(t.getStackTrace()).toString());
    }
}