## TS类的私有成员的getter、setter的方法

当TS中一个类的属性被private修饰，那么外面就不能直接访问该属性了，我们可以对该属性手动写上getXxx()、setXxx()方法，但是我明明要访问的是一个属性，最后却访问成了getXxx()、setXxx()方法，不符合语义啊！！！所以我们就需要使用TS内部预定好的方式实现getter和setter。



getter/setter方法例子

```typescript
class Person {

		//私有化属性 一定要以“_”开头
    private _age: number = 0


  	//编写age的getter方法，方法名最好与属性名一致，不要带“_”
    get age() : number{
        return this._age
    }

  	//编写age的setter方法，方法名最好与属性名一致，不要带“_”
    set age(age: number) {
        this._age = age
    }
}

let person = new Person()
person.age = 32
console.log(person.age)

```