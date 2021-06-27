package com.sleepy.media.theater.service;

import com.sleepy.media.theater.entity.LocalVideoEntity;
import com.sleepy.media.theater.pojo.CommonDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视频service
 *
 * @author gehoubao
 * @create 2020-12-10 18:38
 **/

public interface SoVideoService {
    void getVideoStream(HttpServletRequest request, HttpServletResponse response, String videoId);

    CommonDTO<LocalVideoEntity> getLocalVideos(String videoType, String sortType, Integer page);
}
