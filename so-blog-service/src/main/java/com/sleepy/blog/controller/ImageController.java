package com.sleepy.blog.controller;

import com.sleepy.blog.service.ImgService;
import com.sleepy.blog.vo.ImgVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 图片服务Controller
 *
 * @author gehoubao
 * @create 2019-10-23 16:23
 **/
@Slf4j
@RestController
@CrossOrigin
@Api("图片服务Controller")
@RequestMapping("/resource/img")
public class ImageController {
    @Autowired
    ImgService imgService;

    @ApiOperation("通过id获取图片")
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        return imgService.getImg(response, id);
    }

    @ApiOperation("压缩传入的图片")
    @GetMapping("/compress")
    public void getCompressedImage(HttpServletResponse response, @RequestParam("ratio") String ratio, @RequestParam("url") String url) {
        imgService.compressImg(response, ratio, url);
    }

    /**
     * TODO 要加权限验证 - 管理员
     *
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public String saveImage(@RequestBody ImgVO vo) throws IOException {
        return imgService.upload(vo);
    }

    /**
     * TODO 要加权限验证 - 管理员
     *
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/delete")
    public String deleteImage(@RequestBody ImgVO vo) throws IOException {
        return imgService.delete(vo);
    }
}