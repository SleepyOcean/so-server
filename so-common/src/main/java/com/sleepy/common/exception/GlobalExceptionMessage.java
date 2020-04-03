package com.sleepy.common.exception;

import lombok.Data;

/**
 * 全局异常返回信息类
 *
 * @author gehoubao
 * @create 2020-04-03 10:36
 **/
@Data
public class GlobalExceptionMessage {
    private Integer status;
    private String message;
    private Double timeout;

    public GlobalExceptionMessage(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static GlobalExceptionMessage getExceptionMessage(int status, String message) {
        return new GlobalExceptionMessage(status, message);
    }
}