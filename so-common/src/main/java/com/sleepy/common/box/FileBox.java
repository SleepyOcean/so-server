package com.sleepy.common.box;

import com.alibaba.fastjson.JSON;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.FileTools;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件处理器
 *
 * @author gehoubao
 * @create 2020-11-24 16:03
 **/
public class FileBox {

    static boolean rename = false;

    public static void main(String[] args) {
//        // controller-service-serviceImpl代码生成
//        generateMVCCodeFile("G:\\generateCode",
//                "com.sleepy.media.theater", "SoMetaInfo");

        int i = 0;

        int a = 1;

        int b = 2;

        rename = false;
//        rename = true;

        for (i = a; i <= b; i++)
        createBatchRenameTask(i);

//        renameByReplace();
//        addSuffix();
    }

    public static void addSuffix() {
        String dirPath = "\\\\DS218plus\\0-Cinema2\\4-Cartoon(动漫)\\《我们仍未知道那天所看见的花的名字》";

        File dir = new File(dirPath);

        System.out.println(dirPath);

        int count = 0;
        for (File file : dir.listFiles()) {
            String originName = file.getName();
            try {
                String newName = originName + ".mkv";
                if (rename) {
                    file.renameTo(new File(file.getParent() + "\\" + newName));
                }
                System.out.println(String.format("NewName=%s  <--  OldName=%s", newName, originName));
                count++;
            } catch (Exception e) {
                System.out.println(String.format("skip the file: %s", originName));
            }
        }
        System.out.println(String.format("Total rename %s files", count));
    }

    public static void renameByReplace() {
        String dirPath = "\\\\DS218plus\\theater-part3\\4-Cartoon(动漫)\\20-《一人之下》S01-S03\\一人之下.S02";

        File dir = new File(dirPath);

        System.out.println(dirPath);

        int count = 0;
        for (File file : dir.listFiles()) {
            String originName = file.getName();
            if (originName.contains("torrent")) continue;

            try {
//                String fileFormatSuffix = "";
//                if (file.isFile()) {
//                    fileFormatSuffix = originName.substring(originName.lastIndexOf("."), originName.length());
//                }
//                String replaceName = originName;
//                String prefixName = "第";
//                String suffixName = "话";
//                String newName = "四驱兄弟.S01E" + getEpisodeIndex(replaceName, prefixName, suffixName, 2) + "."
//                        + originName.substring(originName.indexOf("「") + 1, originName.lastIndexOf("」")) + fileFormatSuffix;

                String newName = originName.replace("一人之下 第二季：第", "一人之下.S02E");
                newName = newName.substring(0, newName.lastIndexOf("["));
                String prefixName = "一人之下.S02E";
                String suffixName = newName.substring(newName.lastIndexOf("话 "));
                newName = "一人之下.S02E" + getEpisodeIndex(newName, prefixName, "话", 2) + suffixName + ".flv";
                newName = newName.replace("话 ", ".");
                if (rename) {
                    file.renameTo(new File(file.getParent() + "\\" + newName));
                }
                System.out.println(String.format("NewName=%s  <--  OldName=%s", newName, originName));
                count++;
            } catch (Exception e) {
                System.out.println(String.format("skip the file: %s", originName));
            }
        }
        System.out.println(String.format("Total rename %s files", count));
    }

    private static void createBatchRenameTask(int index) {
        String dirPath = "\\\\DS218plus\\0-Cinema2\\4-Cartoon(动漫)\\《哆啦A梦》\\哆啦A梦.S0" + (index == 0 ? "" : index);

//        changeSuffix(dirPath);

        String matchPrefix = "季 (";
        String matchSuffix = ")";
        String newPrefix = "哆啦A梦.S0"+index+"E";
        String newSuffix = "";

        batchRename(dirPath, matchPrefix, matchSuffix, newPrefix, newSuffix, rename);
    }

    /**
     * batch rename ver1.0
     *
     * @param dirPath
     */
    private static void changeSuffix(String dirPath) {
        File dir = new File(dirPath);

        for (File file : dir.listFiles()) {
            String originName = file.getName();

            String newName = originName.substring(originName.indexOf("]") + 1).substring(originName.indexOf("爆走兄弟Let's&Go!!") + 5, originName.indexOf("[64") - 9) + ".mp4";
            file.renameTo(new File(file.getParent() + "\\" + newName));
            System.out.println(String.format("newName=%s, oldName=%s", newName, originName));
        }
    }

