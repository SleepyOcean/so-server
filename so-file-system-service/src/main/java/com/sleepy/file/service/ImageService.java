package com.sleepy.file.service;

import com.sleepy.file.vo.ImageVO;
import com.sleepy.file.vo.ImgSearchVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author SleepyOcean
 * @create 2020-03-06 15:16:29
 */
public interface ImageService {
    /**
     * 通过id获取图片
     *
     * @param response
     * @param id
     * @return
     * @throws IOException
     */
    byte[] getImg(HttpServletResponse response, String id) throws IOException;

    /**
     * 通过id获取图片缩略图
     *
     * @param response
     * @param id
     * @return
     */
    byte[] getImgThumbnail(HttpServletResponse response, String id);

    /**
     * 获取压缩图片
     *
     * @param response
     * @param ratio
     * @param url
     */
    void compressImg(HttpServletResponse response, String ratio, String url);

    /**
     * 搜索图片
     *
     * @param vo
     * @return
     */
    Map<String, Object> search(ImgSearchVO vo) throws IOException;

    /**
     * 上传图片
     *
     * @param vo
     * @return
     * @throws IOException
     */
    String upload(ImageVO vo) throws IOException;

    /**
     * 删除图片
     *
     * @param vo
     * @return
     */
    String delete(ImageVO vo) throws IOException;

    /**
     * 备份图片数据
     *
     * @return
     * @throws IOException
     */
    String backup() throws IOException;

    String recover() throws IOException;
}
