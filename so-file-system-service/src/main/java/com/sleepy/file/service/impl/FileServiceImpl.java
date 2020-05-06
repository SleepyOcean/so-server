package com.sleepy.file.service.impl;

import com.sleepy.common.tools.LogTools;
import com.sleepy.file.service.FileService;
import com.sleepy.file.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件服务 ServiceImpl
 *
 * @author gehoubao
 * @create 2020-01-31 14:29
 **/
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private static String CHUNK_SUFFIX = ".chunk";
    @Value("${rootDir}")
    private String rootDir;
    private String currentPath = "FileServer";
    private ConcurrentHashMap<String, String> uploadFileMap = new ConcurrentHashMap<>(4);

    @Override
    public Boolean checkFileExist(String name) {
        String path = rootDir + currentPath + name;
        File uploadFile = new File(path);
        if (uploadFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String mergeFile(String name) {
        if (CommonUtil.stringIsNotEmpty(uploadFileMap.get(name))) {
            File chunkFileFolder = new File(uploadFileMap.get(name) + CHUNK_SUFFIX);
            File[] files = chunkFileFolder.listFiles();

            File mergeFile = new File(uploadFileMap.get(name));
            List<File> fileList = Arrays.asList(files);
            mergeFile(fileList, mergeFile);
            CommonUtil.delFile(chunkFileFolder);
            uploadFileMap.remove(name);
            return "success";
        } else {
            return "未找到" + name + "文件分片";
        }
    }

    @Override
    public String uploadFile(MultipartFile files, String fileMd5, Integer chunk) throws IOException {
        String name = files.getOriginalFilename();
        String msg = "200";
        if (chunk < 0) {
            try {
                String path = rootDir + currentPath + name;
                File uploadFile = new File(path);
                files.transferTo(uploadFile);
                msg = "添加成功";
            } catch (Exception e) {
                msg = "添加失败: " + e;
            }
        } else {
            if (!CommonUtil.stringIsNotEmpty(uploadFileMap.get(name))) {
                uploadFileMap.put(name, rootDir + currentPath + name);
            }
            String path = uploadFileMap.get(name);
            File tmpFolder = new File(path + CHUNK_SUFFIX);
            File chunkFile = new File(path + CHUNK_SUFFIX + File.separator + chunk);
            if (!tmpFolder.exists()) {
                tmpFolder.mkdirs();
            }
            if (!chunkFile.exists()) {
                // 上传文件输入流
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                inputStream = files.getInputStream();
                outputStream = new FileOutputStream(chunkFile);
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            }
        }
        return msg;
    }

    @Override
    public Object getDir(HttpServletRequest request, HttpServletResponse response, String path, String ratio) throws IOException {
        String dir = rootDir + path;
        Map<String, Object> result = new HashMap<>(4);
        List<Map> dirList = new ArrayList<>();
        File file = new File(dir);
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String s : list) {
                File f = new File(dir + File.separator + s);
                Map<String, Object> item = new HashMap<>(2);
                item.put("name", f.getName());
                if (f.isDirectory()) {
                    item.put("type", "dir");
                } else {
                    if (f.getName().lastIndexOf('.') > -1) {
                        String suffix = f.getName().substring(f.getName().lastIndexOf('.'));
                        item.put("type", suffix);
                        item.put("category", CommonUtil.getCategoryBySuffix(suffix));
                    } else {
                        item.put("type", "default");
                    }
                }
                item.put("size", CommonUtil.getFormatFileSize(f.length()));
                dirList.add(item);
            }
            result.put("dir", dirList);
            currentPath = path + File.separator;
        } else {
            String suffix = file.getName().substring(file.getName().lastIndexOf('.'));
            if (CommonUtil.CATEGORY_IMAGE.equals(CommonUtil.getCategoryBySuffix(suffix))) {
                getImageStream(response, dir, ratio);
                return null;
            }
            if (CommonUtil.CATEGORY_AUDIO.equals(CommonUtil.getCategoryBySuffix(suffix))) {
                getAudioStream(request, response, file);
                return null;
            }
        }
        return result;
    }

    @Override
    public void getFile(HttpServletRequest request, HttpServletResponse response, String name) throws IOException {
        String dir = rootDir + name;
        File file = new File(dir);
        String suffix = file.getName().substring(file.getName().lastIndexOf('.'));
        if (CommonUtil.CATEGORY_IMAGE.equals(CommonUtil.getCategoryBySuffix(suffix))) {
            getImageStream(response, dir, "1");
        }
        if (CommonUtil.CATEGORY_AUDIO.equals(CommonUtil.getCategoryBySuffix(suffix))) {
            getAudioStream(request, response, file);
        }
        if (CommonUtil.CATEGORY_HTML.equals(CommonUtil.getCategoryBySuffix(suffix))) {
            getHTMLStream(request, response, file);
        }

        // 设置response的Header
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("application/x-font-ttf");

        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[fis.available()];
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

    @Override
    public String uploadStaticFile(MultipartFile files, String fileMd5, Integer chunk) {
        return null;
    }

    @Override
    public void getStaticFile(HttpServletRequest request, HttpServletResponse response, String name) {

    }

    private void getAudioStream(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        String range = request.getHeader("Range");
        String[] rs = range.split("\\=");
        range = rs[1].split("\\-")[0];
        int start = Integer.parseInt(range);
        long length = file.length();
        response.addHeader("Accept-Ranges", "bytes");
        response.addHeader("Content-Length", length + "");
        response.addHeader("Content-Range", "bytes " + start + "-" + (length - 1) + "/" + length);
        response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");

        OutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        fis.skip(start);
        FileCopyUtils.copy(fis, os);
    }

    private void getImageStream(HttpServletResponse response, String dir, String ratio) {
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("image/jpeg");
            response.addHeader("Connection", "keep-alive");
            response.addHeader("Cache-Control", "max-age=604800");
            Thumbnails.of(dir).scale(Float.parseFloat(ratio)).outputFormat("jpeg").toOutputStream(outputStream);
        } catch (NumberFormatException e) {
            LogTools.logExceptionInfo(e);
            log.error("图片压缩失败，ratio值应为float类型，如ratio=0.25f(缩小至0.25倍)，失败URL：{}", dir);
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
            log.error("{} 获取图片失败！{} {}", "/compress请求", e.getMessage(), dir);
        }
    }

    private void getHTMLStream(HttpServletRequest request, HttpServletResponse response, File file) {
        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fis = new FileInputStream(file);) {
            response.setContentType("text/html;charset=utf-8");
            FileCopyUtils.copy(fis, outputStream);
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
            log.error("获取文件失败！ {}", e.getMessage());
        }
    }

    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        try {
            // 有删 无创建
            if (mergeFile.exists()) {
                mergeFile.delete();
            } else {
                mergeFile.createNewFile();
            }
            // 排序
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });

            byte[] b = new byte[1024];
            RandomAccessFile writeFile = new RandomAccessFile(mergeFile, "rw");
            for (File chunkFile : chunkFileList) {
                RandomAccessFile readFile = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                while ((len = readFile.read(b)) != -1) {
                    writeFile.write(b, 0, len);
                }
                readFile.close();
            }
            writeFile.close();
            return mergeFile;

        } catch (IOException e) {
            LogTools.logExceptionInfo(e);
            return null;
        }
    }
}
