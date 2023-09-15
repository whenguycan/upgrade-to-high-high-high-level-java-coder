package com.example;

import javassist.*;

import java.util.Iterator;

/**
 * @Auther: tangwei
 * @Date: 2023/5/23 10:03 PM
 * @Description: 类描述信息
 */
public class modifyClass {

    public static void modifyClass(ClassLoader loader){
        try {

            System.out.println("当前的类加载器为：" + loader.toString());

            ClassPool classPool = new ClassPool(true);
            classPool.appendClassPath(new LoaderClassPath(loader));



            String targentClassName = "com.example.agentspringboot.utils.BitStringUtil";
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
                    "long end = System.nanoTime();" +
                    "Object result = " + method.getName() + "$agent($$);" +
                    "System.out.println(end-begin);" +
                    "return ($r)result;" +
                    "}";
            method.setBody(src);

            //将修改好的类写入到JVM
            targetClass.toClass(loader, targetClass.getClass().getProtectionDomain());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
