package com.sleepy.file.controller;

import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.file.service.ImageService;
import com.sleepy.file.vo.ImageVO;
import com.sleepy.file.vo.ImgSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 图片服务
 *
 * @author SleepyOcean
 * @create 2020-03-06 15:16:29
 */
@RestController
@CrossOrigin
@RequestMapping("/resource/img")
public class ImageController {
    @Autowired
    ImageService imageService;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        return imageService.getImg(response, id);
    }

    @GetMapping(value = "/thumbnail/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getThumbnailImage(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        return imageService.getImgThumbnail(response, id);
    }

    @GetMapping("/compress")
    public void getCompressedImage(HttpServletResponse response, @RequestParam("ratio") String ratio, @RequestParam("url") String url) {
        imageService.compressImg(response, ratio, url);
    }

    @PostMapping("/search")
    @ResponseBody
    public CommonDTO search(@RequestBody ImgSearchVO vo) throws IOException {
        return imageService.search(vo);
    }

    /**
     * TODO 要加权限验证 - 管理员
     *
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/save")
    public CommonDTO saveImage(@RequestBody ImageVO vo) throws IOException {
        return imageService.upload(vo);
    }

    /**
     * TODO 要加权限验证 - 管理员
     *
     * @param vo
     * @return
     * @throws IOException
     */
    @PostMapping("/delete")
    public CommonDTO deleteImage(@RequestBody @Valid ImageVO vo) throws IOException {
        if ("4352".equals(vo.getEditCode())) {
            return imageService.delete(vo);
        } else {
            return CommonDTO.create(HttpStatus.BAD_REQUEST, "editCode 错误!操作失败");
        }
    }

    @PostMapping("/backup")
    public CommonDTO backup(@RequestBody @Valid ImageVO vo) throws IOException {
        return imageService.backup(vo);
    }

    @PostMapping("/recover")
    public CommonDTO recover(@RequestBody @Valid ImageVO vo) throws IOException {
        return imageService.recover(vo);
    }
}
