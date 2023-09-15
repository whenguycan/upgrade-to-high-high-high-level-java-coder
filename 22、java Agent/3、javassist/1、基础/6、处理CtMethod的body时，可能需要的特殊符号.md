## 处理CtMethod的body时，需要的特殊符号



我们在第5篇文章中，使用到了一个特殊符号，在获取到参数值的时候，我们是用了$1，代码如下：

```java
CtMethod method = new CtMethod(reternType, mname, parameters, targetClass);

//方法体中 花括号不能少
String src = "{ " +

  "System.out.println(\"hello \" + $1); " +

  "}";
//设置方法的方法体
// 第一个参数： 是方法的源代码
method.setBody(src);
```



有如下特殊符号：

| 标识符                     | 作用                                                 |
| -------------------------- | ---------------------------------------------------- |
| $0、$1、$2、 3 、 3、 3、… | $0代表this、$1-$N代表方法参数（1-N是方法参数的顺序） |
| $args                      | 方法参数数组，类型为Object[]                         |
| $$                         | 所有方法参数，例如：m($$)相当于m($1,$2,…)            |
| $cflow(…)                  | control flow 变量                                    |
| $r                         | 返回结果的类型，在强制转换表达式中使用。             |
| $w                         | 包装器类型，在强制转换表达式中使用。                 |
| $_                         | 返回的结果值                                         |
| $sig                       | 类型为java.lang.Class的参数类型对象数组              |
| $type                      | 为当前方法的返回值类型                               |
| $class                     | 为正在修改的类的类名                                 |

大致使用如下：

```java
CtMethod method = new CtMethod(reternType, mname, parameters, targetClass);

String src = "{ " +

  "System.out.println($1);" +

  "System.out.println($args);" +

  "System.out.println($type);" +

  "System.out.println($class);" +

  "System.out.println(\"hello \" + $1); " +

  "}";
//设置方法的方法体
// 第一个参数： 是方法的源代码
method.setBody(src);
```

