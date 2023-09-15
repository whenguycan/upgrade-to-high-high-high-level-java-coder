## TS的枚举类型



#### 创建一个枚举：

```typescript
enum Weeks {
  one,
  two,
  three,
  four,
  five
}
```

当没有给枚举中的数据附默认值的时候，从第一个开始是0，后面依次+1。如果给枚举中的数据附了默认值，从附默认值开始的数据后续依次+1，比如：

```typescript
enum Weeks {
    one = 1,
    two,
    three=5,
    four,
    five
}

```

如果，上面的enum中的four改为 如下：

```typescript
enum Weeks {
    one = 1,
    two,
    three=5,
    four = "tangwei",
    five
}


```

这样写是有问题的，因为four之后的five没法在four的基础上+1操作了，所以会报错，所以只能把four之后的每个值都手动填写上自己要的值才行。





使用const修饰枚举

```typescript
const enum Weeks {
    one = 1,
    two,
    three=5,
    four = "tangwei",
    five
}

```





#### 枚举如何调用

```typescript
console.log(Weeks.one); //输出0

console.log(Weeks.three); //输出2

console.log(Weeks[1]);//会根据[]中的数字，到枚举中找到第几个数据，不如[2]，输出two
```





#### 枚举类型用于数据类型注解中

```typescript
enum Weeks {
    one = 1,
    two,
    three=5,
    four,
    five
}

let num: Weeks = 7 //num只能设置枚举中存在的值
console.log(num)

```

