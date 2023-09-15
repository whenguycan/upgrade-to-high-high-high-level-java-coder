package com.example.demolog.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @Auther: tangwei
 * @Date: 2023/7/12 11:03 AM
 * @Description: 类描述信息
 */
public class MyAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("前置通知2");
    }
}
