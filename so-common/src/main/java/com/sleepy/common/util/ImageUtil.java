package com.sleepy.common.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.IOException;

/**
 * 图片工具类
 *
 * @author gehoubao
 * @create 2019-10-23 13:49
 **/
public class ImageUtil {

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
        if (destFileName.contains(StringUtil.POINT)) {
            format = destFileName.substring(destFileName.indexOf(".") + 1);
            destFileName = destFileName.substring(0, destFileName.indexOf("."));
        }
        if (base64Str.contains(StringUtil.COMMA)) {
            format = base64Str.substring(base64Str.indexOf("/") + 1, base64Str.indexOf(";"));
            base64Str = base64Str.substring(base64Str.indexOf(",") + 1);
        }

        String dest = destFileName + StringUtil.POINT + (StringUtil.isNullOrEmpty(format) ? "jpg" : format);
        //对字节数组字符串进行Base64解码并生成图片
        byte[] bs = Base64Utils.decodeFromString(base64Str);
        FileUtils.writeByteArrayToFile(new File(dest), bs);
        return dest;
    }

    public static void main(String[] args) throws IOException {
        String src = FileUtil.readToString("E:\\Dev Tools\\DockerFile\\ImageServer\\test");
        String dest = "E:\\Dev Tools\\DockerFile\\ImageServer\\home-bg.jpg";
        base64ToImgFile(src, dest);
    }
}