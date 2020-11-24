package com.sleepy.common.box;

import java.io.File;

/**
 * 文件处理器
 *
 * @author gehoubao
 * @create 2020-11-24 16:03
 **/
public class FileBox {

    public static void main(String[] args) {
//        // controller-service-serviceImpl代码生成
//        generateMVCCodeFile("E:\\MicroProjects\\so-server\\so-security-starter\\src\\main\\java\\com\\sleepy\\security",
//                "com.sleepy.security", "SecurityRole");

        String dirPath = "G:\\BaiduNetdiskDownload\\26-《四驱兄弟》无印篇";

        changeSuffix(dirPath);
//        String matchPrefix = "[SC-OL][Sword Art Online][";
//        String newPrefix = "刀剑神域.Sword.Art.Online.";
//
//        boolean rename = false;
//        rename = true;
//        batchRename(dirPath, matchPrefix, newPrefix, rename);
    }

    private static void changeSuffix(String dirPath) {
        File dir = new File(dirPath);

        for (File file : dir.listFiles()) {
            String originName = file.getName();

            String newName = originName.substring(originName.indexOf("]") + 1).substring(originName.indexOf("爆走兄弟Let's&Go!!") + 5, originName.indexOf("[64") - 9) + ".mp4";
            file.renameTo(new File(file.getParent() + "\\" + newName));
            System.out.println(String.format("newName=%s, oldName=%s", newName, originName));
        }
    }

    private static void batchRename(String dirPath, String matchPrefix, String newPrefix, boolean rename) {
        File dir = new File(dirPath);

        System.out.println(dirPath);

        for (File file : dir.listFiles()) {
            String originName = file.getName();
            String suffix = originName.substring(originName.lastIndexOf("."), originName.length());

            String index = originName.substring(matchPrefix.length()).substring(0, originName.substring(matchPrefix.length()).indexOf("]"));
            String newName = newPrefix + index + suffix;

            if (rename) {
                file.renameTo(new File(file.getParent() + "\\" + newName));
            }
            System.out.println(String.format("newName=%s, oldName=%s", newName, originName));
        }
    }
}