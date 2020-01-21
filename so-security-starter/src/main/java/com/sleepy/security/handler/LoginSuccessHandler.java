package com.sleepy.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepy.security.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功 处理
 *
 * @author gehoubao
 * @create 2020-01-21 9:25
 **/
@Component
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler { //自定义的

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功！");

        //登录成功后设置JWT
        String token = JwtUtils.generateToken(authentication);
        httpServletResponse.addHeader("Authorization", token);
        //要做的工作就是将Authentication以json的形式返回给前端。 需要工具类ObjectMapper，Spring已自动注入。
        //设置返回类型
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Map<String, Object> tokenInfo = new HashMap<String, Object>();
        tokenInfo.put("Authorization", token);
        //将token信息写入
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(tokenInfo));
    }
}