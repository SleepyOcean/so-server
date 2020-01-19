package com.sleepy.blog.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 文件服务Controller
 *
 * @author gehoubao
 * @create 2019-12-20 15:29
 **/
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/resource/file")
public class FileController {
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile files, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        response.setCharacterEncoding("utf-8");
        String msg = "添加成功";
        log.info("-------------------开始调用上传文件upload接口-------------------");
        try {
            String name = files.getOriginalFilename();
            String path = "E:\\tmp\\" + name;
            File uploadFile = new File(path);
            files.transferTo(uploadFile);
        } catch (Exception e) {
            msg = "添加失败";
        }
        log.info("-------------------结束调用上传文件upload接口-------------------");
        json.put("msg", msg);
        return BuildJsonOfObject.buildJsonOfJsonObject(json);
    }

    public static class BuildJsonOfObject {
        public static String buildJsonOfString(String msg) {
            JSONObject json = new JSONObject();
            json.put("text", "请求成功");
            json.put("msg", msg);
            return json.toJSONString();
        }

        public static String buildJsonOfJsonObject(JSONObject jsonObj) {
            JSONObject json = new JSONObject();
            JSONObject o = new JSONObject();
            json.put("data", jsonObj);
            return json.toString();
        }
    }
}