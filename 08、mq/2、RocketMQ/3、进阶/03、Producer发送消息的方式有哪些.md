## Producer发送消息的方式有哪些

一、同步发送消息：

producer要发送10条消息，只有当第一条发送到broker，且producer获取到发送成功的ACK之后，才会发送第二条消息，以此类推。

<font color="red">顺序消息最好使用不同发送的方式</font>

二、异步发送消息（最常用的）：

producer要发送10条消息，producer先发送第一条消息，但是producer不等待消息发送成功的ACK，而是直接发送第二条消息，以此类推。

三、单向发送消息：

producer要发送10条消息，producer仅负责发送消息，不等待不处理broker返回的发送成功的ACK，这种方式发送消息，broker也不会返回ACK消息。