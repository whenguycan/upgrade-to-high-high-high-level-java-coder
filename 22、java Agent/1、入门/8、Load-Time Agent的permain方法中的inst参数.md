## Load-Time: inst参数



当agent项目启动的时候，会默认执行InstrumentationImpl类，找到然后利用反射执行premain()方法，将自己做为参数，参数名为inst，传入到premain方法中。

那么，inst参数，实际传入的是Instrumentation接口的一个实现类即InstrumentationImpl类。



如何证明，inst是InstrumentationImpl类

```java
package lsieun.agent;

import java.lang.instrument.Instrumentation;

public class LoadTimeAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Premain-Class: " + LoadTimeAgent.class.getName());
        System.out.println("agentArgs: " + agentArgs);
        System.out.println("Instrumentation Class: " + inst.getClass().getName());
      
      	Exception ex = new Exception("error");
      	ex.printStackTrace(System.out);//手动抛出异常，看看堆栈是不是InstrumentationImpl类。
    }
}
```

