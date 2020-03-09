package com.sleepy.file.repository;

import com.sleepy.file.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ghb
 * @create 2019-10-31 10:07
 **/

public interface ImageRepository extends JpaRepository<ImageEntity, String> {

    /**
     * 通过id查找图片本地相对路径
     *
     * @param id
     * @return
     */
    @Query(value = "SELECT path FROM so_img WHERE id = :id", nativeQuery = true)
    String findLocalPathById(@Param("id") String id);
}
