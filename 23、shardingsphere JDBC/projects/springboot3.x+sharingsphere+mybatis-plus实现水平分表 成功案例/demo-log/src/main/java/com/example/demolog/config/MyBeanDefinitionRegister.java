package com.example.demolog.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/13 1:12 PM
 * @Description: 类描述信息
 */
public class MyBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    /** 方法说明
     * 可以认为是beanDefinition的注册中心。所有的beanDefinition都会被注册到这里面去。
     * 有如下的功能：
     *      1. 以Map<String, BeanDefinition>的形式注册bean
     *      2. 根据beanName 删除和获取 beanDefiniation
     *      3. 得到持有的beanDefiniation的数目
     *      4. 根据beanName 判断是否包含beanDefiniation
     */

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//        registry.registerBeanDefinition("duck", new RootBeanDefinition(Duck.class));//将Duck这个类包装成一个RootBeanDefinition，并最终生成bean的时候以duck命名bean
    }
}
