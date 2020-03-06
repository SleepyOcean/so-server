package com.sleepy.file.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.ImageTools;
import com.sleepy.common.tools.StringTools;
import com.sleepy.file.common.Constant;
import com.sleepy.file.entity.ImageEntity;
import com.sleepy.file.repository.ImageRepository;
import com.sleepy.file.service.ImageService;
import com.sleepy.file.vo.ImageVO;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SleepyOcean
 * @create 2020-03-06 15:16:29
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Value("${imgDir}")
    private String imgDir;

    @Override
    public byte[] getImg(HttpServletResponse response, String id) throws IOException {
        String imgPath;
        if (id.contains(StringTools.POINT)) {
            imgPath = imgDir + "resource" + File.separator + id;
        } else {
            imgPath = imgDir + imageRepository.findLocalPathById(id);
        }
        File file = new File(imgPath);
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
            e.printStackTrace();
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
    public String upload(ImageVO vo) throws IOException {
        ImageEntity entity = JSON.parseObject(JSON.toJSONString(vo), ImageEntity.class);
        Date current = DateTools.getDateWithCurrent(0, Calendar.DAY_OF_YEAR);
        if (StringTools.isNullOrEmpty(entity.getType())) {
            entity.setType(Constant.IMG_TYPE_OTHERS);
        }
        // 图片名称的路径： 图片的类型/图片上传日期/图片的UUID， 例如： 封面/2019-10-31/2fc9e266e21f4fe18f92da2fc56567f8
        String randomName = entity.getType() + File.separator + DateTools.dateFormat(current, DateTools.DEFAULT_DATE_PATTERN) + File.separator + StringTools.getRandomUuid("");
        String imgPath = ImageTools.base64ToImgFile(vo.getImgOfBase64(), imgDir + randomName);
        try {
            FileTools.ImgMetaHolder imgMetaHolder = new FileTools.ImgMetaHolder(imgPath);
            Map imgMeta = imgMetaHolder.getMetaInfo();
            entity.setUploadTime(current);
            entity.setPath(imgPath.substring(imgDir.length()));
            entity.setCreateTime(imgMeta.get("创建时间") != null ? DateTools.toDate(imgMeta.get("创建时间").toString(), DateTools.DEFAULT_DATETIME_PATTERN) : current);
            entity.setImgSize(imgMeta.get("图片大小").toString());
            entity.setImgFormat(imgMeta.get("图片格式").toString());
            entity.setResolutionRatio(imgMeta.get("宽") + " × " + imgMeta.get("高"));
            entity = imageRepository.save(entity);
        } catch (Exception e) {
            File file = new File(imgPath);
            file.delete();
            throw e;
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("id", entity.getId());
        result.put("url", Constant.IMG_SERVER_URL_PLACEHOLDER + entity.getId());
        return new JSONObject(result).toJSONString();
    }

    @Override
    public String delete(ImageVO vo) {
        if (!StringTools.isNullOrEmpty(vo.getId())) {
            String imgPath = imgDir + imageRepository.findLocalPathById(vo.getId());
            File file = new File(imgPath);
            file.delete();
            imageRepository.deleteById(vo.getId());
        }
        return "success";
    }
}
