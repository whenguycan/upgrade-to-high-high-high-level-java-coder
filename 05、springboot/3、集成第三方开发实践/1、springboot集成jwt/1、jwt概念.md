## 概念



#### 1、什么是jwt

JSON Web Token (JWT) 是一个开放标准，它定义了一种紧凑且自包含的加密方式，用于在各方之间以JSON 对象方式安全地传输信息，被传输的信息是数字签名的，可以验证和信任。

JWT 可以使用密钥（使用 HMAC 算法）或使用 RSA 或 ECDSA 的公钥/私钥对进行签名。

签名的令牌可以验证其中包含的声明的完整性，而加密的令牌会向其他方隐藏这些声明。当使用公钥/私钥对对令牌进行签名时，只有持有私钥的一方才可以签署。



#### 2、使用场景

**授权（Authorization）**：这是最常见的场景。用户登录后，每个后续请求都将包含 JWT，从而允许用户访问该令牌允许的路由、服务和资源。 单点登录是当今广泛使用 JWT 的一项功能，因为它的开销很小并且能够在不同的域中轻松使用。

**信息交换（Information Exchange）**：JWT令牌是在各方之间安全传输信息的好方法。 因为可以对 JWT 进行签名（例如，使用公钥/私钥对），所以可以确定发件人就是他们所说的那个人。此外，由于使用 header和payload 计算签名，还可以验证内容是否被篡改。



