package com.sleepy.file.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.*;
import com.sleepy.file.common.Constant;
import com.sleepy.file.dto.ImageDTO;
import com.sleepy.file.img.so.so_gallery.SoGallery;
import com.sleepy.file.img.so.so_gallery.SoGalleryManager;
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
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.sleepy.common.request.Check.checkStr;
import static com.sleepy.common.tools.DateTools.DEFAULT_DATETIME_PATTERN;
import static com.sleepy.common.tools.DateTools.convertToTimestamp;
import static com.sleepy.common.tools.FileTools.constructPath;
import static com.sleepy.common.tools.StringTools.getOrDefault;
import static com.sleepy.file.common.Constant.IMG_STORAGE_NAME;

/**
 * @author SleepyOcean
 * @create 2020-03-06 15:16:29
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${storage}")
    private String STORAGE_ROOT;
    @Autowired
    SoGalleryManager gallery;

    @NacosValue(value = "${image-backup-automatic-switch:false}", autoRefreshed = true)
    private String autoBackup;

    @Override
    public byte[] getImg(HttpServletResponse response, String id) throws IOException {
        String imgPath = getLocalImagePath(id);
        checkStr(imgPath);

        File file = new File(imgPath);
        if (!file.exists()) {
            log.warn("image does not exist for id[{}], path=[{}]", id, imgPath);
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
        // todo 获取图片缩略图
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
    public CommonDTO search(ImgSearchVO vo) throws IOException {
        long total = gallery.stream().count();
        List<SoGallery> data = gallery.stream().sorted(Comparator.comparing(SoGallery::getUploadTime).reversed()).skip(vo.getPageStart() * vo.getPageSize()).limit(vo.getPageSize()).collect(Collectors.toList());
        return CommonDTO.create(HttpStatus.OK, "search is done.").setResultList(data).setTotal(total);
    }

    @Override
    public CommonDTO upload(ImageVO vo) throws IOException {
        String md5 = FileTools.getStringMD5(vo.getImgOfBase64());
        // 判断文件是否已经存在，若存在则直接返回结果
        if (gallery.stream().filter(SoGallery.ID.equal(md5)).collect(Collectors.toList()).size() > 0) {
            return CommonDTO.create(HttpStatus.CONFLICT, "The image already existed")
                    .setResult(CommonTools.getCustomMap(
                            new MapModel("imageId", md5),
                            new MapModel("url", Constant.IMG_SERVER_URL_PLACEHOLDER + md5),
                            new MapModel("imgUrl", Constant.IMG_SERVER_URL_PREFIX + md5)));
        }

        String currentDay = DateTools.dateFormat(new Date(), DateTools.DEFAULT_DATE_PATTERN);
        String currentTime = DateTools.dateFormat(new Date());
        // 图片名称的路径： 图片的类型/图片上传日期/图片的UUID， 例如： 封面/2019-10-31/2fc9e266e21f4fe18f92da2fc56567f8
        String relativePath = constructPath("Gallery", currentDay, StringTools.getRandomUuid(""));
        String absolutePath = ImageTools.base64ToImgFile(vo.getImgOfBase64(), constructPath(STORAGE_ROOT, IMG_STORAGE_NAME, relativePath));

        try {
            FileTools.ImgMetaHolder imgMetaHolder = new FileTools.ImgMetaHolder(absolutePath);
            Map imgMeta = imgMetaHolder.getMetaInfo();
            SoGallery entity = gallery.create()
                    .setId(md5)
                    .setTitle(vo.getAlias())
                    .setDescription(getOrDefault(vo.getDescribeInfo(), ""))
                    .setSize(imgMeta.get("图片大小").toString())
                    .setFormat(imgMeta.get("图片格式").toString())
                    .setResolution(imgMeta.get("宽") + " × " + imgMeta.get("高"))
                    .setPath(absolutePath.substring(constructPath(STORAGE_ROOT, IMG_STORAGE_NAME).length()))
                    .setCreateTime(convertToTimestamp(getOrDefault(imgMeta.get("创建时间").toString(), currentTime)))
                    .setUploadTime(new Timestamp(System.currentTimeMillis()));
            gallery.persist(entity);
        } catch (Exception e) {
            File file = new File(absolutePath);
            file.delete();
            log.error("image save error", e);
            return CommonDTO.create(HttpStatus.INTERNAL_ERROR, e.getMessage());
        }

        return CommonDTO.create(HttpStatus.OK, "The image is uploaded successfully.")
                .setResult(CommonTools.getCustomMap(
                        new MapModel("imageId", md5),
                        new MapModel("url", Constant.IMG_SERVER_URL_PLACEHOLDER + md5),
                        new MapModel("imgUrl", Constant.IMG_SERVER_URL_PREFIX + md5)));
    }

    @Override
    public CommonDTO delete(ImageVO vo) throws IOException {
        if (!StringTools.isNullOrEmpty(vo.getImageId())) {
            deleteImage(vo.getImageId());
            return CommonDTO.create(HttpStatus.OK, String.format("Delete successfully: %s", vo.getImageId()));
        } else if (vo.getImgIds() != null && vo.getImgIds().size() > 0) {
            deleteImages(vo.getImgIds());
            return CommonDTO.create(HttpStatus.OK, String.format("Delete successfully: %s", vo.getImgIds()));
        }
        return CommonDTO.create(HttpStatus.NOT_MODIFIED, String.format("ImageId(s) not provide, do nothing."));
    }

    @Override
    public CommonDTO backup(ImageVO vo) throws IOException {
        log.info("image auto backup status: [{}]", autoBackup);

        // todo: step1. get backup data time range
        Date start = DateTools.toDate(vo.getStartTime(), DEFAULT_DATETIME_PATTERN);
        Date end = DateTools.toDate(vo.getEndTime(), DEFAULT_DATETIME_PATTERN);
        String backupPath = constructPath(STORAGE_ROOT, Constant.BACKUP_STORAGE_NAME, String.format("img-backup-%s~%s", DateTools.dateFormat(start, DateTools.CUSTOM_DATETIME_PATTERN), DateTools.dateFormat(end, DateTools.CUSTOM_DATETIME_PATTERN)));

        // todo: step2. query database to get the backup item list
        List<SoGallery> list = gallery.stream().filter(SoGallery.CREATE_TIME.between(convertToTimestamp(vo.getStartTime()), convertToTimestamp(vo.getEndTime()))).collect(Collectors.toList());

        // todo: step3. move data to backup folder according to backup item list
        for (SoGallery image : list) {
            String imagePath = getLocalImagePath(image.getId());

            String backupImgPath = constructPath(backupPath, "GalleryStorage", image.getPath().substring(0, image.getPath().lastIndexOf(".") - 33));
            File backupImgFile = new File(backupImgPath);
            if (!backupImgFile.exists()) {
                backupImgFile.mkdirs();
            }

            FileTools.copyFileToDir(new File(imagePath), backupImgFile);
        }
        FileTools.writeString(constructPath(backupPath, Constant.IMG_DATA_OUTPUT_FILE_NAME), JSON.toJSONString(list));

        // todo: step4. compress backup folder


        // todo: step5. transfer backup zip file to NAS

        return CommonDTO.create(HttpStatus.OK, String.format("backup successfully, output to: %s", backupPath));
    }

    @Override
    public CommonDTO recover(ImageVO vo) throws IOException {
        String imageDataPath = constructPath(STORAGE_ROOT, Constant.BACKUP_STORAGE_NAME, vo.getRecoverVersion(), Constant.IMG_DATA_OUTPUT_FILE_NAME);
        String imgDataJsonStr = FileTools.readToString(imageDataPath);
        JSONArray imgDataJsonArray = JSON.parseArray(imgDataJsonStr);
        for (Object o : imgDataJsonArray) {
            ImageDTO img = JSON.parseObject(o.toString(), ImageDTO.class);
            gallery.merge(gallery.create()
                    .setId(img.getImageId())
                    .setTitle(getOrDefault(img.getAlias(), img.getCreateTime()))
                    .setFormat(img.getImgFormat())
                    .setSize(img.getImgSize())
                    .setResolution(img.getResolutionRatio())
                    .setPath(img.getPath())
                    .setCreateTime(convertToTimestamp(img.getCreateTime()))
                    .setUploadTime(convertToTimestamp(img.getUploadTime())));
        }
        return CommonDTO.create(HttpStatus.OK, "recover successfully.");
    }

    private String getLocalImagePath(String imageId) {
        Optional<SoGallery> optional = gallery.stream().filter(SoGallery.ID.equal(imageId)).findFirst();
        return optional.isPresent() ? constructPath(STORAGE_ROOT, IMG_STORAGE_NAME, optional.get().getPath()) : "";
    }

    private String getLocalImagePath(SoGallery image) {
        return STORAGE_ROOT + File.separator + image.getPath();
    }

    private void deleteImage(String imgId) throws IOException {
        // delete file
        String localPath = getLocalImagePath(imgId);
        File file = new File(localPath);
        if (file.exists()) {
            file.delete();
        }
        // delete record
        gallery.remove(gallery.create().setId(imgId));
    }

    private void deleteImages(List<String> imgIds) throws IOException {
        // delete files
        gallery.stream().filter(SoGallery.ID.in(imgIds)).forEach(image -> {
            String imgPath = getLocalImagePath(image);
            File file = new File(imgPath);
            if (file.exists()) {
                file.delete();
            }
        });
        // delete records
        gallery.stream().filter(SoGallery.ID.in(imgIds)).forEach(gallery.remover());
    }


}
