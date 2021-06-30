package com.sleepy.media.theater.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.sleepy.common.tools.FileTools;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.File;
import java.util.*;

/**
 * 影视资源文档重整processor
 *
 * @author gehoubao
 * @create 2021-01-21 18:39
 **/
public class MovieFileProcessor {
    Set<String> videoFormat = Sets.newHashSet(".mkv", ".mp4", ".m2ts", ".avi", ".MKV", ".m4v");
    Set<String> formatSet = new HashSet<>();
    List<String> errorList = new ArrayList<>();
    TMDbProcessor tmDbProcessor = new TMDbProcessor();

    public Map<String, String> getMetaFileMap(String path) {
        Map<String, String> result = new HashMap<>(1024);
        File dir = new File(path);
        getMetaVideoDir(dir, result);
        return result;
    }

    private void getMetaVideoDir(File dir, Map<String, String> fileMap) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                getMetaVideoDir(file, fileMap);
            }
            return;
        }
        String fileName = dir.getName();
        if (videoFormat.contains(getFileType(fileName)) && !fileName.contains("trailer")) {
            File parentFile = dir.getParentFile();
            String key = parentFile.getName().substring(parentFile.getName().indexOf("(") + 1, parentFile.getName().indexOf(")"));
            fileMap.put(key, dir.getParent());
        }
    }

    public void regularOffline(String sourcePath, String targetPath) {
        Map<String, String> resultMap = new HashMap<>(1024);
        Map<String, String> unMatchMap = new HashMap<>(1024);

        File targetRoot = new File(targetPath);
        Map<String, File> targetMap = new HashMap<>(1024);
        getTargetVideoDir(targetRoot, targetMap);

        File sourceRoot = new File(sourcePath);
        Map<String, String> sourceMap = new HashMap<>(1024);
        getMetaVideoDir(sourceRoot, sourceMap);
        for (String targetKey : targetMap.keySet()) {
            int maxRatio = 0;
            String matchKey = "";
            for (String sourceKey : sourceMap.keySet()) {
                int ratio = FuzzySearch.ratio(targetKey, sourceKey);
                if (ratio > 10 && ratio > maxRatio) {
                    maxRatio = ratio;
                    matchKey = sourceKey;
                }
            }
            if ("".equals(matchKey)) {
                unMatchMap.put(targetKey, matchKey);
            } else {
                resultMap.put(targetKey, matchKey);
            }
        }
        List<String> targetList = new ArrayList<>(targetMap.keySet());
        List<String> sourceList = new ArrayList<>(sourceMap.keySet());

        Collections.sort(targetList);
        Collections.sort(sourceList);
        System.out.println(resultMap);
    }

    public void regularOnline(String path, String cachePath) {
        File root = new File(path);
        Map<String, File> result = new HashMap<>(1024);
        getTargetVideoDir(root, result);
        result.forEach((key, val) -> {
            JSONArray array = tmDbProcessor.searchMovie(key, 1);
            JSONObject matchObj = array.getJSONObject(0);
            System.out.println(String.format("original name: %s -> match name: %s", key.toString(), matchObj.getString("title")));
            FileTools.writeString(cachePath + matchObj.getString("title") + ".json", matchObj.toJSONString());
        });
        System.out.println(result);
    }

    private void getTargetVideoDir(File dir, Map<String, File> fileMap) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                getTargetVideoDir(file, fileMap);
            }
            return;
        }
        String fileName = dir.getName();
        if (videoFormat.contains(getFileType(fileName))) {
            File parentFile = dir.getParentFile();
            try {
                String key = parentFile.getName().substring(parentFile.getName().indexOf("(") + 1, parentFile.getName().lastIndexOf(")"));
                fileMap.put(key, dir.getParentFile());
            } catch (Exception e) {
                errorList.add(parentFile.getPath());
            }
        }
    }

    private void rename(File file) {
        String fileName = file.getName();
        String parentName = file.getParentFile().getName();

        long fileSize = file.length();
//        if (fileSize < 1292475391) return;

        if (parentName.indexOf("(") > 0 && parentName.lastIndexOf(")") > 0) {
            String chineseName = parentName.substring(parentName.indexOf("(") + 1, parentName.lastIndexOf(")"));
            renameFile(file.getParentFile(), chineseName);
        }
    }

    public static void renameFile(File file, String name) {
        String newFileName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + name;
        File newFile = new File(newFileName);
        if (file.exists()) {
            file.renameTo(newFile);
        }
    }

    public void getDestMovieDirList(String rootPath) {

    }

    private String getFileType(String name) {
        String fileType = "";
        try {
            fileType = name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            System.out.println("error: " + name);
        }
        return fileType;
    }

    public void findMultiVideoFile(File dir, Set<String> pathList) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                findMultiVideoFile(file, pathList);
            }
            return;
        }
        String fileName = dir.getName();
        if (videoFormat.contains(getFileType(fileName))) {
            int videoCount = 0;
            for (File file : dir.getParentFile().listFiles()) {
                if (file.isFile() && videoFormat.contains(getFileType(file.getName()))) {
                    videoCount++;
                }
            }
            if (videoCount > 1) {
                pathList.add(dir.getParent());
            }
        }
    }

    public void getAllFileType(File dir, Map<String, List<String>> fileTypeMap) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                getAllFileType(file, fileTypeMap);
            }
            return;
        }
        String fileName = dir.getName();
        List list = fileTypeMap.get(getFileType(fileName));
        if (null == list || list.isEmpty()) {
            list = new ArrayList();
        }
        list.add(dir.getAbsolutePath());
        fileTypeMap.put(getFileType(fileName), list);
    }

    public void getAllVideoFile(File dir, List<String> pathList) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                getAllVideoFile(file, pathList);
            }
            return;
        }
        String fileName = dir.getName();
        if (videoFormat.contains(getFileType(fileName))) {
            pathList.add(dir.getAbsolutePath());
        }
    }
}