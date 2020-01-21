package com.sleepy.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt拒绝(无权限时的处理器) 处理
 *
 * @author gehoubao
 * @create 2020-01-21 9:31
 **/
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> message = new HashMap<>();
        message.put("msg", "当前角色无权访问");
        //将token信息写入
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}
