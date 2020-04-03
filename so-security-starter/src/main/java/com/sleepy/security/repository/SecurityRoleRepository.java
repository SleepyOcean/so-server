package com.sleepy.security.repository;

import com.sleepy.security.entity.SoRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ghb
 * @create 2019-04-19 15:19
 **/
public interface SecurityRoleRepository extends JpaRepository<SoRoleEntity, Long> {
}
