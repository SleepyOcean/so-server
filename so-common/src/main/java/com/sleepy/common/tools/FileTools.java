package com.sleepy.common.tools;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件工具类
 *
 * @author gehoubao
 * @create 2019-04-26 10:16
 **/
public class FileTools {

    public static String getProjectPath() throws IOException {
        File file = new File("");
        String filePath = file.getCanonicalPath();
        return filePath;
    }

    public static String readToString(String fileName) throws IOException {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            return new String(filecontent, encoding);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            in.close();
        }
    }

    /**
     * 文本写入文件操作类
     */
    public static class StringWriter {
        File file;
        FileOutputStream fos;

        public StringWriter(String filePath) {
            file = new File(filePath);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void writeStringToFile(String content) {
            try {
                fos.write(content.getBytes());
                fos.write("\r\n".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void printContent() {
            try {
                InputStream input = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf8"));
                String line = null;
                // 按行读取文本，直到末尾（一般都这么写）
                while ((line = reader.readLine()) != null) {
                    // 打印当前行字符串
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 图片EXIF信息获取
     */
    public static class ImgMetaHolder {
        private Map<String, Object> metaInfo;

        public ImgMetaHolder(String filePath) {
            File imageFile = new File(filePath);
            try {

                Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
                metaInfo = new LinkedHashMap<>(32);
                for (Directory directory : metadata.getDirectories()) {
                    if (directory == null) {
                        continue;
                    }
                    for (Tag tag : directory.getTags()) {
                        String tagName = tag.getTagName();
                        String desc = tag.getDescription();
                        String directName = tag.getDirectoryName();
                        if ("GPS Latitude".equals(tagName)) {
                            metaInfo.put("纬度 ", desc);
                        } else if ("GPS Longitude".equals(tagName)) {
                            metaInfo.put("经度", desc);
                        } else if ("GPS Altitude".equals(tagName)) {
                            metaInfo.put("海拔", desc);
                        } else if ("Date/Time Original".equals(tagName)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                            Date date = sdf.parse(desc);
                            metaInfo.put("拍照时间", DateTools.dateFormat(date));
                        } else if ("Date/Time".equals(tagName)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                            Date date = sdf.parse(desc);
                            metaInfo.put("创建时间", DateTools.dateFormat(date));
                        } else if ("Expected File Name Extension".equals(tagName)) {
                            metaInfo.put("图片格式", desc);
                        } else if ("File Size".equals(tagName)) {
                            metaInfo.put("图片大小", StringTools.getFormatFileSize(Double.parseDouble(desc.replaceAll("[^0-9]", "").trim())));
                        } else {
                            if ("Image Width".equals(tagName)) {
                                metaInfo.put("宽", StringTools.getIntegerNumFromString(desc));
                            } else if ("Image Height".equals(tagName)) {
                                metaInfo.put("高", StringTools.getIntegerNumFromString(desc));
                            } else if ("X Resolution".equals(tagName)) {
                                metaInfo.put("水平分辨率", StringTools.getIntegerNumFromString(desc));
                            } else if ("Y Resolution".equals(tagName)) {
                                metaInfo.put("垂直分辨率", StringTools.getIntegerNumFromString(desc));
                            }
                        }
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_FNUMBER)) {
                        //光圈F值=镜头的焦距/镜头光圈的直径
                        metaInfo.put("光圈值", directory.getDescription(ExifSubIFDDirectory.TAG_FNUMBER));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME)) {
                        metaInfo.put("曝光时间", directory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME) + "秒");
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT)) {
                        metaInfo.put("ISO速度", directory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                        metaInfo.put("焦距", directory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH) + "毫米");
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_MAX_APERTURE)) {
                        metaInfo.put("最大光圈", directory.getDouble(ExifSubIFDDirectory.TAG_MAX_APERTURE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH)) {
                        metaInfo.put("宽", directory.getString(ExifIFD0Directory.TAG_EXIF_IMAGE_WIDTH));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
                        metaInfo.put("高", directory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_MAKE)) {
                        metaInfo.put("照相机制造商", directory.getString(ExifSubIFDDirectory.TAG_MAKE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_MODEL)) {
                        metaInfo.put("照相机型号", directory.getString(ExifSubIFDDirectory.TAG_MODEL));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_X_RESOLUTION)) {
                        metaInfo.put("水平分辨率(X方向分辨率)", directory.getString(ExifSubIFDDirectory.TAG_X_RESOLUTION));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_Y_RESOLUTION)) {
                        metaInfo.put("垂直分辨率(Y方向分辨率)", directory.getString(ExifSubIFDDirectory.TAG_Y_RESOLUTION));
                    }
                    //其他参数测试开始
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_SOFTWARE)) {
                        //Software软件 显示固件Firmware版本
                        metaInfo.put("显示固件Firmware版本(图片详细信息的程序名称)", directory.getString(ExifSubIFDDirectory.TAG_SOFTWARE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH)) {
                        metaInfo.put("35mm焦距", directory.getString(ExifSubIFDDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_APERTURE)) {
                        metaInfo.put("孔径(图片分辨率单位)", directory.getString(ExifSubIFDDirectory.TAG_APERTURE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_APPLICATION_NOTES)) {
                        //一般无
                        metaInfo.put("应用程序记录", directory.getString(ExifSubIFDDirectory.TAG_APPLICATION_NOTES));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_ARTIST)) {
                        //作者
                        metaInfo.put("作者", directory.getString(ExifSubIFDDirectory.TAG_ARTIST));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_BODY_SERIAL_NUMBER)) {
                        metaInfo.put("TAG_BODY_SERIAL_NUMBER", directory.getString(ExifSubIFDDirectory.TAG_BODY_SERIAL_NUMBER));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_METERING_MODE)) {
                        //MeteringMode测光方式， 平均式测光、中央重点测光、点测光等
                        metaInfo.put("点测光值", directory.getString(ExifSubIFDDirectory.TAG_METERING_MODE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_RESOLUTION_UNIT)) {
                        //XResolution/YResolution X/Y方向分辨率
                        metaInfo.put("分辨率单位", directory.getString(ExifSubIFDDirectory.TAG_RESOLUTION_UNIT));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS)) {
                        metaInfo.put("曝光补偿", directory.getDouble(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_COLOR_SPACE)) {
                        metaInfo.put("色域、色彩空间", directory.getString(ExifSubIFDDirectory.TAG_COLOR_SPACE));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_YCBCR_COEFFICIENTS)) {
                        metaInfo.put("色相系数", directory.getString(ExifSubIFDDirectory.TAG_YCBCR_COEFFICIENTS));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_YCBCR_POSITIONING)) {
                        metaInfo.put("色相定位", directory.getString(ExifSubIFDDirectory.TAG_YCBCR_POSITIONING));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_YCBCR_SUBSAMPLING)) {
                        metaInfo.put("色相抽样", directory.getString(ExifSubIFDDirectory.TAG_YCBCR_SUBSAMPLING));
                    }
                    if (directory.containsTag(ExifSubIFDDirectory.TAG_EXIF_VERSION)) {
                        metaInfo.put("exif版本号", directory.getString(ExifSubIFDDirectory.TAG_EXIF_VERSION));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Map<String, Object> getMetaInfo() {
            return metaInfo;
        }
    }

}