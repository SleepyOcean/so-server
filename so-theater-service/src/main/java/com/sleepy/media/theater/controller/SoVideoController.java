package com.sleepy.media.theater.controller;

import com.sleepy.media.theater.entity.LocalVideoEntity;
import com.sleepy.media.theater.pojo.CommonDTO;
import com.sleepy.media.theater.service.SoVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视频API
 *
 * @author gehoubao
 * @create 2020-12-10 18:37
 **/
@RestController
@CrossOrigin
@RequestMapping("/resource/video")
public class SoVideoController {

    @Autowired
    SoVideoService soVideoService;

    @GetMapping("/{videoId}")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, @PathVariable String videoId) {
        soVideoService.getVideoStream(request, response, videoId);
    }

    @GetMapping("/local/videos/{videoType}/{sortType}/{page}")
    public CommonDTO<LocalVideoEntity> getLocalVideos(@PathVariable("videoType") String videoType,
                                                      @PathVariable("sortType") String sortType, @PathVariable("page") Integer page) {
        return soVideoService.getLocalVideos(videoType, sortType, page);
    }
}