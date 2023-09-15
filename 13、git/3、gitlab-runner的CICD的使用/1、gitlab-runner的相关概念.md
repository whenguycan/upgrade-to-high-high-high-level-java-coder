## gitlab-runner的相关概念



#### 1、什么是gitlab-runner

gitlab runner是一个开源项目，用于运行gitlab中的CI/CD作业并将结果发送回gitlab



#### 2、gitlab-runner的类型与状态

- 类型：
  1. shared 共享类型，运行整个平台项目的作业
  2. group 项目组类型，运行特性group下的所有的项目作业
  3. specific 项目类型，运行指定的项目作业
- 状态：
  1. locked 锁定状态，无法运行项目作业
  2. paused 暂停状态，暂时不会接受新的作业



