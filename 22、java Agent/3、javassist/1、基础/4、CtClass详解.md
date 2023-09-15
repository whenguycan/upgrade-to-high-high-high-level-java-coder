## CtClass详解

经过ClassPool之后，我们得到了CtClass对象，然后就是要操作CtClass对象了！



#### 1、CtClass的使用



- 创建类

  ```java
  //构建一个新的类
  CtClass ctClass = classPool.makeClass("com.example.Hhhh");
  ```

- 指定类的接口

  ```java
  //给对象实现一个接口
  ctClass.addInterface(classPool.get("接口的全路径"));
  ```

- 新增一个方法

  ```java
  ctClass.addMethod(CtMethod对象);
  ```

- 将class的修改写入JVM

  ```java
  Class cla = ctClass.toClass(); //不加任何参数，是指扔到当前的classLoader去加载
  
  //或者
  
  ctClass.toClass(loader, ctClass.getClass().getProtectionDomain()); //指定了把当前的class扔给哪个classLoader加载
  ```

- 获取到类中所有的构造方法

  ```java
  CtConstructor[] ctConstructors = ctClass.getDeclaredConstructors();
  ....省略遍历过程....
  ```

- 根据方法名，获取一个CtMethod

  ```java
  //获取一个公共方法，名称为addString
  CtMethod method = ctClass.getDeclaredMethod("addString");
  ```

- 新增一个字段

  ```java
  ctClass.addField(CtField对象);
  ```

- 将类写成文件  

  ```java
  ctClass.writeFile();
  ```

  ​    