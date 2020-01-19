package com.sleepy.blog.controller;

import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.entity.ProjectEntity;
import com.sleepy.blog.service.ProjectService;
import com.sleepy.blog.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目管理控制器
 *
 * @author gehoubao
 * @create 2019-06-26 11:05
 **/
@RestController
@CrossOrigin
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @PostMapping("/save")
    public CommonDTO<String> save(@RequestBody ProjectVO vo) {
        return projectService.saveProject(vo);
    }

    @PostMapping("/get")
    public CommonDTO<ProjectEntity> get(@RequestBody ProjectVO vo) {
        return projectService.getProject(vo);
    }

    @PostMapping("/delete")
    public CommonDTO<ProjectEntity> delete(@RequestBody ProjectVO vo) {
        return projectService.deleteProject(vo);
    }

    @PostMapping("/update")
    public CommonDTO<String> update(@RequestBody ProjectVO vo) {
        return projectService.updateStatus(vo);
    }
}