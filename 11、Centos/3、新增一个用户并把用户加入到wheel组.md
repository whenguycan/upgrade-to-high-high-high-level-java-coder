## 新增用户并加入到wheel组中



#### 1、背景知识

学完前面的用户管理和用户组管理，那么我们就可以使用新增的用户登录到系统了！但是！！任何人登录到系统，如果有人剽窃了root用户的密码，都可以通过`su root`切换到root身份，然后为所欲为！这样肯定不行啊，然后就有了wheel出现了！



#### 2、认识wheel组

wheel是一个特殊的用户组，只有加入到这个组的用户才能使用`su root`切换到root身份。而不加入到该组的用户，我们需要进行适当的配置让这些用户不能切换到root身份。

1. 修改 /etc/pam.d/su 文件，找到“#auth required /lib/security/$ISA/pam_wheel.so use_uid ”这一行，将行首的“#”去掉。
2. 修改 /etc/login.defs 文件，在最后一行增加“SU_WHEEL_ONLY yes”语句。

经过上面两步的操作，普通用户已经无法使用`su root`切换到root身份了！

将用户加入到wheel组中，然后再使用`su root` 看看效果！