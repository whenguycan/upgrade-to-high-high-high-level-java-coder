package com.example.demolog.listener;

import com.example.demolog.event.MyEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/10 4:10 PM
 * @Description: 类描述信息
 */
@Component
public class MyEventListener {

    @EventListener
    public void listener(MyEvent event) {
        System.out.println("我的监听器执行了...");
    }

    @EventListener
    public void listener2(ApplicationReadyEvent event){
        System.out.println("我的监听器执行了...222");
    }
}