package com.example.swagger.swagger2demo.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * @author Administrator
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
//    @Pointcut("execution(public * com.example.swagger.swagger2demo.controller..*.*(..))")
    @Pointcut("execution(public * com..*.controller..*.*(..))")
    private void controllerAspect() {
    }

//    @Around("webLog()")
    @Around("controllerAspect()")
    public Object arround(ProceedingJoinPoint pjp) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Signature signature = pjp.getSignature();
        // 打印请求内容
        log.info("===============请求内容===============");
        log.info("请求地址:" + request.getRequestURL().toString());
        log.info("请求方式:" + request.getMethod());
        log.info("请求类方法:" + signature);
        log.info("请求参数:" + Arrays.toString(pjp.getArgs()));
        // 打印参数名和值
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();
            for (int i = 0; i < parameterNames.length; i++) {
                log.info(String.format("%s = %s", parameterNames[i], pjp.getArgs()[i]));
            }
        }

        try {
            Object o =  pjp.proceed();
            log.info("返回内容:" + JSON.toJSONString(o));
            log.info("======================================");
            return o;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}
