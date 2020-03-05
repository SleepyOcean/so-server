package com.sleepy.common.model;

import lombok.Data;

/**
 * MapModel
 *
 * @author gehoubao
 * @create 2020-02-17 12:37
 **/
@Data
public class MapModel {
    private String key;
    private Object value;

    public MapModel(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}