package com.sleepy.blog.repository;

import com.sleepy.blog.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ghb
 * @create 2019-09-04 16:48
 **/

public interface SettingRepository extends JpaRepository<SettingEntity, String> {
}
