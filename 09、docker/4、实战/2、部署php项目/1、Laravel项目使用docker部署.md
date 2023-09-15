## Laravel项目使用docker部署



#### 1、创建一个docker内公用的网络

因为php项目需要nginx做为反向代理，所以我们需要启动2个容器，一个是php项目，一个是nginx，所以需要把他们放在同一个docker的网络中，所以我们最好先建立一个docker网络叫sellphonecard。

```shell
docker network create --driver=bridge --subnet=10.10.0.0/16 sellphonecard
```



#### 2、将larave项目直接制作成tgz的压缩包



#### 3、编写如下的Dockerfile

```dockerfile
FROM bitnami/php-fpm:8.1.21 #因为php要运行必须要使用php-fpm，所以这儿需要使用php-fpm的镜像
RUN mkdir -p /var/www/html/app
WORKDIR /var/www/html/app
ADD app.tgz ./ #注意这儿会自动解压
RUN cd ./phonecard && chmod -R 0777 ./storage/ #phonecard目录是压缩包解压后的目录
EXPOSE 9000 #php-fpm 需要使用到9000端口
```



#### 4、使用如下命令将php项目打成镜像

```shell
docker build -f ./Dockerfile -t php-project:1.0 .
```



#### 5、启动php项目容器

````java
docker run -i -d --name=php --network=sellphonecard --ip=10.10.0.2 第4步打好的镜像id
````

注意这儿没有把9000端口暴露到宿主机，因为是内部服务，访问php项目的入口在我们后面的容器nginx上，所以这儿不用暴露9000端口。



#### 6、准备一个nginx需要的配置文件

用于配置反向代理php请求到之前部署的容器中去

```nginx
server {
    listen       80;
    listen  [::]:80;
    index  index.php index.html index.htm;
    root  /var/www/html/app/phonecard/public; #项目根目录，一般php项目放在php-fpm这个容器中，所以此处的地址是指向php-fpm的项目地址！！
    try_files $uri $uri/ @rewrite; #此处根据需要配置
    location @rewrite {
        rewrite ^(.*) /index.php?/$1 last;
    }
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /var/www/html;
    }
    client_max_body_size 10m;
		
  	#配置反向代理php请求	
    location ~ \.php {
        root           /var/www/html/app/phonecard/public; #项目根目录，一般php项目放在php-fpm这个容器中，所以此处的地址是指向php-fpm的项目地址！！
        fastcgi_pass   10.10.0.2:9000; #连接php-fpm的服务
        fastcgi_index  index.php;
        fastcgi_param  SCRIPT_FILENAME  /var/www/html/app/phonecard/public/$fastcgi_script_name; #上面连接上php-fpm之后，php-fpm会根据这个请求地址，到php-fpm容器的本地找到对应路径的文件解析，并把解析的php结果返回给nginx
        include        fastcgi_params;
    }
    location ~ \.(jpg|jpeg|png|gif|css|js|xlsx) { #本地静态文件配置
        root  /var/www/html/app/phonecard/public;
    }
}
```



#### 7、启动nginx容器

```shell
docker run -i -d -v /上一步中app.conf的路径:/etc/nginx/conf.d/app.conf --name=nginx-php --network=sellphonecard --ip=10.10.0.3 nginx的镜像ID
```

注意，这儿也没有对外暴露端口，是因为，访问入口在前端项目。

