## should与must同时使用



#### 1、错误使用

```json
{
  "query":{
    "bool":{
      "must":[
        {
          条件
        },
        {
          条件
        }
      ],
      "should":[
        {
          条件
        },
        {
          条件
        }
      ]
    }
  }
}
```

如果must与should同级出现，那么只会有must有效，should就不执行了！如果要组成should与must的组合查询，只需要将should或must做为另一个的子查询。



#### 2、正确使用

```json
{
  "query":{
    "bool":{
      "must":[
        {
          条件
        },
        {
          条件
        },
        {
          "bool":{
            "should":[
              {
                条件
              },
              {
                条件
              }
            ]
          }
        }
      ]
      
    }
  }
}
```

