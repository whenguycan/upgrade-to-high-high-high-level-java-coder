## 部署vue项目



#### 1、打包vue项目

将dist目录制作成.tgz的压缩包



#### 2、编写如下的Dockerfile文件

```dockerfile
FROM nginx:stable #因为，前端项目打好包就是一堆html文件，需要配合nginx才能玩，所以要用nginx的镜像
RUN mkdir -p /var/www/html/phondcard
ADD ./front.tgz /var/www/html/phondcard #用户端项目
ADD ./backend.tgz /var/www/html/phondcard #管理端项目
COPY ./app.conf /etc/nginx/conf.d/ #http的配置
COPY ./app-https.conf /etc/nginx/conf.d/ #https的配置
COPY ./jslhw.cn.key /etc/nginx/conf.d/jslhw.cn.key #域名证书
COPY ./jslhw.cn.pem /etc/nginx/conf.d/jslhw.cn.pem #域名证书
```



#### 3、以https的配置文件为例

```nginx
server {
    listen 443 ssl;   #监听443端口，如果是http则修改为监听80端口
    server_name www.jslhw.cn; #监听的域名
    ssl_certificate /etc/nginx/conf.d/jslhw.cn.pem; #证书文件
    ssl_certificate_key /etc/nginx/conf.d/jslhw.cn.key; #证书文件
    ssl_session_timeout 5m;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
    ssl_prefer_server_ciphers on;
    index  index.html index.htm;
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /var/www/html;
    }
    client_max_body_size 10m;

    location ^~/prod-api/ { #根据prod-api路径转发请求到php-fpm的反向代理的nginx
        proxy_set_header Host $host;
        proxy_set_header X-Request-Id $request_id;
        proxy_pass http://10.10.0.3:80/;
    }

    location ^~ /admin/ {  #根据prod-api路径判断是管理端项目
        alias           /var/www/html/phondcard/backend/;
        index          index.html index.htm;
        try_files      $uri $uri/ /admin/index.html;
    }

    location / { # 默认是用户端项目
        root           /var/www/html/phondcard/front;
        index          index.html index.htm;
        try_files      $uri $uri/ /index.html;
    }

}
```



#### 4、运行命令制作前端项目的镜像



#### 5、运行前端项目的容器

```shell
docker run -i -d --name front -p 80:80 -p 443:443  --network=sellphonecard --ip=10.10.0.5 前端项目镜像ID
```

需要暴露443、80端口