package com.example.agent;

import java.lang.instrument.Instrumentation;

/**
 * @Auther: tangwei
 * @Date: 2023/5/9 1:42 PM
 * @Description: 类描述信息
 */
public class DynamicAgent {

    public static void agentmain(String AgentArgs, Instrumentation inst){
        InfoTransformer infoTransformer = new InfoTransformer();

        try{
            inst.addTransformer(infoTransformer);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            //下面一行 一旦带了，就无法实现探针了
            //inst.removeTransformer(infoTransformer);
        }
    }
}
