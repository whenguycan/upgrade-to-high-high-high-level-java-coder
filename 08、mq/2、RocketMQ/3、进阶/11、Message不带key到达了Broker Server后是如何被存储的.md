## Message不带key到达了Broker Server后是如何被存储的



#### Message不带key是什么意思

![avatar](../images/8.jpg)

在创建Message的时候，可以设置Message的key，而我们所讲的情况是不设置key的！



#### Message在Broker Server被存储的过程概述

1. Broker根据消息所属的Topic、queueId（queueId在客户端发送数据的时候已经确定了）,获取到即将被写入消息对应索引条目在consumequeue目录中的写入偏移量（就是在000000000000000000000这个文件中获取即将被写入消息的偏移量），即QueueOffset。

   ![avatar](../images/81.jpg)

   每个Queue中的文件（00000000000000000000），文件名都是固定的（因为它也是记录第一条数据的偏移量，但是，它的每一条数据都是固定的长度，所以名称是固定的。）

2. 将queueId、queueOffset等数据， 与消息一起封装为消息单元

3. 将消息单元写入到commitlog

   ![avatar](../images/3.jpg)

   在Broker Server的store目录中就有一个commitlog目录，commitlog目录中有很多文件，这些文件被称为mappedFile文件。Message最终都会被记录到这些mappedFile文件中。

   mappedFile文件大小为1G (小于等于1G)，文件名由20位十进制（多少字节的数据）数构成，表示当前文件的第一条消息的起始位移偏移量。

   > 第一个文件名一定是20位0构成的。因为第一个文件的第一条消息的偏移量commitlog offset为0。当第一个文件放满时，则会自动生成第二个文件继续存放消息。假设第一个文件大小是1073741820字节(1G= 1073741824字节)，则第二个文件名就是0000000001073741824。以此类推，第n个文件名应该是前n-1 个文件大小之和。<font color="red">一个Broker Server中所有mappedFile文件的commitlog offet是连续的。</font>
   >
   > ![avatar](../images/82.jpg)
   >
   > 无论当前Broker Server中存放着多少Topic的消息，这些消息都是被顺序写入到了mappedFile文件中的。也就是说，这些<font color="red">**消息在Commitlog中存放时并没有被按照Topic进行分类存放**</font>。
   >
   > 
   >
   > commitlog文件中每一条消息的具体组成：
   >
   > ![avatar](../images/91.jpg)

4. 形成消息索引条目

   消息索引条目长这样,所有的消息产生的消息索引条目，长度都是一致的！

   ![avatar](../images/93.jpg)

5. 将消息索引条目分发到相应的consumequeue中对应的Topic的QueueId的文件（即0000000000000000000）中

   ![avatar](../images/81.jpg)

   每个Queue中的文件，文件中可以包含30万条数据。

   

   



#### 总结，消息投递概览图

![avatar](../images/51.jpg)