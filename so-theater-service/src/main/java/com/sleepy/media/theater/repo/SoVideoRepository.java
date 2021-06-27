package com.sleepy.media.theater.repo;

import com.sleepy.media.theater.entity.SoVideoEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 影片repository
 *
 * @author gehoubao
 * @create 2020-12-30 21:26
 **/
public interface SoVideoRepository extends ElasticsearchRepository<SoVideoEntity, Long> {
}
