## jmap的生成和分析JVM的内存文件



#### 1、生成JVM内存文件

- 确定需要查看的java进程的ID号

- 使用`jmap -heap 进程号`可以看到heap的大致信息

- 使用`jmap -histo 进程号`  打印每个class的实例数目,内存占用,类全名信息

  > jmap -histo:live 进程号 只统计活的对象数量

- 使用`jmap -dump:live,format=b,file=dump.hprof 进程号`导出dump文件

  > format 指定输出格式，live 指明是活着的对象，file 指定文件名
  >
  > .hprof 这个后缀是为了后续可以直接用 MAT(Memory Anlysis Tool)打开





#### 2、使用Mat等工具分析即可

