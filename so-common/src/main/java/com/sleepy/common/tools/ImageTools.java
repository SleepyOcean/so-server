package com.sleepy.common.tools;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片工具类
 *
 * @author gehoubao
 * @create 2019-10-23 13:49
 **/
public class ImageTools {

    /**
     * 压缩图片
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void compressImg(String src, String dest) throws IOException {
        Thumbnails.of(src).scale(1).toFile(dest);
    }

    /**
     * base64字符串保存为图片
     *
     * @param base64Str
     * @param destFileName
     * @throws IOException
     */
    public static String base64ToImgFile(String base64Str, String destFileName) throws IOException {
        String format = null;
        if (destFileName.contains(StringTools.POINT)) {
            format = destFileName.substring(destFileName.indexOf(".") + 1);
            destFileName = destFileName.substring(0, destFileName.indexOf("."));
        }
        if (base64Str.contains(StringTools.COMMA)) {
            format = base64Str.substring(base64Str.indexOf("/") + 1, base64Str.indexOf(";"));
            base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
        }

        String dest = destFileName + StringTools.POINT + (StringTools.isNullOrEmpty(format) ? "jpg" : format);
        //对字节数组字符串进行Base64解码并生成图片
        byte[] bs = Base64Utils.decodeFromString(base64Str);
        FileUtils.writeByteArrayToFile(new File(dest), bs);
        return dest;
    }

    public static void downloadImg(String url, String savePath, String name) throws IOException {
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

    public static void main(String[] args) throws IOException {
        downloadImg("https://img9.doubanio.com/view/photo/r/public/p2413297334.webp",
                "G:\\test-data\\2002 - (谍影重重) The Bourne Identity.2002\\capture", "幽灵公主");
    }
}