package com.sleepy.blog.vo;

import com.sleepy.blog.entity.ImgEntity;
import lombok.Data;

/**
 * 图片服务VO
 *
 * @author gehoubao
 * @create 2019-10-30 15:59
 **/
@Data
public class ImgVO extends ImgEntity {
    private String imgOfBase64;
}