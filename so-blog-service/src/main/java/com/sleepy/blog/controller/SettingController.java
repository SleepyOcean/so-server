package com.sleepy.blog.controller;

import com.sleepy.blog.entity.SettingEntity;
import com.sleepy.blog.service.SettingService;
import com.sleepy.blog.vo.SettingVO;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.common.tools.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 配置项操作 Controller
 *
 * @author gehoubao
 * @create 2019-09-04 16:51
 **/
@RestController
@CrossOrigin
@RequestMapping("/config")
public class SettingController {

    @Autowired
    SettingService settingService;

    @PostMapping("/save")
    public CommonDTO<SettingEntity> save(@RequestBody SettingVO vo) {
        return settingService.save(vo);
    }

    @PostMapping("/get")
    public CommonDTO<SettingEntity> get(@RequestBody SettingVO vo) {
        if (StringTools.isNullOrEmpty(vo.getId())) {
            return settingService.findAllSetting(vo);
        } else {
            return settingService.findSetting(vo);
        }
    }

    @GetMapping("/static")
    public CommonDTO<String> nacosConfig() {
        return settingService.getNacosConfig();
    }
}