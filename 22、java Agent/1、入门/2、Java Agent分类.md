## Java Agent分类

Java Agent的分类其实是根据Instrumentation的不同来决定的Java Agent的分类



#### 1、对.class文件进行修改的三个不同的时机

- Static Instrumentation：.class文件没有被加载到JVM中

  这个一般不用

  

- Load-Time Instrumentation：.class文件正在被加载到JVM中，可以对.class文件进行一次修改，对应的java agent的启动方式是在

  ```java
  -javaagent:jarpath[=options]
   
  ```

  

- Dynamic Instrumentation：.class已经被加载到JVM中，可以对.class文件进行多次修改，对应的java agent的启动方式是通过虚拟机提供的Attach机制。

  ```java
  import com.sun.tools.attach.VirtualMachine;
  
  public class VMAttach{
    
    public static void main(String[] args) throws Exception {
      
      String pid = "1234"; //需要修改的JVM的pid号
      String agentPath = "...........xxx.jar"; //指定java agent
      VirtualMachine vm = VirtualMachine.attach(pid);
      vm.loadAgent(agentPath);
      vm.detach();
      
    }
    
  }
  ```

  

![avatar](../images/MG421.jpeg)

