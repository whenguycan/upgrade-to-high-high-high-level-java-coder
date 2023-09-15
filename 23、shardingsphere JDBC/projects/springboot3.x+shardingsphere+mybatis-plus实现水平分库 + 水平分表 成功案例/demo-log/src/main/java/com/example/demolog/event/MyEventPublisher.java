package com.example.demolog.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Auther: tangwei
 * @Date: 2023/7/10 4:11 PM
 * @Description: 类描述信息
 */
@Scope("prototype")
@Component
public class MyEventPublisher {

    @Autowired
    private ApplicationContext applicationEventPublisher;

    public void pushMyEvent(){
        applicationEventPublisher.publishEvent(new MyEvent("tangwei"));
    }

}
