package com.example.demolog.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/12 11:03 AM
 * @Description: 类描述信息
 */
@Component
public class MyAdvisor extends AbstractPointcutAdvisor {


    @Override
    public Advice getAdvice() {
        return new MyAdvice();
    }

    @Override
    public boolean isPerInstance() {
        return false;
    }

    @Override
    public Pointcut getPointcut() {
        return new MyPointcut();
    }
}
