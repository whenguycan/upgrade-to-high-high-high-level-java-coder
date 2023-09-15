package com.example.demolog.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;

/**
 * @Auther: tangwei
 * @Date: 2023/7/12 11:02 AM
 * @Description: 类描述信息
 */
public class MyPointcut implements Pointcut {
    @Override
    public ClassFilter getClassFilter() {
        //这儿表示所有的类都进行放行，如果对类有过滤要求，可以在这儿进行过滤
        return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            //主要用这个方法
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                //注意自定义注解必须使用内置注解@Retention(RetentionPolicy.RUNTIME)，否则这儿获取不到
                MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
                //判断方法是否使用了MyAnnotation注解。
                if(myAnnotation != null){
                    return true;
                }
                return false;
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                return false;
            }
        };
    }
}
