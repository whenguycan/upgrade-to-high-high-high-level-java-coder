## TS的数据类型







#### 1、数据类型有哪些？

number，string、boolean、object、xxx[]、[number, string、object]、any、null、undefined、void、function



重点说明：

1. void一般用在函数返回上，代表函数不能有返回值。
2. any是任意类型



#### 2、数据类型的注解

- 作用：在于，强制限定声明的变量、函数参数、函数返回值的类型

- 范例：let 变量名称 : 数据类型 = xxxx

- 例子：

  - 设置变量的类型

    ```typescript
    let num : number = 100
    ```

  - 设置函数参数、返回值的类型

    ```typescript
    function a(b: string) : object {
    
        if (b === 'b'){
            return {
                age:18
            }
        }else{
            return {
    
            }
        }
    
    }
    
    
    console.log(a('b'))
    
    ```

  - 如果参数类型为function

    ```typescript
    function a (age: number, height: number): number {
        return age;
    }
    
    let b:(age: number, height: number)=>number = a //(age: number, height: number)=>number这一段就是声明a是一个function类型，(age: number, height: number)是function的参数，=>number是指function的返回类型
    
    console.log(b(10, 20))
    
    ```

    

  