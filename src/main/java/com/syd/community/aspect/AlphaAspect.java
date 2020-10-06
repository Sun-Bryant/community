package com.syd.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.syd.community.service.*.*(..))")
    public void pointcut() {

    }

    // 连接点之前
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    // 连接点之后
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    // 返回值以后
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    // 抛异常之后
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    // 环绕   在之前处理，也在之后处理
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
