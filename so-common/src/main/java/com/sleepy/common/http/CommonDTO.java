package com.sleepy.common.http;

import com.sleepy.common.constant.HttpStatus;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 公共返回数据集
 *
 * @author Captain
 * @create 2019-04-20 13:28
 */
@Getter
public class CommonDTO<T> {
    private T result;
    private List<T> resultList;
    private Map<String, Object> extra;
    private Long total;
    private HttpStatus status;
    private String message;
    private Double timeout;

    public CommonDTO() {
    }

    public CommonDTO(T result) {
        this.result = result;
    }

    public CommonDTO(List<T> resultList) {
        this.resultList = resultList;
    }

    public CommonDTO(Map<String, Object> extra) {
        this.extra = extra;
    }

    public CommonDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static CommonDTO create(HttpStatus httpStatus, String message) {
        return new CommonDTO(httpStatus, message);
    }

    public static CommonDTO createError(HttpStatus httpStatus, String errorMessage) {
        return create(httpStatus, errorMessage);
    }

    public CommonDTO build() {
        return this;
    }

    public CommonDTO setResult(T result) {
        this.result = result;
        return this;
    }

    public CommonDTO setResultList(List<T> resultList) {
        this.resultList = resultList;
        return this;
    }

    public CommonDTO setExtra(Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }

    public CommonDTO setTotal(Long total) {
        this.total = total;
        return this;
    }

    public CommonDTO setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public CommonDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonDTO setTimeout(Double timeout) {
        this.timeout = timeout;
        return this;
    }
}
