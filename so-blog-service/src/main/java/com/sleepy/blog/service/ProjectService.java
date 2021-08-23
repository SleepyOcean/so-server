package com.sleepy.blog.service;

import com.sleepy.blog.entity.ProjectEntity;
import com.sleepy.blog.vo.ProjectVO;
import com.sleepy.common.http.CommonDTO;

/**
 * 项目管理服务
 *
 * @author ghb
 * @create 2019-06-26 11:09
 **/

public interface ProjectService {
    /**
     * 保存项目详情
     *
     * @param vo
     * @return
     */
    CommonDTO<String> saveProject(ProjectVO vo);

    /**
     * 获取项目详情
     *
     * @param vo
     * @return
     */
    CommonDTO<ProjectEntity> getProject(ProjectVO vo);

    /**
     * 删除项目详情
     *
     * @param vo
     * @return
     */
    CommonDTO<ProjectEntity> deleteProject(ProjectVO vo);

    /**
     * 更新完成状态
     *
     * @param vo
     * @return
     */
    CommonDTO<String> updateStatus(ProjectVO vo);

}
