## 开发体验HelloWorld



#### 1、在任意目录新建一个HelloWorld.java文件

> 后缀一定.java，如果系统不显示后缀名，需要打开显示后缀名。



#### 2、使用记事本打开并编辑HelloWorld.java文件

内容如下：

```java
class HelloChina {
  
  public static void main(String[] args){
    
    System.out.println("HelloWorld!!你好中国");
    
  }
  
}

class HelloShanghai {
  
  
}

class HelloChangzhou {
  
}
```

java的所有代码都需要放到一个`class`（我们称之为类）中

`public static void main(String[] args)`这一段，我们称之为入口方法，运行java代码默认就会找入口方法做为执行起点。这个main方法的格式是固定的！记住就行！方法内的代码，每一行都需要以`;`结尾！

`System.out.println("HelloWorld");`是输出一句话`HelloWorld`。还有一个方法`System.out.print(....)`，区别是换不换行！



注意：如果输出的中文是乱码，需要将文件另存为一下，然后选择对应的字符集，然后保存下！在Windows下需要保存为`ANSI`的编码不要`UTF-8`的编码



#### 3、编译HelloWorld.java并运行

- 了解java代码的编译执行过程

  ![avatar](../images/WechatIMG521.jpeg)

- 编译HelloWorld.java文件到.class文件

  > 编译的过程，只会从java语法层面入手，语法错误编译不通过，逻辑有错是能编译通过的！

  ```shell
  javac HelloWorld.java
  ```

  注意：这儿编译生成的.class文件的文件名是.java文件中的类名。.java文件中有几个类，就会编译生成几个.class的字节码文件！

- 运行.class文件

  ```shell
  java HelloChina
  ```

  注意：运行.class字节码文件时，指定文件是不要带.class后缀的！



#### 4、用记事本来编辑java很痛苦

所以我们需要用开发工具来替换记事本。就用editplus替代。





#### 5、安装editplus

过程略

