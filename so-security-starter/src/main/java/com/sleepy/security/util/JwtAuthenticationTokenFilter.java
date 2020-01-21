package com.sleepy.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gehoubao
 * @create 2020-01-21 9:33
 **/
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //获取JWT
        String authHeader = request.getHeader("Authorization");
        logger.info("--------->" + authHeader);
        if (authHeader != null) {
            JwtUtils.tokenParser(authHeader);
        }
        filterChain.doFilter(request, response);
    }
}
