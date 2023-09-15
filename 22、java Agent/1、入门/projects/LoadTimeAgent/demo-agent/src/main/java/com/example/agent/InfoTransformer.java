package com.example.agent;

//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateTime;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Formatter;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:42 PM
 * @Description: 类描述信息
 */
public class InfoTransformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //判断一些不需要处理的类，直接返回null，代表不处理。
        if (className == null) return null;
        if (className.startsWith("java")) return null;
        if (className.startsWith("javax")) return null;
        if (className.startsWith("jdk")) return null;
        if (className.startsWith("sun")) return null;
        if (className.startsWith("org")) return null;

        if(className.endsWith("HelloWorldController")){
            System.out.println("xxxxxx");

            //读取到类文件的字节码

//            ClassReader cr = new ClassReader(classfileBuffer);
//            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//            ClassVisitor cv = new MethodInfoVisitor(cw);
//
//            int parsingOptions = 0;
//            cr.accept(cv, parsingOptions);
//
//            return cw.toByteArray();

        }

        return null;
    }
}
