package com.example.demolog.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/13 4:35 PM
 * @Description: 类描述信息
 */
@Component
public class MyAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //applicationContext 就是ioc容器

        System.out.println("ioc为" + applicationContext.toString());

    }
}
