package com.sleepy.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.sleepy.security.entity.SoRoleEntity;
import com.sleepy.security.pojo.SecurityRoleVO;
import com.sleepy.security.repository.SecurityRoleRepository;
import com.sleepy.security.service.SecurityRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:16:54
 */
@Service
@Slf4j
public class SecurityRoleServiceImpl implements SecurityRoleService {
    @Autowired
    SecurityRoleRepository securityRoleRepository;

    @Override
    public String newRole(SecurityRoleVO vo) {
        SoRoleEntity entity = new SoRoleEntity(vo);
        securityRoleRepository.saveAndFlush(entity);
        return "new role success.";
    }

    @Override
    public String getRole(SecurityRoleVO vo) {
        List<SoRoleEntity> roles = securityRoleRepository.findAll();
        return JSON.toJSON(roles).toString();
    }
}
