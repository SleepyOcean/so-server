package com.sleepy.crawler.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 异步图片下载任务
 *
 * @author gehoubao
 * @create 2020-11-18 9:17
 **/
@Component
public class ImageDownloadTask {

    @Async
    public void downloadImg(String url, String savePath, String name) throws IOException {
        String suffix = url.lastIndexOf('.') > 0 ? url.substring(url.lastIndexOf('.')) : "";
        if (suffix.contains("webp") || StringUtils.isEmpty(suffix)) {
            suffix = ".jpg";
        }
        name += suffix;
        String localSavePath = savePath + File.separator + name;
        URL url1 = new URL(url);
        URLConnection uc = url1.openConnection();
        File file = new File(localSavePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (InputStream inputStream = uc.getInputStream()) {
            FileOutputStream out = new FileOutputStream(localSavePath);
            int j = 0;
            while ((j = inputStream.read()) != -1) {
                out.write(j);
            }
        }
    }
}