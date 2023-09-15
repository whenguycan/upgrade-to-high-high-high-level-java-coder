## maven的基础命令



1. mvn clean

   用于清理掉本地打包的target文件及其子文件

2. mvn compile

   编译项目

3. mvn package

   将项目打包成产出包jar/war，生成在项目根目录的target文件夹中

4. mvn install

   将项目打包成产出包jar/war，生成在项目根目录的target文件夹中，同时存放一份到本地的代码仓库中

5. mvn deploy

   将项目打包成产出包jar/war，生成在项目根目录的target文件夹中，同时将项目发布到远端仓库中

6. mvn test

   执行项目的单元测试文件，就是我们编写在项目的test文件夹中的内容

7. mvn site

   创建项目的文档，一般不用！

8. mvn versions:set -DnewVersion=0.0.5

   用于更新当前单个项目、父子项目的版本
   
8. mvn help:effective-settings

   查看当前生效的maven的settings.xml文件的最终的样子，如果有多个settings.xml文件，命令会进行合并给你看到最终的样子
   
10. mvn -X

   查看mvn相关配置文件的读取顺序

