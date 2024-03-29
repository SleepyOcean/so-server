package com.sleepy.media.theater.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.StringTools;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * 影视资源文档重整processor
 *
 * @author gehoubao
 * @create 2021-01-21 18:39
 **/
@Component
public class MovieFileProcessor {
    Set<String> videoFormat = Sets.newHashSet(".mkv", ".mp4", ".m2ts", ".avi", ".MKV", ".m4v", ".wmv");
    Set<String> subtitleFormat = Sets.newHashSet(".idx", ".ssa", ".srt", ".sub", ".SRT", ".sup", ".ass");
    Set<String> formatSet = new HashSet<>();
    List<String> errorList = new ArrayList<>();
    TMDbProcessor tmDbProcessor = new TMDbProcessor();
    FakeFileProcessor fakeFileProcessor = new FakeFileProcessor();

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
        try {
            if (videoFormat.contains(getFileType(fileName)) && !fileName.contains("trailer")) {
                File parentFile = dir.getParentFile();
                String key = parentFile.getName().substring(parentFile.getName().indexOf("(") + 1, parentFile.getName().indexOf(")"));
                fileMap.put(key, dir.getParent());
            }
        } catch (Exception e) {
            System.out.printf(fileName);
        }
    }

    public JSONObject regularOffline(String sourcePath, String targetPath, String cacheFetchMap, boolean confirmRename) {
        File targetRoot = new File(targetPath);
        Map<String, File> targetMap = new HashMap<>(1024);
        getTargetVideoDir(targetRoot, targetMap);

        File sourceRoot = new File(sourcePath);
        Map<String, String> sourceMap = new HashMap<>(1024);
        getMetaVideoDir(sourceRoot, sourceMap);

        List<String> pathList = new ArrayList<>();
        getAllVideoFile(new File(targetPath), pathList);
        Map<String, String> resultMap = new HashMap<>();
        if (StringTools.isNotNullOrEmpty(cacheFetchMap)) {
            resultMap = getFetchKeyMap(cacheFetchMap);
        } else {
            resultMap = getFetchKeyMap(sourceMap, targetMap);
        }

        JSONObject renameInfo = renameTargetFile(sourceMap, targetMap, resultMap, confirmRename);
        JSONObject copyInfo = copyMetaFileToTarget(sourceMap, targetMap, resultMap, confirmRename);

        JSONObject result = new JSONObject();
        result.put("rename", renameInfo);
        result.put("copy", copyInfo);
        return result;
    }

    private JSONObject renameTargetFile(Map<String, String> sourceMap, Map<String, File> targetMap, Map<String, String> fetchMap, boolean confirmRename) {
        JSONObject result = new JSONObject();
        JSONArray info = new JSONArray();
        fetchMap.forEach((key, val) -> {
            File targetDir = targetMap.get(key);
            File sourceDir = new File(sourceMap.get(val));
            String[] sourceVideoNames = sourceDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    return file.isFile() && ".nfo".equals(getFileType(file.getName()));
                }
            });
            String movieName = sourceVideoNames[0].substring(0, sourceVideoNames[0].lastIndexOf("."));

            // rename 之前需要将原文件信息写入 original_meta.json
            writeMetaJsonFile(targetDir, confirmRename);

            // 开始重命名
            File[] targetRenameFile = targetDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    return file.isFile() && (videoFormat.contains(getFileType(file.getName())) || subtitleFormat.contains(getFileType(file.getName())));
                }
            });
            // 重命名电影目录下的影视文件（视频文件、字幕文件等等）
            for (File file : targetRenameFile) {
                if (confirmRename) {
                    renameFile(file, movieName + getFileType(file.getName()));
                }
                JSONObject item = new JSONObject();
                item.put("fromName", file.getName());
                item.put("toName", movieName + getFileType(file.getName()));
                item.put("fromParentPath", file.getParent());
                info.add(item);
            }
        });
        result.put("output", info);
        return result;
    }

    private JSONObject copyMetaFileToTarget(Map<String, String> sourceMap, Map<String, File> targetMap, Map<String, String> fetchMap, boolean confirmRename) {
        JSONObject result = new JSONObject();
        JSONArray info = new JSONArray();
        JSONArray error = new JSONArray();
        Map<String, String> failedMap = new HashMap<>(1024);
        fetchMap.forEach((key, val) -> {
            File targetDir = targetMap.get(key);
            File sourceDir = new File(sourceMap.get(val));
            File[] sourceFiles = sourceDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    return file.isFile() && !videoFormat.contains(getFileType(file.getName()));
                }
            });
            for (File sourceFile : sourceFiles) {
                try {
                    if (confirmRename) {
                        FileTools.copyFileToDir(sourceFile, targetDir);
                    }
                    JSONObject item = new JSONObject();
                    item.put("fromFile", sourceFile);
                    item.put("toDir", targetDir);
                    info.add(item);
                } catch (IOException e) {
                    JSONObject item = new JSONObject();
                    item.put("fromFile", sourceFile.getAbsolutePath());
                    item.put("errorMsg", e.getMessage());
                    error.add(item);
                }
            }
            // 重命名电影目录，将中文影视名替换为匹配到的中文名
            String dirName = targetDir.getName();
            String dirNewName = dirName.replace(key, val);
            if (confirmRename) {
                renameFile(targetDir, dirNewName);
            }
        });
        result.put("output", info);
        result.put("error", error);
        return result;
    }

    private Map<String, String> getFetchKeyMap(String cachePath) {
        Map<String, String> fetchMap = new HashMap<>();
        try {
            String fetchMapString = FileTools.readToString(cachePath);
            fetchMap = JSON.parseObject(fetchMapString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fetchMap;
    }

    private Map<String, String> getFetchKeyMap(Map<String, String> sourceMap, Map<String, File> targetMap) {
        Map<String, String> resultMap = new HashMap<>(1024);
        Map<String, String> unMatchMap = new HashMap<>(1024);
        Map<String, String> resultMapDetail = new HashMap<>(1024);

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
                resultMapDetail.put(targetMap.get(targetKey).getName(), new File(sourceMap.get(matchKey)).getName());
            }
        }
        List<String> targetList = new ArrayList<>(targetMap.keySet());
        List<String> sourceList = new ArrayList<>(sourceMap.keySet());

        Collections.sort(targetList);
        Collections.sort(sourceList);
        return resultMap;
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
                String key = parentFile.getName().substring(parentFile.getName().indexOf("(") + 1, parentFile.getName().indexOf(")"));
                fileMap.put(key, dir.getParentFile());
            } catch (Exception e) {
                errorList.add(parentFile.getPath());
            }
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

    public void batchRenameMovieFileName(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                batchRenameMovieFileName(file);
            }
            return;
        }
        String fileName = dir.getName();
        File parentFile = dir.getParentFile();
        if (videoFormat.contains(getFileType(fileName))) {
            try {
                String key = parentFile.getName().substring(parentFile.getName().indexOf("(") + 1, parentFile.getName().indexOf(")"));
                renameFile(dir.getParentFile(), key);
            } catch (Exception e) {
                System.out.printf(fileName);
            }
        }
    }

    /**
     * 将电影的原始名称写入当前目录的original_meta.json文件中
     *
     * @param targetDir
     * @param confirmRename
     */
    public void writeMetaJsonFile(File targetDir, boolean confirmRename) {
        Map<String, String> originalMeta = new HashMap<>(128);
        try {
            originalMeta.put("originalFileList", JSON.toJSONString(fakeFileProcessor.readFileTree(targetDir.getPath())));
        } catch (Exception e) {
            System.out.println(targetDir);
        }
        if (confirmRename) {
            FileTools.writeString(targetDir.getAbsolutePath() + File.separator + "original_meta.json", JSON.toJSONString(originalMeta));
        }
    }

    /**
     * Page18整理方法： 将sourceDir目录下的所有视频文件移动到toDir
     *
     * @param sourceDir
     * @param toDir
     */
    public void regularPage18Video(String sourceDir, String toDir) {
        File dirFile = new File(sourceDir);
        List<File> videoFiles = new ArrayList<>();
        for (File file : dirFile.listFiles()) {
            File[] files = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    return file.isFile() && videoFormat.contains(getFileType(file.getName()));
                }
            });
            videoFiles.addAll(Arrays.asList(files));
        }

        videoFiles.forEach(file -> {
            try {
                FileTools.moveFileToDir(file.getAbsolutePath(), toDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void renameMovieDirForTmmSearch(File movieDir) {
        String newName = movieDir.getName().substring(movieDir.getName().indexOf("(") + 1, movieDir.getName().indexOf(")"));
        renameFile(movieDir, newName);
    }
}