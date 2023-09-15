## java Agent的启动方式



#### 1、从命令行（Command line）启动Java Agent

> 对应Load-Time Instrumentation

在使用`java`命令时，使用`-javaagent`选项：

```shell
java -javaagent:jarpath[=options]
```

使用示例：看skywalking的客户端探针如何启动的就行了



#### 2、通过虚拟机提供的Attach机制来启动Java Agent

> 对应Dynamic Instrumentation

```java
import com.sun.tools.attach.VirtualMachine;

public class VMAttach {
  
  public static void main(String[] args) throws Exception{
    
    String pid = "";//要操作的java进程id
    String agentPath = "......jar";//当前需要被附加的java agent的jar包
    VirtualMachine vm = VirtualMachine.attach(pid);
    vm.loadAgent(agentPath);//加载java agent
    vm.detach();//关闭连接
    
  }
  
}
```

