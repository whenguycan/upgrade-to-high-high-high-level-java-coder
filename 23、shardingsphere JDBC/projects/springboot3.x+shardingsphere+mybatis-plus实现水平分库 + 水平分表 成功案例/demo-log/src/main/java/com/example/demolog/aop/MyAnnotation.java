package com.example.demolog.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
//使用该注解设置为RetentionPolicy.RUNTIME
//才能在pointcut中通过反射访问到
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAnnotation {
}
