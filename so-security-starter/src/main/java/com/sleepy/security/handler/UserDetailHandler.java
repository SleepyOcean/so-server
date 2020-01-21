package com.sleepy.security.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户登录处理
 *
 * @author gehoubao
 * @create 2020-01-21 10:16
 **/
@Component
public class UserDetailHandler implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username,
                passwordEncoder.encode("123456"),
                // 此处的角色需要`ROLE_`前缀。Spring Security配置中的角色不需要`ROLE_` 前缀
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }
}
