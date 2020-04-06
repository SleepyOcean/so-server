package com.sleepy.blog.repository;

import com.sleepy.blog.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ghb
 * @create 2019-07-09 19:18
 **/

public interface TagRepository extends JpaRepository<TagEntity, String> {

    /**
     * 模糊搜索tag
     *
     * @return
     */
    @Query(value = "select distinct tag_name from so_tag where tag_name like :tag", nativeQuery = true)
    List<String> findAllByTagLike(@Param("tag") String tag);

    /**
     * 获取所有tag名称
     *
     * @return
     */
    @Query(value = "select distinct tag_name from so_tag", nativeQuery = true)
    List<String> findAllTag();

    /**
     * 通过tags查找符合的文章ID
     *
     * @param tags
     * @return
     */
    @Query(value = "select distinct article_ids from so_tag where tag_name in (:tags)", nativeQuery = true)
    List<String> findArticleIdsByTags(@Param("tags") String[] tags);
}
