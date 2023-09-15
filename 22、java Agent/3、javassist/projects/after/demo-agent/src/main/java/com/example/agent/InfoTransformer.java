package com.example.agent;
import javassist.*;

import java.io.*;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:42 PM
 * @Description: 类描述信息
 */
public class InfoTransformer implements ClassFileTransformer {



    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //当加载的类是在com/example/agentspringboot包中
        if(className.contains("com/example/agentspringboot")){
            System.out.println(className);
            //对所有的Controller结尾的类
            if (className.endsWith("Controller")){
                // 获取一个 class 池。
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(new LoaderClassPath(loader));

                try {
                    // 创建一个新的 class 类。classfileBuffer 就是当前class的字节码
                    CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                    //判断类是否被冻结
                    if(ctClass.isFrozen()){
                        //如果被冻结了需要先解冻
                        ctClass.defrost();
                    }
                    //插入新内容
                    CtMethod[] personFly = ctClass.getDeclaredMethods();
                    for (CtMethod ctMethod : personFly) {
                        ctMethod.insertBefore("System.out.println(\"起飞之前准备降落伞\");");
                        ctMethod.insertAfter("System.out.println(\"执行后清理\");");
                    }
                    // 返回新的字节码
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;

    }

    private byte[] loadClassData(String target) {
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
