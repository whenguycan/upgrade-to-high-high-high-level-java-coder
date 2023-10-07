## must关键字



#### 1、使用范例

```json
{
  "query":{
    
    "bool":{
      
      "must":[
        
        {
          
          条件1中的match、term、terms、nested、子查询等具体参与字段匹配的规则
          
        },
        
        {
          
          条件2中的match、term、terms、nested、子查询等具体参与字段匹配的规则 
          
        },
        { //这儿跟的是子查询
          "bool":{
            "should":[
              {
                条件1....

              },
              {
                条件2.....

              }
            ]
          }
        }
        
        ......
        
      ]
      
    }
    
  }
  
}
```





#### 2、范例详述

`must`关键字，规定如下：

1. must中所有的条件都是 `and`（并且）的关系。
2. must中所有的条件都是跟字段匹配相关的了，所以都是跟着`match`、`term`、`terms`、`nested`、子查询等。
3. 如果后续跟着是多个条件使用[ ]，如果后续是单个条件可以使用{ }





#### 3、真实使用

```json
{
    "query":{
        "bool":{
            
            "must":[  //多条件and查询
                {
                    "term":{
                        "goodsPrice":1
                    }
                },
                {
                    "term":{
                        "goodsPrice":2
                    }
                },
                {
                  "bool":{
                    "should":[
                      	{
                      		条件1....
                      	
                    		},
                    		{
                    			条件2.....
                    			
                  			}
                    ]
                  }
                }
            ]
            
        }
    }

}
```

