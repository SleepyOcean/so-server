package com.sleepy.media.theater.function;

import com.sleepy.media.theater.pojo.FakeFilePOJO;
import com.sleepy.media.theater.processor.FakeFileProcessor;
import com.sleepy.media.theater.processor.MovieFileProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.sleepy.common.tools.FileTools.constructPath;

public class PageMovieAddHelper {
    static String newMovieDirPath = "\\\\DS218plus\\0-Cinema3\\5-Classify(小小篇)\\Collection - page18.area\\2-fresh";
    static String fakeFileOutputPath = "G:\\2-实验目录\\1-FakeDir";
    static String HELPER_NAME = "Page18";

    @Test
    public void regularTest() {
//        writeFakeFile(newMovieDirPath, constructPath(fakeFileOutputPath, HELPER_NAME));

        for (int i = 1; i < 4; i++)
            moveVideoTogether(constructPath(newMovieDirPath, String.valueOf(i)), constructPath(newMovieDirPath, "regular", String.valueOf(i)));
    }

    private void moveVideoTogether(String source, String target) {
        MovieFileProcessor processor = new MovieFileProcessor();
        processor.regularPage18Video(source, target);
    }

    private void writeFakeFile(String source, String target) {
        FakeFileProcessor processor = new FakeFileProcessor();
        List<FakeFilePOJO> tree = processor.readFileTree(source);
        try {
            processor.writeFakeFile(tree, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
