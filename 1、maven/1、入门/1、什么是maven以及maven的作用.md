## 什么是maven以及maven的作用

1. 什么是maven

   Maven 是一个项目管理工具，它包含了一个项目对象模 型 (POM：Project Object Model)，一组标准集合，一个项目生命周期(Project Lifecycle)，一个依赖管 理系统(Dependency Management System)，和用来运行定义在生命周期阶段(phase)中插件(plugin)目标 (goal)的逻辑。

2. maven的作用

   1. 依赖管理

      maven 工程中不直接将 jar 包导入到工程中，而是通过在 pom.xml 文件中添加所需 jar 包的坐标，这样就很好的避免了 jar 直接引入进来，在需要用到 jar 包的时候，只要查找 pom.xml 文 件，再通过 pom.xml 文件中的坐标，到一个专门用于”存放 jar 包的仓库”(maven 仓库)中根据坐标从 而找到这些 jar 包，再把这些 jar 包拿去运行。

   2. 一键构建

      项目从编译、测试、运行、打包、安装 ，部署整个过程都交给 maven 进行管理，这个 过程称为构建。一键构建 指的是整个构建过程，使用 maven 一个命令可以轻松完成整个工作。