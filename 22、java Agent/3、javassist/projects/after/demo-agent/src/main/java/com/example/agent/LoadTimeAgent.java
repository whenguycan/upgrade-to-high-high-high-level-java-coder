package com.example.agent;


import com.example.Hello;
import com.example.IHhh;
import javassist.*;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.lang.instrument.Instrumentation;
import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:41 PM
 * @Description: 类描述信息
 */
public class LoadTimeAgent {

    public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, IOException, NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
        System.out.println("Can-Redifined-Class:" + inst.isRedefineClassesSupported());
        System.out.println("Can-Retransform-Class:" + inst.isRetransformClassesSupported());
        System.out.println("Can-Set-Native-Method-Prefix" + inst.isNativeMethodPrefixSupported());

        System.out.println("Premain-class:" + LoadTimeAgent.class.getName());


        InfoTransformer infoTransformer = new InfoTransformer();
        inst.addTransformer(infoTransformer);





//        ClassPool classPool = new ClassPool();
//        classPool.insertClassPath(new LoaderClassPath(LoadTimeAgent.class.getClassLoader()));
//        String targentClassName = "com.example.utils.BitStringUtil";
////        根据类名获取到要修改的class
//        CtClass targetClass = classPool.get(targentClassName);
//        //根据方法名获取到要修改的方法名
//        CtMethod method = targetClass.getDeclaredMethod("addString");
//
//        //拷贝方法到代理方法
//        // 第一个参数，是被拷贝方法
//        // 第二个参数，新方法的方法名
//        // 第三个参数，执行新方法归属哪个类
//        // 第四个参数，
//        CtMethod newMethod = CtNewMethod.copy(method, method.getName() + "$agent", targetClass, null);
//        //将新方法放到类中
//        targetClass.addMethod(newMethod);
//
//        String src = "{" +
//                "long begin = System.nanoTime();" +
//                "long begin = System.nanoTime();" +
//                "Object result = " + method.getName() + "$agent($$);" +
//                "System.out.println(end-begin);" +
//                "return ($r)result;" +
//                "}";
//        method.setBody(src);
//
//        //将修改好的类写入到JVM
//        targetClass.toClass();











//        //插入类路径，通过这儿加进去的类路劲，才能搜索到我们需要修改的类
//        System.out.println("添加的classLoader：" + LoadTimeAgent.class.getClassLoader().toString());
//        classPool.insertClassPath(new LoaderClassPath(LoadTimeAgent.class.getClassLoader()));
//        //classPool.appendClassPath() //也可以使用appendClassPath()方法
//
//        //构建一个新的对象
//        CtClass targetClass = classPool.makeClass("com.example.Hhhh");
//
//        //表名该对象实现一个接口
//        targetClass.addInterface(classPool.get(IHhh.class.getName()));
//
//        CtClass reternType = classPool.get(void.class.getName());
//        String mname = "sayHello";
//        CtClass[] parameters = new CtClass[]{classPool.get(String.class.getName())};
//        //构建一个方法
//        // 第一个参数为：方法返回值
//        // 第二个参数为：方法名称
//        // 第三个参数为：方法参数
//        // 第四个参数为：方法需要加入到哪个类中
//        CtMethod method = new CtMethod(reternType, mname, parameters, targetClass);
//
//        String src = "{ " +
//
//                "System.out.println($1);" +
//
//                "System.out.println($args);" +
//
//                "System.out.println($type);" +
//
//                "System.out.println($class);" +
//
//                "System.out.println(\"hello \" + $1); " +
//
//                "}";
//        //设置方法的方法体
//        // 第一个参数： 是方法的源代码
//        method.setBody(src);
//
//        //将方法写入到对应的类中
//        targetClass.addMethod(method);
//
//        //写入到JVM中
//        Class cla = targetClass.toClass();
//
//        //从JVM中获取到刚刚写入的类，并调用方法
//        IHhh hh = (IHhh) cla.newInstance();
//        hh.sayHello("可爱");


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

