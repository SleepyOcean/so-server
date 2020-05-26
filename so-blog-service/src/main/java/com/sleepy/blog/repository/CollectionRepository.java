package com.sleepy.blog.repository;

import com.sleepy.blog.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ghb
 * @create 2019-07-09 19:18
 **/

public interface CollectionRepository extends JpaRepository<CollectionEntity, String> {


    /**
     * 通过Name模糊查找专栏列表
     *
     * @param keyword
     * @return
     */
    List<CollectionEntity> findAllByNameLike(String keyword);
}
