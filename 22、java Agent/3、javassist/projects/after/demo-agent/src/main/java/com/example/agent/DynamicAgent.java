package com.example.agent;

import javassist.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: tangwei
 * @Date: 2023/5/9 1:42 PM
 * @Description: 类描述信息
 */
public class DynamicAgent {

    public static void agentmain(String agentArgs, Instrumentation inst) throws NotFoundException, CannotCompileException {
        System.out.println("Can-Redifined-Class:" + inst.isRedefineClassesSupported());
        System.out.println("Can-Retransform-Class:" + inst.isRetransformClassesSupported());
        System.out.println("Can-Set-Native-Method-Prefix" + inst.isNativeMethodPrefixSupported());

        System.out.println("Agent-class:" + DynamicAgent.class.getName());



        //创建一个ClassPool对象然后追加系统搜索路径到其中。
        ClassPool classPool = new ClassPool(true);
        classPool.insertClassPath(new LoaderClassPath(LoadTimeAgent.class.getClassLoader()));

        String targentClassName = "com.example.utils.BitStringUtil";
        //根据类名获取到要修改的class
        CtClass targetClass = classPool.get(targentClassName);

        //根据方法名获取到要修改的方法名
        CtMethod method = targetClass.getDeclaredMethod("addString");

        //拷贝方法到代理方法
        // 第一个参数，是被拷贝方法
        // 第二个参数，新方法的方法名
        // 第三个参数，执行新方法归属哪个类
        // 第四个参数，
        CtMethod newMethod = CtNewMethod.copy(method, method.getName() + "$agent", targetClass, null);
        //将新方法放到类中
        targetClass.addMethod(newMethod);

        String src = "{" +
                "long begin = System.nanoTime();" +
                "long begin = System.nanoTime();" +
                "Object result = " + method.getName() + "$agent($$);" +
                "System.out.println(end-begin);" +
                "return ($r)result;" +
                "}";
        method.setBody(src);

        //将修改好的类写入到JVM
        targetClass.toClass();


//        String classname = "com.example.Hello";
//
//        InfoTransformer infoTransformer = new InfoTransformer(classname);
//        inst.addTransformer(infoTransformer, true);
//
//        try {
//            Class<?> clazz = Class.forName(classname);
//            if (inst.isModifiableClass(clazz)) {
//
//                inst.retransformClasses(clazz);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static byte[] loadClassData(String target) {
        InputStream is = null;
        byte[] bytes = null;
        ByteArrayOutputStream os = null;
        int len;
        try {
            is = new FileInputStream(new File(target));
            os = new ByteArrayOutputStream();
            while (-1 != (len = is.read())) {
                os.write(len);
            }
            bytes = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

}