package com.sleepy.blog.aop;

import com.sleepy.common.constant.HttpStatus;
import com.sleepy.common.exception.UserOperationIllegalException;
import com.sleepy.common.http.CommonDTO;
import com.sleepy.common.tools.LogTools;
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

    @Pointcut("execution(public com.sleepy.common.http.CommonDTO* com.sleepy.blog.controller.*.*(..))")
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
            result.setStatus(HttpStatus.OK);
            result.setTimeout((double) (System.currentTimeMillis() - startTime) / 1000);
        } catch (UserOperationIllegalException e) {
            result.setStatus(HttpStatus.INTERNAL_ERROR);
            result.setMessage(e.getMessage());
            log.error("用户操作异常：" + e.getMessage());
        } catch (Exception e) {
            LogTools.logExceptionInfo(e);
            result.setStatus(HttpStatus.INTERNAL_ERROR);
            result.setMessage(e.getMessage());
            log.error("serviceImpl异常：" + e.getMessage());
        } catch (Throwable throwable) {
            LogTools.logExceptionInfo(throwable);
            result.setStatus(HttpStatus.INTERNAL_ERROR);
            result.setMessage(throwable.getMessage());
            log.error("Controller拦截器异常：" + throwable.getMessage());
        }
        return result;
    }

    @After("controllerAnnotationPointCut()")
    public void doAfter() {
    }

}