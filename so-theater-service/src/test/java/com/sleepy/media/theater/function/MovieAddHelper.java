package com.sleepy.media.theater.function;

import com.sleepy.media.theater.pojo.FakeFilePOJO;
import com.sleepy.media.theater.processor.FakeFileProcessor;
import com.sleepy.media.theater.processor.MovieFileProcessor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 电影添加助手类
 * <p>
 * step1. 创建 Fake file
 * step2. 使用 TMM 匹配电影
 * step3. 写入原始文件的信息到 MetaJsonFile.json，并重命名电影文件名，并复制已匹配电影文件到目标电影目录中
 *
 * @author gehoubao
 * @create 2021-07-31 9:46
 **/
public class MovieAddHelper {

    static MovieFileProcessor movieFileProcessor = new MovieFileProcessor();
    static String newMovieDirPath = "G:\\3-院线上新";
    static String fakeFileOutputPath = "G:\\2-实验目录\\1-FakeDir";
    static String matchFileOutputPath = "G:\\2-实验目录\\2-MatchDir";


    /**
     * step1. 创建 Fake file
     *
     * @throws IOException
     */
    @Test
    public void step1() throws IOException {
        // write fake file
        FakeFileProcessor fakeFileProcessor = new FakeFileProcessor();
        List<FakeFilePOJO> tree = fakeFileProcessor.readFileTree(newMovieDirPath);
        fakeFileProcessor.writeFakeFile(tree, fakeFileOutputPath);

        // rename movie dir name for TMM search
        File fakeDir = new File(fakeFileOutputPath);
        boolean check = fakeDir.isDirectory() && fakeDir.list().length > 0;
        if (!check) return;
        for (File file : fakeDir.listFiles()) {
            movieFileProcessor.renameMovieDirForTmmSearch(file);
        }
    }

    /**
     * step3. 写入原始文件的信息到 MetaJsonFile.json，并重命名电影文件名，并复制已匹配电影文件到目标电影目录中
     */
    @Test
    public void step3() {
        File movieDir = new File(newMovieDirPath);
        boolean check = movieDir.isDirectory() && movieDir.list().length > 0;
        if (!check) return;

        // write meta to json file
        for (File file : movieDir.listFiles()) {
            movieFileProcessor.writeMetaJsonFile(file, true);
        }

        movieFileProcessor.regularOffline(matchFileOutputPath, newMovieDirPath, "", true);
    }
}