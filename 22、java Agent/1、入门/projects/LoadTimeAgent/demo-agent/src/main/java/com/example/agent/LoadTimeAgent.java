package com.example.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:41 PM
 * @Description: 类描述信息
 */
public class LoadTimeAgent {

    public static void premain(String agentArgs, Instrumentation inst){
        System.out.println("Premain-class:" + LoadTimeAgent.class.getName());

        ClassFileTransformer transformer = new InfoTransformer();
        inst.addTransformer(transformer);//将指定的ClassFileTransformer加入到agent中
    }

}
