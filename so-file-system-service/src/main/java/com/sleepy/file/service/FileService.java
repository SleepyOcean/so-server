package com.sleepy.file.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Captain1920
 * @create 2020/2/1 13:25
 */
public interface FileService {
    /**
     * 判断文件是否存在
     *
     * @param name
     * @return
     */
    Boolean checkFileExist(String name);

    /**
     * 合并大文件
     *
     * @param name
     * @return
     */
    String mergeFile(String name);

    /**
     * 上传文件
     *
     * @param files
     * @param fileMd5
     * @param chunk
     * @return
     */
    String uploadFile(MultipartFile files, String fileMd5, Integer chunk) throws IOException;

    /**
     * 获取文件或文件夹内容
     *
     * @param request
     * @param response
     * @param path
     * @param ratio
     * @return
     */
    Object getDir(HttpServletRequest request, HttpServletResponse response, String path, String ratio) throws IOException;

    /**
     * 获取文件
     *
     * @param request
     * @param response
     * @param name
     */
    void getFile(HttpServletRequest request, HttpServletResponse response, String name) throws IOException;
}
