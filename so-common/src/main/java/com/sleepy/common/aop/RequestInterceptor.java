package com.sleepy.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.sleepy.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http请求拦截器
 *
 * @author gehoubao
 * @create 2019-12-30 10:18
 **/
@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = StringUtil.getRandomUuid("");
        log.info(String.format("Request[%s] URL:[%s], Protocol:[%s], Params:%s", uuid, request.getRequestURL(), request.getProtocol(), JSONObject.toJSONString(request.getParameterMap())));
        request.setAttribute("startTime", System.currentTimeMillis());
        request.setAttribute("uuid", uuid);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long timeout = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
        String uuid = (String) request.getAttribute("uuid");
        log.info(String.format("Response[%s] [%s] Timeout:[%s ms], ResponseStatus:[%s], ResponseBodySize:[%s], Error:[%s]", uuid, request.getRequestURI(), timeout, response.getStatus(), response.getBufferSize(), ex != null ? ex.getMessage() : "null"));
        super.afterCompletion(request, response, handler, ex);
    }
}