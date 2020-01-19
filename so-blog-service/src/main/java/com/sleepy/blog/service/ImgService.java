package com.sleepy.blog.service;

import com.sleepy.blog.vo.ImgVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片服务
 *
 * @author ghb
 * @create 2019-10-30 16:05
 **/
public interface ImgService {

    /**
     * 获取图片接口
     *
     * @param response
     * @param id
     * @return
     * @throws IOException
     */
    byte[] getImg(HttpServletResponse response, String id) throws IOException;

    /**
     * 图片压缩接口
     *
     * @param response
     * @param ratio
     * @param url
     */
    void compressImg(HttpServletResponse response, String ratio, String url);

    /**
     * 图片上传接口
     *
     * @param vo
     * @return
     * @throws IOException
     */
    String upload(ImgVO vo) throws IOException;

    /**
     * 图片删除接口
     *
     * @param vo
     * @return
     * @throws IOException
     */
    String delete(ImgVO vo) throws IOException;
}
