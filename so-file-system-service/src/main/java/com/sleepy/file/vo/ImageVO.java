package com.sleepy.file.vo;

import com.sleepy.file.dto.ImageDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 图片VO
 *
 * @author gehoubao
 * @create 2020-03-06 16:13
 **/
@Data
public class ImageVO extends ImageDTO {
    private String imgOfBase64;

    @NotEmpty(message = "请输入editCode")
    private String editCode;

    private List<String> imgIds;

    private String deleteType;

    private String startTime;

    private String endTime;

    private String recoverVersion;
}