package com.sleepy.security.jwt;

import com.sleepy.security.entity.SoUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JWT用户
 *
 * @author gehoubao
 * @create 2020-04-03 15:02
 **/
public class JwtUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser() {
    }

    // 写一个能直接使用user创建jwtUser的构造器
    public JwtUser(SoUserEntity user) {
        id = user.getId();
        username = user.getName();
        password = user.getPassword();
        String[] roles = user.getRoles().split(",");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (int i = 0; i < roles.length; i++) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roles[i]));
        }
    }

    public JwtUser(User user) {
        username = user.getUsername();
        password = user.getPassword();
        authorities = user.getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "JwtUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
                '}';
    }

}