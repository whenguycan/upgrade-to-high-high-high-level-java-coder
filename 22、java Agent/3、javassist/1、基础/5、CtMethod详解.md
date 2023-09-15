## CtMethod详解

用CtClass可以成功获取到CtMethod了，那么针对CtMethod有哪些方法呢？



#### 1、CtMethod的使用



- 创建一个方法，并设置方法名、返回值、方法参数等

  ```java
  CtClass reternType = classPool.get(void.class.getName());
  String mname = "sayHello";
  CtClass[] parameters = new CtClass[]{classPool.get(String.class.getName())};
  //构建一个方法
  // 第一个参数为：方法返回值
  // 第二个参数为：方法名称
  // 第三个参数为：方法参数
  // 第四个参数为：方法需要加入到哪个类中
  CtMethod method = new CtMethod(reternType, mname, parameters, CtClass对象);
  ```

- 设置方法的访问修饰符

  ```java
  // 设置方法的访问修饰
  ctMethod.setModifiers(Modifier.PUBLIC);
  ```

  

- 设置方法体

  ```java 
  //方法体中 花括号不能少
  String src = "{ " +
  
    "System.out.println(\"hello \" + $1); " +
  
    "}";
  //设置方法的方法体
  // 第一个参数： 是方法的源代码
  method.setBody(src);
  ```

  

- 在方法体的最后面插入代码块

  ```java
  method.insertAfter("{" +
                     "System.out.println(\"yyyyyyy\");" +
                     "}");
  ```

  也可以是某一个类中的静态方法

  ```java
  method.insertAfter("com.example.modifyClass.modifyClass(this);");
  ```

  这儿插入的是modifyClass类中的modifyClass静态方法。

- 在方法体的最前面插入代码块

  ```java
  method.insertBefore("{" +
                     "System.out.println(\"yyyyyyy\");" +
                     "}");
  ```

  也可以是某一个类中静态方法

  ```java
  method.insertBefore("com.example.modifyClass.modifyClass(this);");
  ```

  这儿插入的是modifyClass类中的modifyClass静态方法。

  

  <font color="red">使用insertBefore、insertAfter插入的代码块，不能互相调用2者之间的变量。</font>

- 根据类中的原方法复制一份

  ```java
  //拷贝方法到代理方法
  // 第一个参数，是被拷贝方法
  // 第二个参数，新方法的方法名
  // 第三个参数，执行新方法归属哪个类
  // 第四个参数，
  CtMethod newMethod = CtNewMethod.copy(method, method.getName() + "$agent", targetClass, null);
  //将新方法放到类中
  ctClass.addMethod(newMethod);
  ```

  