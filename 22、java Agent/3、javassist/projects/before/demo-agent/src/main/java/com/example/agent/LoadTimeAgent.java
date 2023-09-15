package com.example.agent;


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

    public static void premain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, IOException {
        System.out.println("Can-Redifined-Class:" + inst.isRedefineClassesSupported());
        System.out.println("Can-Retransform-Class:" + inst.isRetransformClassesSupported());
        System.out.println("Can-Set-Native-Method-Prefix" + inst.isNativeMethodPrefixSupported());

        System.out.println("Premain-class:" + LoadTimeAgent.class.getName());


        // 判断一个module是否可以读取另一个module
        Module baseModule = Object.class.getModule();
        Module instrumentModule = Instrumentation.class.getModule();
        boolean canRead = baseModule.canRead(instrumentModule);

        // 第三步，使用inst：修改module权限
        if (!canRead && inst.isModifiableModule(baseModule)) {
            Set<Module> extraReads = Set.of(instrumentModule);
            inst.redefineModule(baseModule, extraReads, Map.of(), Map.of(), Set.of(), Map.of());
        }

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

