## docker-compose的优势

> 与docker相比的优势



1. docker是基于dockerfile创建得出的镜像，在这个镜像启动的时候是一个单独container(容器)，而docker-compoose创建得出的往往是多个container(容器)
2. docker管理容器只能在命令行操作，而docker-compose是基于资源配置文件
3. docker创建容器先要创建网络再创建挂载盘再启动容器的时候指定，而docker-compose只需要在资源配置文件中指定即可。
4. docker-compose是基于docker的容器编排工具。

