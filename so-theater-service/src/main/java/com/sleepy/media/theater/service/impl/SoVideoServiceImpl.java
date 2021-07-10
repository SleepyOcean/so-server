package com.sleepy.media.theater.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.media.theater.constant.RegularType;
import com.sleepy.media.theater.entity.LocalVideoEntity;
import com.sleepy.media.theater.processor.MovieFileProcessor;
import com.sleepy.media.theater.processor.VideoProcessor;
import com.sleepy.media.theater.service.SoVideoService;
import com.sleepy.media.theater.vo.VideoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.sleepy.common.request.Check.checkStr;

/**
 * 视频Service实现类
 *
 * @author gehoubao
 * @create 2020-12-10 18:39
 **/
@Service
public class SoVideoServiceImpl implements SoVideoService {

    @Autowired
    VideoProcessor videoProcessor;

    @Autowired
    MovieFileProcessor movieFileProcessor;

    @Override
    public void getVideoStream(HttpServletRequest request, HttpServletResponse response, String videoId) {
        String fileName = "testvideo.mkv";
        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        try {
            //获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            File originalFile = new File("G:\\" + fileName);
            File file = new File(videoProcessor.convertToMp4(originalFile));
            if (file.exists()) {
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if (rangeString != null) {

                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes " + range + "-" + (fileLength - 1) + "/" + fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                } else {//下载

                    //设置响应头，把文件名字设置好
                    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                    //设置文件长度
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    //解决编码问题
                    response.setHeader("Content-Type", "application/octet-stream");
                }


                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache)) != -1) {
                    outputStream.write(cache, 0, flag);
                }
            } else {
                String message = "file:" + fileName + " not exists";
                //解决编码问题
                response.setHeader("Content-Type", "application/json");
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    @Override
    public CommonDTO<LocalVideoEntity> getLocalVideos(String videoType, String sortType, Integer page) {
        return null;
    }

    @Override
    public CommonDTO<JSONObject> regularNewMovie(VideoVO vo) {
        boolean modify = false;
        if (RegularType.MOVIE_CONFIRM.name().equals(vo.getRegularType())) {
            modify = true;
        }
        String sourcePath = checkStr(vo.getMetaPath());
        String targetPath = checkStr(vo.getTargetPath());
        String targetMetaMapPath = checkStr(vo.getTargetMetaMapPath());

        JSONObject result = movieFileProcessor.regularOffline(sourcePath, targetPath, targetMetaMapPath, modify);
        result.put("fileChange", modify);
        return CommonDTO.create(HttpStatus.OK, "check").setResult(result).build();
    }
}