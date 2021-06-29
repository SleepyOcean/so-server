package com.sleepy.media.theater.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sleepy.common.tools.HttpTools;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * TMDb数据处理器
 *
 * @author gehoubao
 * @create 2021-06-23 19:55
 **/
public class TMDbProcessor {
    private static final String API_KEY = "5831f4ac1f844ddcb8fc16a6730d0d4f";
    private static final String LANGUAGE = "zh";
    private RestTemplate restTemplate = new RestTemplate();

    public JSONArray searchMovie(String name, int page) {
        StringBuilder url = new StringBuilder();

        String baseUrl = "https://api.themoviedb.org/";
        String apiVersion = "3";
        String path = "/search/movie";

        Map<String, Object> params = new HashMap<>();
        params.put("api_key", API_KEY);
        params.put("language", LANGUAGE);
        params.put("query", name);
        params.put("page", page);

        String result = null;
        try {
            result = HttpTools.doGet(baseUrl + apiVersion + path, params);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray list = JSON.parseObject(result).getJSONArray("results");
        return list;
    }

    public void createMetaData() {

    }

}