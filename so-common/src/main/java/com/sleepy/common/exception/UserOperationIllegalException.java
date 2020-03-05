package com.sleepy.common.exception;

/**
 * 用户操作非法异常
 *
 * @author gehoubao
 * @create 2020-03-05 15:08
 **/
public class UserOperationIllegalException extends Exception {

    public UserOperationIllegalException() {
    }

    public UserOperationIllegalException(String message) {
        super(message);
    }
}