package com.sleepy.security.handler;

import com.sleepy.security.entity.SoUserEntity;
import com.sleepy.security.repository.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录处理
 *
 * @author gehoubao
 * @create 2020-01-21 10:16
 **/
@Service
public class UserDetailHandler implements UserDetailsService {

    @Autowired
    SecurityUserRepository securityUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SoUserEntity entity = securityUserRepository.findByName(username);
        if (entity == null) {
            throw new UsernameNotFoundException(username + "用户不存在!");
        }

        String[] roles = entity.getRoles().split(",");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (int i = 0; i < roles.length; i++) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roles[i]));
        }
        return new User(username, entity.getPassword(), authorities);
    }
}
