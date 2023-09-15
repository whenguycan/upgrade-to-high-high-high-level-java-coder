package com.example.demolog.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Auther: tangwei
 * @Date: 2023/7/10 4:09 PM
 * @Description: 类描述信息
 */
public class MyEvent extends ApplicationEvent {

    private String msg;

    //构造函数，一个参数为object
    public MyEvent(Object source) {

        //实现父类的构造函数
        super(source);
        System.out.println("事件执行了...");

        System.out.println(source);
        this.msg = msg;
    }

}
