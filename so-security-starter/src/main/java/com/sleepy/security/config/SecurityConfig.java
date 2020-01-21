package com.sleepy.security.config;

import com.sleepy.security.handler.JwtAccessDeniedHandler;
import com.sleepy.security.handler.LoginFailureHandler;
import com.sleepy.security.handler.LoginSuccessHandler;
import com.sleepy.security.util.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 配置
 *
 * @author gehoubao
 * @create 2020-01-21 9:21
 **/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /** JWT拦截器*/
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        /** 将JWT拦截器添加到UsernamePasswordAuthenticationFilter之前*/
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin()
                .loginPage("/auth/loginInfo")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler);
        http.authorizeRequests()
                // 此处的角色不需要`ROLE_` 前缀,实现UserDetailsService设置角色时需要`ROLE_` 前缀
                .antMatchers("/sys/**", "/resource/img/save", "/resource/img/delete").hasRole("ADMIN")
                .antMatchers("/login", "/loginInfo", "/logoutSuccess", "/resource/img/**", "/**")
                .permitAll()
                .anyRequest()
                .authenticated();
        // 访问 /logout 表示用户注销，并清空session
        http.logout().logoutSuccessUrl("/auth/logoutSuccess");
        // 关闭csrf
        http.csrf().disable();
        http.cors();
        // AccessDeniedHandler处理器 拒绝访问处理器
        http.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler);
    }

    /**
     * 密码加盐加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //Spring自带的每次会随机生成盐值，即使密码相同，加密后也不同
        return new BCryptPasswordEncoder();
    }
}
