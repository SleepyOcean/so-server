package com.sleepy.file.dao;

import com.sleepy.common.model.MapModel;
import com.sleepy.common.tools.CommonTools;
import com.sleepy.common.tools.DateTools;
import com.sleepy.file.dto.ImageDTO;
import com.sleepy.file.vo.ImgSearchVO;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
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

    @Autowired
    JestClient jestClient;

    public String findLocalPathById(String id) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("_id", id));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = jestClient.execute(search);

        String path = jestResult.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source").getAsJsonObject().get("path").getAsString();
        return path;
    }

    public String save(ImageDTO dto) throws IOException {
        ImageDTO res = new ImageDTO();
        dto.setImageId(DateTools.dateFormat(new Date(), DateTools.CUSTOM_DATETIME_PATTERN));
        Index index = new Index.Builder(dto).index(ES_SO_IMAGE_STORE_INDEX).type("image").id(dto.getImageId()).build();
        JestResult jestResult = jestClient.execute(index);

        return ((DocumentResult) jestResult).getId();
    }

    public void deleteById(String id) {
    }

    public Map<String, Object> search(ImgSearchVO vo) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("uploadTime", SortOrder.DESC);
        searchSourceBuilder.from(vo.getPageStart());
        searchSourceBuilder.size(vo.getPageSize());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ES_SO_IMAGE_STORE_INDEX).addType("image").build();
        JestResult jestResult = jestClient.execute(search);
        List<ImageDTO> data = ((SearchResult) jestResult).getHits(ImageDTO.class).stream().map(p -> p.source).collect(Collectors.toList());
        long total = ((SearchResult) jestResult).getTotal();
        return CommonTools.getCustomMap(new MapModel("list", data), new MapModel("total", total));
    }
}