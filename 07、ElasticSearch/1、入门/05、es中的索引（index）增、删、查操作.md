## es中的索引（index）增、删、查操作



1. 创建索引（重复创建相同的所以会报错）：

   用postman向es服务器发送PUT请求，请求地址为：http://localhost:9200/索引名称

2. 查看上面创建的一个索引的信息

   用postman向es服务器发送GET请求，请求地址为：http://localhost:9200/索引名称

3. 查看所有的创建好了的索引信息

   用postman向es服务器发送GET请求，请求地址为：http://localhost:9200/_cat/indices?v

4. 删除上面创建的索引

   用postman向es服务器发送DELETE请求，请求地址为：http://localhost:9200/索引名称

