package com.sleepy.blog.dto;

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
}
