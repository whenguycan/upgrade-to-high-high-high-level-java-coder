## ClassPool详解

即 类池详解



#### 1、什么是类池

类池中存放的最小单位是CtClass对象，不同的CtClass对象是由不同的ClassLoader加载进来的，所以类池中的CtClass对象按照ClassLoader的不同被划分进不同的分组中。



#### 2、使用

- 创建一个ClassPool

  ```java
  ClassPool classPool = new ClassPool(false); //参数为false，那么agent项目没有任何类被放入到classPool中
  
  ClassPool classPool = new ClassPool(true); //参数设置为true，那么当前agent项目中所有的自己开发的类、依赖的第三方jar包的类都会默认被包含进去
  
  ClassPool classPool = ClassPool.getDefault(); //效果同new ClassPool(true)一样,不同的是这个方法是用单例模式创建的classPool
  ```

- 给ClassPool添加一个classpath，可以通过这个classpath加入更多的类到classPool中

  - appendClassPath() 插在所有类查询路径的末尾

  ```java
  classPool.appendClassPath(new LoaderClassPath(ClassLoader对象));
  
  //或者
  
  classPool.appendClassPath("/.../.../...")
  ```

  - classPool.insertClassPath() 插在所有类查询路径的头部

  ```java
  classPool.insertClassPath(new LoaderClassPath(ClassLoader对象));
  
  //或者
  
  classPool.insertClassPath("/.../.../...")
  ```

- 从classPool中获取一个CtClass

  ```java
  String targentClassName = "com.example.agentspringboot.utils.BitStringUtil";
  //根据类名获取到要修改的class
  CtClass targetClass = classPool.get(targentClassName);
  ```

- 创建一个类

  ```java
  CtClass targetClass = classPool.makeClass("com.example.Hhhh");
  ```

  