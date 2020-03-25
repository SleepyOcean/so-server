package com.sleepy.blog.repository;

import com.sleepy.blog.dto.ChartOfBarDTO;
import com.sleepy.blog.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author ghb
 * @create 2019-04-19 15:19
 **/
@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {
    /**
     * 通过title查找记录
     *
     * @param title
     * @return
     */
    List<ArticleEntity> findAllByTitle(String title);

    /**
     * 通过title模糊查找记录
     *
     * @param title
     * @return
     */
    Page<ArticleEntity> findAllByTitleLike(String title, Pageable pageable);

    /**
     * 通过ids查找文章
     *
     * @param ids
     * @return
     */
    @Query(value = "SELECT * FROM so_article WHERE id IN (:ids) LIMIT 6", nativeQuery = true)
    List<ArticleEntity> findAllByIds(@Param("ids") List<String> ids);

    /**
     * 根据 hotRate 倒序获取热度文章
     *
     * @return
     */
    @Query(value = "SELECT * FROM so_article ORDER BY hot_rate DESC LIMIT 10", nativeQuery = true)
    List<ArticleEntity> findHotArticleInfo();

    /**
     * 统计指定时间段内的文章数量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT new com.sleepy.blog.dto.ChartOfBarDTO(a.createTime, COUNT(a.createTime)) " +
            "FROM ArticleEntity as a " +
            "WHERE a.createTime BETWEEN :startTime AND :endTime " +
            "GROUP BY a.createTime")
    List<ChartOfBarDTO> getArticleStatistic(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
