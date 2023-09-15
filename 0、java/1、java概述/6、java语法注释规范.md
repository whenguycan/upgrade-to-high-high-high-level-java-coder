## java语法注释规范



#### 1、什么是注释

注释在java中没有特定的语义，只是标注一行或多行代码的功能与作用，让自己或者接手项目的人更容易读懂代码！

被注释掉的代码，不参与编译！



#### 2、为什么需要注释

注释是一个程序员的良好的变成习惯，在实际开发中，程序员可以先将思路通过注释整理出来，再用代码去实现。

大部分的程序员，在写代码的时候不喜欢写注释，但是在阅读他人的代码的时候喜欢规范别人写注释！



#### 3、单行注释

```java
//单行注释
```

示例

```java
class HelloChina {
  
  public static void main(String[] args){
    //输出一句话
    System.out.println("HelloWorld!!你好中国");
    
  }
  
}

class HelloShanghai {
  
  
}

class HelloChangzhou {
  
}
```





#### 4、多行注释

```java
/*
注释行1
注释行2
......
*/
```

示例

```java
/*

多行注释

*/
class HelloChina {
  
  public static void main(String[] args){
    //输出一句话
    System.out.println("HelloWorld!!你好中国");
    
  }
  
}

class HelloShanghai {
  
  
}

class HelloChangzhou {
  
}
```



#### 5、文档注释

> 一般不用

文档注释可以被jdk提供的javadoc工具识别解析，然后生成一套以网页形式提现的该程序的说明文档

```java
/**
@author twstart
@version 1.0
@email 2268676560@qq.com
*/
```

示例

```java
/**
@author twstart
@version 1.0
@email 2268676560@qq.com
*/
public class HelloWorld {
  
  public static void main(String[] args){
    //输出一句话
    System.out.println("HelloWorld!!你好中国");
    
  }
  
}

class HelloShanghai {
  
  
}

class HelloChangzhou {
  
}
```

使用javadoc生成说明文档

```shell
javadoc -d mydoc -author -version HelloWorld.java
```

`-d mydoc`是生成的说明文档存到mydoc这个目录职工

`-author -version`保留文档注释中的author、version信息

