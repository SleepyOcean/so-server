package com.sleepy.file.vo;

import com.sleepy.file.entity.ImageEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 图片VO
 *
 * @author gehoubao
 * @create 2020-03-06 16:13
 **/
@Data
public class ImageVO extends ImageEntity {
    private String imgOfBase64;

    @NotEmpty(message = "请输入editCode")
    private String editCode;
}