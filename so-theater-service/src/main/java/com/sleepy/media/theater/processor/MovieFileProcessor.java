package com.sleepy.media.theater.processor;

import com.google.common.collect.Sets;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 影视资源文档重整processor
 *
 * @author gehoubao
 * @create 2021-01-21 18:39
 **/
public class MovieFileProcessor {
    Set<String> videoFormat = Sets.newHashSet(".mkv", ".mp4", ".m2ts", ".avi");
    Set<String> formatSet = new HashSet<>();

    public void regular1(String path) {

        File dir = new File(path);

        if (dir.isFile() || dir.listFiles() == null) return;

        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                String fileType = getFileType(file.getName());
                formatSet.add(fileType);
                if (videoFormat.contains(fileType.toLowerCase())) {
                    renameFile(file, file.getName().toUpperCase());
                }
            } else {
                regular1(file.getAbsolutePath());
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
        String fileType = name.substring(name.lastIndexOf("."));
        return fileType;
    }

    public static void main(String[] args) {
        MovieFileProcessor processor = new MovieFileProcessor();
        processor.regular1("G:\\Lab\\theater-part1\\1-Series(电影系列)");
        System.out.printf(processor.formatSet.toString());
//        processor.rename(new File("G:\\Lab\\1952 - (雨中曲) Singin'.in.the.Rain.1952\\162.雨中曲.1952.BluRay.1080p.DTS.2Audio.x264-CHD.mkv"));
    }
}