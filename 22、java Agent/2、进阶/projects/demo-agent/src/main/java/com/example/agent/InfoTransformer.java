package com.example.agent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
    private String internalClassName;

    public InfoTransformer(String clazz){
        this.internalClassName = clazz.replace(".", "/");
    }


    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (className.equals(this.internalClassName)){
            System.out.println("className:" + className);
            return loadClassData("/Users/tangwei/javaProject/demo-agent/Hello.class");
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
