package com.sleepy.security.service.impl;

import com.sleepy.security.entity.SoUserEntity;
import com.sleepy.security.pojo.SecurityUserVO;
import com.sleepy.security.repository.SecurityUserRepository;
import com.sleepy.security.service.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author SleepyOcean
 * @create 2020-03-31 18:06:55
 */
@Service
@Slf4j
public class SecurityUserServiceImpl implements SecurityUserService {
    @Autowired
    SecurityUserRepository securityUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String newUser(SecurityUserVO vo) {
        SoUserEntity entity = new SoUserEntity(vo);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        securityUserRepository.saveAndFlush(entity);
        return "new user success.";
    }
}
