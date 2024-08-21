package com.example.demo.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // 記錄方法執行前的日誌
    @Before("execution(* com.example.demo..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String requestUrl = (requestAttributes != null) ? (String) requestAttributes.getAttribute("javax.servlet.request.request_uri", RequestAttributes.SCOPE_REQUEST) : "unknown";
        String requestMethod = (requestAttributes != null) ? (String) requestAttributes.getAttribute("javax.servlet.request.method", RequestAttributes.SCOPE_REQUEST) : "unknown";
        Object requestParams = (requestAttributes != null) ? requestAttributes.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables", RequestAttributes.SCOPE_REQUEST) : "unknown";

        logger.info("Executing method: {}. Request URL: {}. Method: {}. Params: {}.", joinPoint.getSignature().toShortString(), requestUrl, requestMethod, requestParams);
    }

    // 記錄方法執行後的日誌
    @After("execution(* com.example.demo..*(..))")
    public void logAfterMethod(JoinPoint joinPoint) {
        logger.info("Method executed: {}", joinPoint.getSignature().toShortString());
    }
}
