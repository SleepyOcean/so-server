package com.sleepy.media.theater.controller;

import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.media.theater.constant.RegularType;
import com.sleepy.media.theater.entity.LocalVideoEntity;
import com.sleepy.media.theater.service.SoVideoService;
import com.sleepy.media.theater.vo.VideoVO;
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

    @PostMapping("/regular/moive")
    public CommonDTO<JSONObject> regularNewMovie(@RequestBody VideoVO vo) {
        String regularType = vo.getRegularType();
        if (RegularType.MOVIE_TEST.name().equals(regularType) || RegularType.MOVIE_CONFIRM.name().equals(regularType)) {
            return soVideoService.regularNewMovie(vo);
        }
        return CommonDTO.create(HttpStatus.OK, "Do nothing.");
    }
}