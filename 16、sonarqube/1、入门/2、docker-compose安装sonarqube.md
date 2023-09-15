## docker安装sonarqube



#### 1、准备容器映射目录

```shell
mkdir -p /opt/pgsql/data
mkdir -p sonar/data sonar/logs sonar/extensions
```



#### 2、拉取镜像

```shell
docker pull postgres:12.4-alpine 
docker pull sonarqube:lts-community
```



#### 3、修改系统参数

因为sonar内部依赖了elasticsearch，而ElasticSearch需要开辟一个65536字节以上空间的虚拟内存

```shell
vi /etc/sysctl.conf 
添加参数:新增如下内容在sysctl.conf文件中，当前用户拥有的内存权限大小 
vm.max_map_count=262144 

重启生效:让系统控制权限配置生效 
sysctl -p
```



#### 3、准备资源配置文件

```yaml
version: "3.7" 
services: 
  postgres:
    image: postgres:12.4-alpine 
    ports: 
    - 5432:5432 
    restart: always 
    environment: 
      - POSTGRES_DB=sonar 
      - POSTGRES_USER=sonar 
      - POSTGRES_PASSWORD=sonar 
      - TZ=Asia/Shanghai 
    volumes: 
    - /opt/pgsql/data:/var/lib/postgresql/data 
  sonarqube: 
    image: sonarqube:lts-community
    ports: 
    - 9000:9000 
    environment: 
      - SONARQUBE_JDBC_URL=jdbc:postgresql://postgres:5432/sonar
      - SONARQUBE_JDBC_USERNAME=sonar
      - SONARQUBE_JDBC_PASSWORD=sonar 
    restart: always 
    depends_on: 
      - postgres 
    volumes: 
    - /opt/sonar/data:/opt/sonarqube/data 
    - /opt/sonar/logs:/opt/sonarqube/logs 
    - /opt/sonar/extensions:/opt/sonarqube/extensions
```



#### 4、打开地址

地址：http://IP:9000/

默认用户名和密码都是admin，第一次登录需要修改密码。