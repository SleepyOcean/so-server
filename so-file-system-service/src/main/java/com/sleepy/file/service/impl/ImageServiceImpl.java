package com.sleepy.file.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.tools.*;
import com.sleepy.file.common.Constant;
import com.sleepy.file.dao.ImageDAO;
import com.sleepy.file.dto.ImageDTO;
import com.sleepy.file.service.ImageService;
import com.sleepy.file.vo.ImageVO;
import com.sleepy.file.vo.ImgSearchVO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;

/**
 * @author SleepyOcean
 * @create 2020-03-06 15:16:29
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageDAO imageDAO;

    @Value("${imgDir}")
    private String imgDir;

    @Override
    public byte[] getImg(HttpServletResponse response, String id) throws IOException {
        String imgPath;
        if (id.contains(StringTools.POINT)) {
            imgPath = imgDir + "resource" + File.separator + id;
        } else {
            String imgName = imageDAO.findLocalPathById(id);
            if (StringTools.isNotNullOrEmpty(imgName)) {
                imgPath = imgDir + imgName;
            } else {
                return new byte[0];
            }
        }
        File file = new File(imgPath);
        if (!file.exists()) {
            return new byte[0];
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return bytes;
    }

    @Override
    public byte[] getImgThumbnail(HttpServletResponse response, String id) {
        return new byte[0];
    }

    @Override
    public void compressImg(HttpServletResponse response, String ratio, String url) {
        OutputStream outputStream = null;
        try {
            if (url != null) {
                URL path = new URL(StringTools.formatUrl(url));
                response.setContentType("image/jpeg");
                response.addHeader("Connection", "keep-alive");
                response.addHeader("Cache-Control", "max-age=604800");
                outputStream = response.getOutputStream();
                Thumbnails.of(path).scale(Float.parseFloat(ratio)).outputFormat("jpeg").toOutputStream(outputStream);
            }
        } catch (NumberFormatException e) {
            log.error("图片压缩失败，ratio值应为float类型，如ratio=0.25f(缩小至0.25倍)，失败URL：{}", url);
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
            log.error("{} 获取图片失败！{} {}", "/compress请求", e.getMessage(), url);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("{} 流关闭失败！{}", "/compress请求", e.getMessage());
            }
        }
    }

    @Override
    public Map<String, Object> search(ImgSearchVO vo) throws IOException {
        return imageDAO.search(vo);
    }

    @Override
    public String upload(ImageVO vo) throws IOException {
        String imageId;
        String md5 = FileTools.getStringMD5(vo.getImgOfBase64());
        imageId = md5;
        Map<String, Object> result = new HashMap<>(4);
        if (StringTools.isNullOrEmpty(imageDAO.findLocalPathById(md5))) {
            ImageDTO entity = JSON.parseObject(JSON.toJSONString(vo), ImageDTO.class);
            String currentDay = DateTools.dateFormat(new Date(), DateTools.DEFAULT_DATE_PATTERN);
            String currentTime = DateTools.dateFormat(new Date());
            if (StringTools.isNullOrEmpty(entity.getType())) {
                entity.setType(Constant.IMG_TYPE_GALLERY);
            }
            // 图片名称的路径： 图片的类型/图片上传日期/图片的UUID， 例如： 封面/2019-10-31/2fc9e266e21f4fe18f92da2fc56567f8
            String randomName = entity.getType() + File.separator + currentDay + File.separator + StringTools.getRandomUuid("");
            String imgPath = ImageTools.base64ToImgFile(vo.getImgOfBase64(), imgDir + randomName);

            try {
                FileTools.ImgMetaHolder imgMetaHolder = new FileTools.ImgMetaHolder(imgPath);
                Map imgMeta = imgMetaHolder.getMetaInfo();
                if (StringTools.isNotNullOrEmpty(vo.getAlias())) {
                    entity.setAlias(vo.getAlias());
                }
                if (StringTools.isNotNullOrEmpty(vo.getDescribeInfo())) {
                    entity.setDescribeInfo(vo.getDescribeInfo());
                }
                entity.setUploadTime(currentTime);
                entity.setPath(imgPath.substring(imgDir.length()));
                entity.setCreateTime(imgMeta.get("创建时间") != null ? imgMeta.get("创建时间").toString() : currentTime);
                entity.setImgSize(imgMeta.get("图片大小").toString());
                entity.setImgFormat(imgMeta.get("图片格式").toString());
                entity.setResolutionRatio(imgMeta.get("宽") + " × " + imgMeta.get("高"));
                entity.setImageId(imageId);
                imageDAO.save(entity);
                result.put("status", HttpStatus.OK);
            } catch (Exception e) {
                File file = new File(imgPath);
                file.delete();
                result.put("status", HttpStatus.INTERNAL_ERROR);
                result.put("message", e.getMessage());
                return new JSONObject(result).toJSONString();
            }
        } else {
            result.put("status", HttpStatus.NOT_ACCEPTABLE);
        }
        result.put("id", imageId);
        result.put("url", Constant.IMG_SERVER_URL_PLACEHOLDER + imageId);
        result.put("imgUrl", Constant.IMG_SERVER_URL_PREFIX + imageId);
        return new JSONObject(result).toJSONString();
    }

    @Override
    public String delete(ImageVO vo) throws IOException {
        if (!StringTools.isNullOrEmpty(vo.getImageId())) {
            deleteSingleImg(vo.getImageId());
        }
        if (vo.getImgIds() != null && vo.getImgIds().size() > 0) {
            batchDeleteImg(vo.getImgIds());
        }
        return "success";
    }

    private void deleteSingleImg(String imgId) throws IOException {
        String imgPath = imgDir + imageDAO.findLocalPathById(imgId);
        imageDAO.deleteByIds(Arrays.asList(imgId));
        File file = new File(imgPath);
        if (file.exists()) {
            file.delete();
        }
    }

    private void batchDeleteImg(List<String> imgIds) throws IOException {
        List<String> localPaths = imageDAO.findLocalPathByIds(imgIds);
        imageDAO.deleteByIds(imgIds);
        localPaths.forEach(path -> {
            String imgPath = imgDir + path;
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        });
    }

}
