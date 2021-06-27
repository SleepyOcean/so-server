package com.sleepy.media.theater.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 公共返回数据集
 *
 * @author Captain
 * @create 2019-04-20 13:28
 */
@Data
public class CommonDTO<T> {
    private T result;
    private List<T> resultList;
    private Map<String, Object> extra;
    private Long total;
    private Integer status;
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

    public CommonDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
