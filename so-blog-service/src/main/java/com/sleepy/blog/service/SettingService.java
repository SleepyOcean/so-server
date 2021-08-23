package com.sleepy.blog.service;

import com.sleepy.blog.entity.SettingEntity;
import com.sleepy.blog.vo.SettingVO;
import com.sleepy.common.http.CommonDTO;

/**
 * 配置项Service
 *
 * @author ghb
 * @create 2019-09-04 16:54
 **/
public interface SettingService {

    /**
     * 保存配置项
     *
     * @param vo
     * @return
     */
    CommonDTO<SettingEntity> save(SettingVO vo);

    /**
     * 获取所有配置项
     *
     * @param vo
     * @return
     */
    CommonDTO<SettingEntity> findAllSetting(SettingVO vo);

    /**
     * 获取配置项
     *
     * @param vo
     * @return
     */
    CommonDTO<SettingEntity> findSetting(SettingVO vo);

    CommonDTO<String> getNacosConfig();
}
