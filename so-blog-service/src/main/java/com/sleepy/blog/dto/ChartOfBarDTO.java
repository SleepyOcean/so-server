package com.sleepy.blog.dto;

import lombok.Data;

/**
 * 图表DTO-Bar柱形图数据结果集
 *
 * @author gehoubao
 * @create 2019-08-29 19:31
 **/
@Data
public class ChartOfBarDTO {
    private String xAxis;
    private String yAxis;

    public ChartOfBarDTO(Object xAxis, Object yAxis) {
        this.xAxis = xAxis.toString();
        this.yAxis = yAxis.toString();
    }
}