## gitlab的CICD的执行流程



执行流程如下：

1. 项目代码提交

2. CICD中的job拉取代码倒当前CICD工作环境的本地

3. 读取项目中.gitlab-ci.yml的内容

4. 获取到.gitlab-ci.yml中声明的stages

   ```yaml
   stages:
     - build
     - buildimage
     - deploy
   ```

   stages是有先后顺序的，只有先处理build才能处理buildimage、最后才是deploy

   

5. 执行stages中定义好的各个步骤