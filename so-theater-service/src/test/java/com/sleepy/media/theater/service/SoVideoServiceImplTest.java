package com.sleepy.media.theater.service;

import com.sleepy.media.theater.constant.RegularType;
import com.sleepy.media.theater.service.impl.SoVideoServiceImpl;
import com.sleepy.media.theater.vo.VideoVO;
import org.junit.jupiter.api.Test;

/**
 * SoVideoServiceImpl test
 *
 * @author gehoubao
 * @create 2021-07-10 15:32
 **/
public class SoVideoServiceImplTest {

    SoVideoServiceImpl socketVideoService = new SoVideoServiceImpl();

    @Test
    public void regularNewMovieTest() {
        VideoVO vo = new VideoVO();
        vo.setRegularType(RegularType.MOVIE_TEST.toString());
        vo.setMetaPath("G:\\2-实验目录\\MovieLab\\meta");
        vo.setTargetPath("G:\\2-实验目录\\MovieLab\\target");
        vo.setTargetMetaMapPath("G:\\2-实验目录\\MovieLab\\metaTargetMap.json");
        socketVideoService.regularNewMovie(vo);
    }
}