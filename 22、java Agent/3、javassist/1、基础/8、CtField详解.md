## CtField详解

用CtClass可以成功获取到CtField了，那么针对CtField有哪些方法呢？



#### 1、CtField的使用



- 创建一个CtField，并设置类型、名称等

  ```java
  //第1个参数：字段的类型
  //第2个参数：字段的名称
  //第3个参数：字段属于哪个CtClass
  CtField ctFieldNew = new CtField(CtClass.intType,"age",ctClass);
  ```

- 设置属性的访问修饰符为public

  ```java
  ctFieldNew.setModifiers(Modifier.PUBLIC);
  ```

  

