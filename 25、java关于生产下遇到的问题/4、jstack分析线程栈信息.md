## jstack分析线程栈信息



#### 1、jstack命令的意义

分析java中某一个线程的线程栈信息，<font color="red">如线程间死锁、死循环、请求外部资源导致的长时间等待等。</font>



#### 2、具体使用

- 使用jps 命令找到你需要查看的java进程id
- 使用top -Hp java进程id，可以看到该进程下所有的线程
- jstack [options] 线程id
  - options参数：
    - -l  打印关于锁的其他信息
    - -m 打印包含Java和本机C/ C++帧的混合模式堆栈跟踪
    - -F 当 jstack [-l] pid 没有响应时，强制打印一个堆栈转储