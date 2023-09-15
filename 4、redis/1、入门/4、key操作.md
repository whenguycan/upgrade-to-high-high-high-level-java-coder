## key操作

因为redis是存放键值对信息的，key就是那个键，redis中有一系列针对key操作的命令。



1. 判断一个key是否存在

   ```shell
   exists key-name
   ```

2. 修改一个key的过期时间

   ```shell
   expire key-name time-secondes
   ```

3. 删除一个key

   ```shell
   del key-name
   ```

4. 移动一个key到另外一个db库

   ```shell
   move key-name db-index
   ```

5. 获取一个key的数据类型

   ```shell
   type key-name
   ```

   