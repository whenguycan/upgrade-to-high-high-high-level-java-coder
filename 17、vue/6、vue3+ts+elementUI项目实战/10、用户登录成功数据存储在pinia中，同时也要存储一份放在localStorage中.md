## 用户登录成功后，数据在pinia中存了一份，同样也需要在localStorage中存一份



因为localStorage中存的是写到用户本地的，pinia中只是用一个js对象去存储了，刷新页面就没有了！最好pinia中states中的数据最好初始化来自localStorage



```js
localStorage.setItem("key", value);


localStorage.getItem("key");
```

