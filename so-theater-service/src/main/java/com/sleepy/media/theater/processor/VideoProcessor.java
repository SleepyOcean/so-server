package com.sleepy.media.theater.processor;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 视频处理器
 *
 * @author gehoubao
 * @create 2020-12-10 20:35
 **/
@Component
public class VideoProcessor {
    public String convertToMp4(File file) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(file);
        String fileName = null;
        Frame captured_frame = null;
        FFmpegFrameRecorder recorder = null;

        try {
            frameGrabber.start();
            fileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".")) + ".mp4";
            recorder = new FFmpegFrameRecorder(fileName, frameGrabber.getImageWidth(), frameGrabber.getImageHeight(), frameGrabber.getAudioChannels());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); //avcodec.AV_CODEC_ID_H264  //AV_CODEC_ID_MPEG4
            recorder.setFormat("mp4");
            recorder.setFrameRate(frameGrabber.getFrameRate());
            //recorder.setSampleFormat(frameGrabber.getSampleFormat()); //
            recorder.setSampleRate(frameGrabber.getSampleRate());

            recorder.setAudioChannels(frameGrabber.getAudioChannels());
            recorder.setFrameRate(frameGrabber.getFrameRate());
            recorder.start();
            while ((captured_frame = frameGrabber.grabFrame()) != null) {
                try {
                    recorder.setTimestamp(frameGrabber.getTimestamp());
                    recorder.record(captured_frame);

                } catch (Exception e) {
                }
            }
            recorder.stop();
            recorder.release();
            frameGrabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //file.delete();
        return fileName;
    }
}