package com.sleepy.media.theater.function;

import com.sleepy.media.theater.processor.MovieFileProcessor;
import me.xdrop.diffutils.DiffUtils;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

/**
 * @author gehoubao
 * @create 2021-06-28 19:36
 **/
public class MovieFetchTest {

    static MovieFileProcessor movieFileProcessor = new MovieFileProcessor();

    @Test
    public void fetchMovieForLocalMovieFile() {
        String sourcePath = "E:\\Z-Cache\\1-CodeCache\\FakeFileDir\\0-Source\\0-Collection";
        String targetPath = "E:\\Z-Cache\\1-CodeCache\\FakeFileDir\\0-Cinema1\\0-Collection(系列收藏)";
        movieFileProcessor.regularOffline(sourcePath, targetPath);
    }

    @Test
    public void renameAbnormalFile() {
        String path = "E:\\Z-Cache\\1-CodeCache\\FakeFileDir\\0-Cinema1\\0-Collection(系列收藏)\\H-詹姆斯邦德007系列";
    }

    @Test
    public void findMultiVideoFile() {
        Set<String> filePath = new HashSet<>();
        movieFileProcessor.findMultiVideoFile(new File("G:\\MovieFetchLab\\0-Cinema1"), filePath);
        List<String> list = new LinkedList<String>(filePath);
        Collections.sort(list);
        System.out.printf(filePath.toString());
    }

    @Test
    public void getAllVideoFile() {
        List<String> fileSet = new ArrayList<>();
        movieFileProcessor.getAllVideoFile(new File("E:\\Z-Cache\\1-CodeCache\\FakeFileDir\\0-Cinema1\\0-Collection(系列收藏)"), fileSet);
        System.out.println(fileSet.toString());
    }

    @Test
    public void getAllFileType() {
        Map<String, List<String>> fileTypeSet = new HashMap<>(1024);
        movieFileProcessor.getAllFileType(new File("G:\\MovieFetchLab\\0-Cinema1"), fileTypeSet);
        System.out.println(fileTypeSet.toString());
    }

    @Test
    public void fuzzySearchTest() {
        System.out.println("1 " + FuzzySearch.ratio("admin", "admin"));
        System.out.println("2 " + FuzzySearch.partialRatio("ADMIN", "admin"));
        System.out.println("3 " + FuzzySearch.tokenSetPartialRatio("test", "test1"));
        System.out.println("4 " + FuzzySearch.weightedRatio("你是", "你是我"));
        System.out.println("5 " + FuzzySearch.tokenSortRatio("你是", "你是W"));
        System.out.println("6 " + FuzzySearch.tokenSetRatio("你是", "你是o"));
        System.out.println(DiffUtils.getRatio("你是", "你是我"));
        System.out.println(DiffUtils.levEditDistance("你是", "你是我", 1));
        System.out.println(DiffUtils.getMatchingBlocks("你是", "你是我"));
        System.out.println(DiffUtils.getEditOps("你是", "你是我"));
    }
}