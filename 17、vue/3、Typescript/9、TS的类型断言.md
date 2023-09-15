## TS的类型断言

TypeScript 允许你覆盖它的推断，并且能以你任何你想要的方式继续执行，这种机制被称为「类型断言」。TypeScript 类型断言用来告诉编译器你比它更了解这个类型，并且它不应该再发出错误。



```typescript
function myInfo(info: number | string) :number {

  	//这一行，我断言info就是一个string类型，再执行info的length方法，如果执行之后发现不是undefined，证名info本就是string
    if((info as string).length != undefined){
        return (info as string).length
    }else{
      	//如果执行之后发现是undefined，证明它本来就是number类型，则需要再断言回number类型。
        return info as number
    }

}

console.log(myInfo(200))

```



