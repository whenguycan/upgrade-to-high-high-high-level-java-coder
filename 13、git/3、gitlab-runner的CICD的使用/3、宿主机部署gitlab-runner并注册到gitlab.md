## 宿主机部署gitlab-runner

这儿只说怎么部署，关于gitlab runner的基础知识去看`docker方式部署gitlab-runner并注册到gitlab.md`一文。



部署步骤：

1. 下载gitlab-runner的yum源：

   下载地址：https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/yum/el7/ 

   

2. 下载好rpm源文件之后，就只要rpm -ivh 上面的rpm文件就安装好了gitlab-runner

   ```shell
   rpm -ivh gitlab-runner-14.6.1-1.x86_64.rpm
   ```

   如果提示，`错误：依赖检测失败：`这样的问题，可以在上面的命令上加上`--nodeps --force`

​		



3. 启动gitlab-runner，systemctl start gitlab-runner

   

4. 注册gitlab-runner到gitlab，参考`docker方式部署gitlab-runner并注册到gitlab.md`一文中的注册过程。

   

5. 因为装好gitlab runner之后会新增一个gitlab runner的用户，但是这个用户没有执行docker的权限，需要给他赋权：

   - 将gitlab runner用户添加到docker组

     sudo usermod -aG docker gitlab-runner

   - 测试用gitlab runner的用户运行docker命令

   

   

   

附：gitlab runner的其它命令

注销所有已经注册好的gitlab runner：gitlab-runner unregister --all-runners

注销某一个特性的gitlab runner：gitlab-runner unregister --url url地址 --token token字符串

查看gitlab runner是否注册成功：gitlab-runner verify

查看当前的gitlab runner注册了多少个runner：gitlab-runner list

停止当前gitlab runner的服务：gitlab-runner stop

启动当前gitlab runner的服务：gitlab-runner start

重启当前gitlab runner的服务：gitlab-runner restart

卸载gitlab runner：gitlab-runner uninstall

查看gitlab runner的状态：gitlab-runner status