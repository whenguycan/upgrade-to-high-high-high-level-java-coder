## java agent是什么

java agent的就是一个`.jar`文件，核心作用是对.class文件(字节码文件)中<font color="red">方法</font>进行修改（bytecode instrumentation）



在java中类(.class)文件，由如下部分组成

![avatar](../images/MG420.jpeg)

说到底，java agent是对.class文件中方法的方法体(method body)进行修改。