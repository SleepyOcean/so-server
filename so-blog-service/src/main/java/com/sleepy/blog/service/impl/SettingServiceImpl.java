package com.sleepy.blog.service.impl;

import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.entity.SettingEntity;
import com.sleepy.blog.repository.SettingRepository;
import com.sleepy.blog.service.SettingService;
import com.sleepy.blog.vo.SettingVO;
import com.sleepy.common.tools.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置项 ServiceImpl
 *
 * @author gehoubao
 * @create 2019-09-04 16:54
 **/
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    SettingRepository settingRepository;

    @Override
    public CommonDTO<SettingEntity> save(SettingVO vo) {
        CommonDTO<SettingEntity> result = new CommonDTO<>();
        SettingEntity entity = new SettingEntity();
        if (!StringTools.isNullOrEmpty(vo.getId())) {
            entity.setId(vo.getId());
        }
        entity.setConfigKey(vo.getKey());
        entity.setConfigValue(vo.getValue());
        entity.setConfigInfo(vo.getInfo());
        result.setResult(settingRepository.saveAndFlush(entity));
        return result;
    }

    @Override
    public CommonDTO<SettingEntity> findAllSetting(SettingVO vo) {
        CommonDTO<SettingEntity> result = new CommonDTO<>();
        List<SettingEntity> set = settingRepository.findAll();
        result.setResultList(set);
        return result;
    }

    @Override
    public CommonDTO<SettingEntity> findSetting(SettingVO vo) {
        CommonDTO<SettingEntity> result = new CommonDTO<>();
        // TODO 获取指定条件配置项
        return result;
    }
}