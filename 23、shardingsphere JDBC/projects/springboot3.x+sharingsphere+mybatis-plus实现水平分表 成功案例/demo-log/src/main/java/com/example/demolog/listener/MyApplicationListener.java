package com.example.demolog.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Auther: tangwei
 * @Date: 2023/7/7 3:47 PM
 * @Description: 类描述信息
 */
public class MyApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("获取到触发事件" + event.getClass().getName());
    }
}
