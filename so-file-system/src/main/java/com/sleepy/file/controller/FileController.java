package com.sleepy.file.controller;

import com.sleepy.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/***
 *
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
    @Autowired
    private FileService fileService;

    @GetMapping("/checkExist/{name}")
    public Boolean checkExist(@PathVariable("name") String name) throws IOException {
        return fileService.checkFileExist(name);
    }

    @GetMapping("/merge/{name}")
    public String merge(@PathVariable("name") String name) throws IOException {
        return fileService.mergeFile(name);
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile files, @RequestParam(value = "fileMd5", required = false) String fileMd5, @RequestParam(name = "chunk", defaultValue = "-1") Integer chunk) throws IOException {
        return fileService.uploadFile(files, fileMd5, chunk);
    }

    @GetMapping(value = "/get")
    public Object getDir(HttpServletRequest request, HttpServletResponse response, @RequestParam("dir") String path, @RequestParam(name = "ratio", defaultValue = "1") String ratio) throws IOException {
        return fileService.getDir(request, response, path, ratio);
    }

    @GetMapping(value = "/{fileName}")
    public void getDir(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String name) throws IOException {
        fileService.getFile(request, response, name);
    }
}