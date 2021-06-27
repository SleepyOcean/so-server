package com.sleepy.media.theater.processor;

import com.sleepy.common.tools.HttpTools;

/**
 * TMDb数据处理器
 *
 * @author gehoubao
 * @create 2021-06-23 19:55
 **/
public class TMDbProcessor {
    private static final String API_KEY = "5831f4ac1f844ddcb8fc16a6730d0d4f";
    private static final String LANGUAGE = "zh";

    public String searchMovie(String name, int page) {
        StringBuilder url = new StringBuilder();

        String baseUrl = "https://api.themoviedb.org/";
        String apiVersion = "3";
        String path = "/search/movie";

        url.append(baseUrl).append(apiVersion).append(path);
        url.append("?").append("api_key=" + API_KEY).append("&").append("language=" + LANGUAGE);
        url.append("&").append("query=" + name).append("&").append("page=" + page);
        String result = HttpTools.doGet(url.toString());

        return result;
    }

    public void createMetaData() {

    }

    public static void main(String[] args) {
        String result = new TMDbProcessor().searchMovie("蜘蛛侠", 1);
        System.out.println(result);
    }
}