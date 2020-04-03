package com.sleepy.security.repository;

import com.sleepy.security.entity.SoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ghb
 * @create 2019-04-19 15:19
 **/
public interface SecurityUserRepository extends JpaRepository<SoUserEntity, Long> {
    /**
     * 通过名称获取用户
     *
     * @param name
     * @return
     */
    SoUserEntity findByName(String name);
}
