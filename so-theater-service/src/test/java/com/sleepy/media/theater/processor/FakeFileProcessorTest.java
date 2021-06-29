package com.sleepy.media.theater.processor;

import com.sleepy.media.theater.pojo.FakeFilePOJO;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * 假文件生产测试类
 *
 * @author gehoubao
 * @create 2021-06-29 19:08
 **/
public class FakeFileProcessorTest {
    FakeFileProcessor processor = new FakeFileProcessor();

    @Test
    public void produceFakeFile() throws IOException {
        List<FakeFilePOJO> tree = processor.readFileTree("\\\\DS218plus\\0-Cinema1");

        processor.writeFakeFile(tree, "G:\\FakeFileDir\\0-Cinema1");
        System.out.println(tree);
    }
}