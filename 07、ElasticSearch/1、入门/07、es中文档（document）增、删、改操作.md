## es中文档（document）增、删、改操作



1. 往索引（index）中写入文档（document）

   用postman向es服务器发送POST请求，请求地址为：`http://localhost:9200/索引名称/_doc/[id号] ` 这儿的id是可选的，如果不写文档生成的id随机，如果写了就是文档的id。如果写了id，路径中的\_doc可以修改为_create，请求方式可以改为PUT。请求体body中的数据为json格式:

   ```json
   {
   
   	"xxxx":"xxxxx"
   
   }
   ```

   

2. 查看刚刚往[索引（index）](evernote:///view/30455392/s33/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/)中写入的一个文档（document）

   用postman向es服务器发送GET请求，请求地址为：`http://localhost:9200/索引名称/_doc/id号`

   

3. 查看刚刚往[索引（index）](evernote:///view/30455392/s33/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/)中写入的所有文档（document）

   用postman向es服务器发送GET请求，请求地址为：`http://localhost:9200/索引名称/_search`

   

4. 修改刚刚往[索引（index）](evernote:///view/30455392/s33/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/)中写入的一个文档（document）

   - 完全覆盖性修改：

     用postman向es服务器发送PUT请求，请求地址为：`http://localhost:9200/索引名称/_doc/id号` 请求体body中的数据格式为json：

     ```json
     {
     
     	"xxxx":"yyyy"
     
     }
     ```

     

   - 修改一个文档中的部分数据：

     用postman向es服务器发送POST请求，请求地址为：`http://localhost:9200/索引名称/_update/id号` 请求体body中的数据格式为json：

     ```json
     {
     
       "doc":{
     
       	"xxxxx":"mmmmm"
     
       }
     
     }
     ```

     



5. 删除刚刚往[索引（index）](evernote:///view/30455392/s33/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/4a6392ea-5efa-4b96-92f1-9e70fa3ebbb4/)中写入的一个文档（document）

   用postman向es服务器发送DELETE请求，请求地址为：`http://localhost:9200/索引名称/_doc/id号`