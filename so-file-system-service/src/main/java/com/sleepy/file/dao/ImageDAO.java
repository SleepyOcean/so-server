package com.sleepy.file.dao;

import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.file.dto.ImageDTO;
import com.sleepy.file.vo.ImgSearchVO;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 图片ES操作类
 *
 * @author gehoubao
 * @create 2020-03-10 16:28
 **/
@Component
public class ImageDAO {
    private static final String ES_SO_IMAGE_STORE_INDEX = "so_image_store_index";

    public String findLocalPathById(String id) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("_id", id));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = getJestClient().execute(search);
        String path = "";
        if (((SearchResult) jestResult).getTotal() > 0) {
            path = jestResult.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source").getAsJsonObject().get("path").getAsString();
        }
        return path;
    }

    public List<String> findLocalPathByIds(List<String> ids) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = getJestClient().execute(search);
        List<String> path = new ArrayList<>();
        if (((SearchResult) jestResult).getTotal() > 0) {
            path = ((SearchResult) jestResult).getHits(ImageDTO.class).stream().map(o -> o.source.getPath()).collect(Collectors.toList());
        }
        return path;
    }

    public String save(ImageDTO dto) throws IOException {
        ImageDTO res = new ImageDTO();
        Index index = new Index.Builder(dto).index(ES_SO_IMAGE_STORE_INDEX).type("image").id(dto.getImageId()).build();
        JestResult jestResult = getJestClient().execute(index);

        return ((DocumentResult) jestResult).getId();
    }

    public void deleteByIds(List<String> imgIds) throws IOException {
        List<Delete> repos = new ArrayList<>();
        for (int i = 0; i < imgIds.size(); i++) {
            Delete index = new Delete.Builder(imgIds.get(i)).build();
            repos.add(index);
        }
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(ES_SO_IMAGE_STORE_INDEX)
                .defaultType("image")
                .addAction(repos).build();
        JestResult jestResult = getJestClient().execute(bulk);
    }

    public Map<String, Object> search(ImgSearchVO vo) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("uploadTime", SortOrder.DESC);
        searchSourceBuilder.from(vo.getPageStart());
        searchSourceBuilder.size(vo.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = getJestClient().execute(search);
        List<ImageDTO> data = ((SearchResult) jestResult).getHits(ImageDTO.class).stream().map(p -> p.source).collect(Collectors.toList());
        long total = ((SearchResult) jestResult).getTotal();
        return CommonTools.getCustomMap(new MapModel("list", data), new MapModel("total", total));
    }

    public List<ImageDTO> getByRangeTime(String start, String end) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("uploadTime", SortOrder.DESC);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10000);
        searchSourceBuilder.query(QueryBuilders.rangeQuery("uploadTime").from(start).to(end));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = getJestClient().execute(search);
        List<ImageDTO> data = ((SearchResult) jestResult).getHits(ImageDTO.class).stream().map(p -> p.source).collect(Collectors.toList());
        return data;
    }

    JestClient jestClient;

    @Value("${esuris}")
    String esUrl;

    private JestClient getJestClient() {
        if (null == jestClient) {
            JestClientFactory factory = new JestClientFactory();
            factory.setHttpClientConfig(new HttpClientConfig
                    .Builder(esUrl)
                    .multiThreaded(true)
                    .build());
            jestClient = factory.getObject();
        }
        return jestClient;
    }
}