## should关键字



#### 1、使用范例

```json
{
  "query":{
    
    "bool":{
      
      "should":[
        
        {
          
          条件1中的match、term、terms、nested、子查询等具体参与字段匹配的规则
          
        },
        
        {
          
          条件2中的match、term、terms、nested、子查询等具体参与字段匹配的规则 
          
        },
        
        ......
        
      ]
      
    }
    
  }
  
}
```





#### 2、范例详述

`should`关键字，规定如下：

1. should中所有的条件都是 `or`（或者）的关系。
2. should中所有的条件都是跟字段匹配相关的了，所以都是跟着`match`、`term`、`terms`、`nested`、子查询等。
3. 如果后续跟着是多个条件使用[ ]，如果后续是单个条件可以使用{ }





#### 3、真实使用

```json
{
    "query":{
        "bool":{
            
            "should":[  //多条件or查询
                {
                    "term":{
                        "goodsPrice":1
                    }
                },
                {
                    "term":{
                        "goodsPrice":2
                    }
                }
            ]
            
        }
    }

}
```

