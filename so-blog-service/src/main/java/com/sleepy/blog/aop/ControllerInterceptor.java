package com.sleepy.blog.aop;

import com.sleepy.blog.dto.CommonDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * controller方法拦截器
 *
 * @author Captain
 * @create 2019-05-21 14:46
 **/
@Aspect
@Component
@Slf4j
public class ControllerInterceptor {

    @Pointcut("execution(public com.sleepy.blog.dto.CommonDTO* com.sleepy.blog.controller.*.*(..))")
    public void controllerAnnotationPointCut() {
    }

    @Before("controllerAnnotationPointCut()")
    public void doBefore(JoinPoint joinPoint) {
    }

    /**
     * 拦截Controller中常规的方法
     *
     * @param point
     * @return
     */
    @Around("controllerAnnotationPointCut() &&args(..)")
    public CommonDTO<Object> around(ProceedingJoinPoint point) {
        CommonDTO<Object> result = new CommonDTO<>();

        try {
            Long startTime = System.currentTimeMillis();
            result = (CommonDTO<Object>) point.proceed();
            result.setStatus(200);
            result.setTimeout((double) (System.currentTimeMillis() - startTime) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(503);
            result.setMessage(e.getMessage());
            log.error("serviceImpl异常：" + e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            result.setStatus(503);
            result.setMessage(throwable.getMessage());
            log.error("Controller拦截器异常：" + throwable.getMessage());
        }
        return result;
    }

    @After("controllerAnnotationPointCut()")
    public void doAfter() {
    }

}