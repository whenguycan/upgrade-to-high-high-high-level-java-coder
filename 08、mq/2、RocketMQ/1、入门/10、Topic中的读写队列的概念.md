## Topic中读写队列的概念

从物理上来说，读/写队列是同一个队列。所以，不存在读/写队列数据同步问题。读/写队列是逻辑上进行区分的概念。一般情况下，读/写队列数量是相同的。



如果创建Topic时，写队列是8，读队列是4，此时系统会创建8个Queue，分别是0 1 2 3 4 5 6 7。Producer会将消息写入到这8个队列，但是Consumer只会消费0 1 2 3 这4个Queue中的消息，而4 5 6 7中的消息不会被消费到。

再如果创建Topic时，写队列是4，读队列是8，此时系统会创建8个Queue，分别是0 1 2 3 4 5 6 7。Producer会将消息写入到0 1 2 3 这4个队列，但是Consumer会消费0 1 2 3 4 5 6 7 这8个Queue中的消息，而4 5 6 7中是没有消息的。



为什么要设计读/写队列，并且数量还可以不一致呢？

是为了方便Topic的Queue的缩容！！

例如，原来创建的Topic中包含16个Queue,如何能够使其Queue缩容为8个，还不会丢失消息?可以动态修改写队列数量为8，读队列数量不变。此时新的消息只能写入到前8个队列，而消费都消费的却是16个队列中的数据。当发现后8个Queue中的消息消费完毕后，就可以再将读队列数量动态设置为8。整个缩容过程，没有丢失任何消息。