    /**
     * batch rename ver2.0
     *
     * @param dirPath
     * @param matchPrefix
     * @param newPrefix
     * @param rename
     */
    private static void batchRename(String dirPath, String matchPrefix, String matchSuffix, String newPrefix, String newSuffix, boolean rename) {
        File dir = new File(dirPath);

        System.out.println(dirPath);

        int count = 0;
        for (File file : dir.listFiles()) {
            String originName = file.getName();
            String suffix = "";
            if (file.isFile()) {
                suffix = newSuffix + originName.substring(originName.lastIndexOf("."), originName.length());
            }

            try {
                String fileName = file.isFile() ? originName.substring(0, originName.lastIndexOf(".")) : originName;
                String index = getEpisodeIndex(fileName, matchPrefix, matchSuffix, 2);
//                String index = originName.substring(0, originName.lastIndexOf("."));
                String newName = newPrefix + index + suffix;

                if (rename) {
                    file.renameTo(new File(file.getParent() + "\\" + newName));
//                    System.out.println(String.format("write data to %s", file.getParent() + "\\" + newName));
                }
                System.out.println(String.format("NewName=%s  <--  OldName=%s", newName, originName));
                count++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("skip the file: %s, because %s", originName, e.getMessage()));
            }
        }
        System.out.println(String.format("Total rename %s files", count));
    }

    private static String getEpisodeIndex(String name, String prefix, String suffix, int bit) throws Exception {
        String indexStr = prefix.length() + suffix.length() > 0 ? "" : name;
        if (suffix.length() > 0 && name.contains(suffix)) {
            if (prefix.isEmpty()) {
                indexStr = name.substring(0, name.substring(prefix.length()).indexOf(suffix));
            } else {
                indexStr = name.substring(prefix.length()).substring(0, name.substring(prefix.length()).indexOf(suffix));
            }
        } else {
            if (!prefix.isEmpty() && name.contains(prefix)) {
                indexStr = name.substring(name.indexOf(prefix) + prefix.length());
            }
        }
        if ("".equals(indexStr)) {
            throw new Exception("No file match.");
        }
        try {
            Integer.parseInt(indexStr);
        } catch (Exception e) {
            return indexStr;
        }
        if (indexStr.length() < bit) {
            return getLoopCharString("0", bit - indexStr.length()) + indexStr;
        }
        return indexStr;
    }

    private static String getLoopCharString(String ch, int times) {
        StringBuilder sb = new StringBuilder(ch);
        for (int i = 1; i < times; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 根据传入的名称生成Controller、service和serviceImpl文件
     *
     * @param distPath
     * @param mvcName
     */
    public static void generateMVCCodeFile(String distPath, String packagePath, String mvcName) {
        String controllerFile = distPath + File.separator + "controller" + File.separator + mvcName + "Controller.java";
        String serviceFile = distPath + File.separator + "service" + File.separator + mvcName + "Service.java";
        String serviceImplFile = distPath + File.separator + "service" + File.separator + "impl" + File.separator + mvcName + "ServiceImpl.java";
        String notes = "/**\n" +
                " * @author SleepyOcean\n" +
                " * @create " + DateTools.dateFormat(new Date()) + "\n" +
                " */\n";
        StringBuilder controllerTemplate = new StringBuilder();
        StringBuilder serviceTemplate = new StringBuilder();
        StringBuilder serviceImplTemplate = new StringBuilder();
        controllerTemplate.append("package " + packagePath + ".controller;\n")
                .append("import " + packagePath + ".service." + mvcName + "Service;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.web.bind.annotation.*;\n").append(notes)
                .append("@RestController\n" +
                        "@CrossOrigin\n" +
                        "@RequestMapping(\"\")\n")
                .append("public class " + mvcName + "Controller {\n" +
                        "    @Autowired\n" +
                        "    " + mvcName + "Service " + mvcName.toLowerCase() + "Service;\n" +
                        "}");
        serviceTemplate.append("package " + packagePath + ".service;\n").append(notes)
                .append("public interface " + mvcName + "Service {}");
        serviceImplTemplate.append("package " + packagePath + ".service.impl;\n")
                .append("import " + packagePath + ".service." + mvcName + "Service;\n" +
                        "import lombok.extern.slf4j.Slf4j;\n" +
                        "import org.springframework.stereotype.Service;\n").append(notes)
                .append("@Service\n" +
                        "@Slf4j\n")
                .append("public class " + mvcName + "ServiceImpl implements " + mvcName + "Service {}");
        FileTools.appendString(controllerFile, controllerTemplate.toString());
        FileTools.appendString(serviceFile, serviceTemplate.toString());
        FileTools.appendString(serviceImplFile, serviceImplTemplate.toString());
    }


    static Map<String, String> fileMap = new HashMap<>();
    static Map<String, String> dirMap = new HashMap<>();

    private static void setFileMap() {
        getList("G:\\DoubanMovieDataset");
        FileTools.appendString("G:\\DoubanMovieDataset\\movieTheaterFileMap.json", JSON.toJSONString(fileMap));
        FileTools.appendString("G:\\DoubanMovieDataset\\movieTheaterDirMap.json", JSON.toJSONString(dirMap));
    }

    private static void getList(String movieDir) {
        String path = movieDir;
        File file = new File(path);
        File[] tempList = file.listFiles();


        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                fileMap.put(tempList[i].getName(), tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
                dirMap.put(tempList[i].getName(), tempList[i].getPath());

                //递归：
                getList(tempList[i].getPath());
            }
        }
    }

}