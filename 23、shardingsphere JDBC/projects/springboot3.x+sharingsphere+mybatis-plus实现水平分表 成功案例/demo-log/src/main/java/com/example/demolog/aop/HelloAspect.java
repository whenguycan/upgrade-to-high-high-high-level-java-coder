//package com.example.demolog.aop;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class HelloAspect {
//
//    @Pointcut("execution(public * com.example.demolog.controller..*.*(..))")
//    public void pointcut() {
//    }
//
//    @Before("pointcut()")
//    public void before(JoinPoint point){
//        log.info("前置通知3");
//    }
//
//    @After("pointcut()")
//    public void after(JoinPoint point) {
//        log.info("后置通知");
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint joinPoint) {
//        log.info("环绕通知");
//        try {
//
//            // 执行方法，连接点
//            Object result = joinPoint.proceed();
//
//            return result;
//        } catch (Throwable throwable) {
//            return throwable.getMessage();
//        }
//    }
//
//    @AfterReturning("pointcut()")
//    public void afterReturning(JoinPoint point) {
//        log.info("返回通知");
//    }
//
//    @AfterThrowing(value = "pointcut()", throwing = "t")
//    public void afterThrowing(JoinPoint point, Throwable t) {
//        log.info("异常通知");
//    }
//
//}
//
//
