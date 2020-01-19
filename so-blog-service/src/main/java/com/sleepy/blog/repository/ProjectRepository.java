package com.sleepy.blog.repository;

import com.sleepy.blog.entity.ProjectEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ghb
 * @create 2019-06-26 11:24
 **/

public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {
    /**
     * 通过projectName模糊查找记录
     *
     * @param name
     * @param sort
     * @return
     */
    List<ProjectEntity> findAllByProjectNameLike(String name, Sort sort);

    /**
     * 通过完成状态查找记录
     *
     * @param status
     * @param sort
     * @return
     */
    List<ProjectEntity> findAllByStatus(Integer status, Sort sort);


    /**
     * 通过计划完成时间查找记录
     *
     * @param date
     * @return
     */
    List<ProjectEntity> findAllByDeadline(String date);


    /**
     * 通过moduleName模糊查找记录
     *
     * @param name
     * @param sort
     * @return
     */
    List<ProjectEntity> findAllByModuleNameLike(String name, Sort sort);

    /**
     * 更新完成状态
     *
     * @param id
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update so_project sp set sp.status = :status where sp.id = :id", nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") Integer status);
}
