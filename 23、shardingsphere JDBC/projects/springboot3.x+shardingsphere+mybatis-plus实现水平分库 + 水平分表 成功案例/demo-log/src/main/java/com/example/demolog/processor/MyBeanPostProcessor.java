package com.example.demolog.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/12 10:09 AM
 * @Description: 类描述信息
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("indexController")){
            System.out.println(bean.getClass());
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (beanName.equals("indexController")){
            System.out.println(bean.getClass());
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
