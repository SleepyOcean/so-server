package com.sleepy.media.theater.function;

import com.sleepy.common.tools.FileTools;
import com.sleepy.media.theater.pojo.FakeFilePOJO;
import com.sleepy.media.theater.processor.FakeFileProcessor;
import com.sleepy.media.theater.processor.MovieFileProcessor;
import me.xdrop.diffutils.DiffUtils;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author gehoubao
 * @create 2021-06-28 19:36
 **/
public class MovieFetchTest {

    static MovieFileProcessor movieFileProcessor = new MovieFileProcessor();
    static String DS_218_PATH = "G:\\MovieFetchLab\\0-WorkDir\\Target";

    @Test
    public void moveTop3() {
        String sourceDir = "\\\\DS218plus\\0-Cinema3\\5-Classify(小小篇)\\Collection - page18.area\\2-fresh\\2";
        String toDir = sourceDir + File.separator + "regular";
        movieFileProcessor.regularPage18Video(sourceDir, toDir);
    }

    @Test
    public void moveFileTest() throws IOException {
        String sourceDir = "\\\\DS218plus\\0-Cinema3\\5-Classify(小小篇)\\Collection - page18.area\\2-fresh\\2";
        String toDir = "\\\\DS218plus\\0-Cinema3\\5-Classify(小小篇)\\Collection - page18.area\\2-fresh\\2";
        movieFileProcessor.regularPage18Video(sourceDir, toDir);
    }

    @Test
    public void writeOriginalMetaJsonFile() throws IOException {
        movieFileProcessor.writeMetaJsonFile(new File("\\\\DS218plus\\0-Cinema1\\2-院线上新\\The.Tomorrow.War.2021.HDR.2160p.WEB.H265-NAISU"), false);
//        movieFileProcessor.writeMetaJsonFile(new File("\\\\DS218plus\\0-Cinema1\\2-院线上新\\War.For.The.Planet.Of.The.Apes.2017.2160p.UHD.BluRay.x265-EMERALD"));
    }

    @Test
    public void fetchMovieForLocalMovieFileAll() throws IOException {
        fetchMovieForLocalMovieFile0();
        fetchMovieForLocalMovieFile1();
        fetchMovieForLocalMovieFile11();
    }

    @Test
    public void fetchMovieForLocalMovieFile0() throws IOException {
        String sourcePath = "G:\\MovieFetchLab\\0-WorkDir\\Source\\电影整理版v3";
        String targetPath = DS_218_PATH + "0-Collection(系列收藏)";
        String cacheFetchMap = "G:\\MovieFetchLab\\0-WorkDir\\FetchResultMap（已整理）.json";
        movieFileProcessor.regularOffline(sourcePath, targetPath, cacheFetchMap, false);
    }

    @Test
    public void fetchMovieForLocalMovieFile1() throws IOException {
        String sourcePath = "G:\\MovieFetchLab\\0-WorkDir\\Source\\电影整理版v3";
        String targetPath = DS_218_PATH + "1-Movie(原盘影院)";
        String cacheFetchMap = "G:\\MovieFetchLab\\0-WorkDir\\FetchResultMap（已整理）1.json";
        movieFileProcessor.regularOffline(sourcePath, targetPath, cacheFetchMap, false);
    }

    @Test
    public void fetchMovieForLocalMovieFile11() throws IOException {
        String sourcePath = "G:\\MovieFetchLab\\0-WorkDir\\Source\\电影整理版v3";
        String targetPath = DS_218_PATH + "1-Series(电影系列)";
        String cacheFetchMap = "G:\\MovieFetchLab\\0-WorkDir\\FetchResultMap（已整理）2.json";
        movieFileProcessor.regularOffline(sourcePath, targetPath, cacheFetchMap, false);
    }

    @Test
    public void fetchMovieForLocalMovieFileTest() throws IOException {
        String sourcePath = "G:\\MovieFetchLab\\0-WorkDir\\Source\\电影整理版v2";
        String targetPath = "\\\\DS218plus\\2-Sharing云盘\\Theater";
        String cacheFetchMap = "G:\\MovieFetchLab\\0-WorkDir\\FetchResultMap（已整理）3.json";
        movieFileProcessor.regularOffline(sourcePath, targetPath, cacheFetchMap, false);
    }

    @Test
    public void renameAllToRightMovieName() throws IOException {
//        movieFileProcessor.batchRenameMovieFileName(new File("G:\\MovieFetchLab\\0-WorkDir\\Target\\targetdir"));
        movieFileProcessor.batchRenameMovieFileName(new File(DS_218_PATH + "1-Movie(原盘影院)"));
        movieFileProcessor.batchRenameMovieFileName(new File(DS_218_PATH + "1-Series(电影系列)"));
    }

    @Test
    public void createFakeFile() throws IOException {
        FakeFileProcessor processor = new FakeFileProcessor();
        List<FakeFilePOJO> tree = processor.readFileTree("\\\\DS218plus\\0-Cinema3\\5-Classify(小小篇)\\Collection - page18.area\\2-fresh");
        processor.writeFakeFile(tree, "G:\\2-实验目录\\MovieLab\\fresh");
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
        movieFileProcessor.getAllFileType(new File("G:\\MovieFetchLab\\0-WorkDir\\Target"), fileTypeSet);
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

    @Test
    public void copyFileTest() throws IOException {
        FileTools.copyFileToDir(new File("G:\\Cache\\内容.txt"), new File("G:\\Cache\\target"));
    }
}