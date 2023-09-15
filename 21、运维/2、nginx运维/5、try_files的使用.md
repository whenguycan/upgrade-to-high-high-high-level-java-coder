## try_files的使用



vue的项目，所有的跳转链接都是在index.html里面的，所以用nginx部署一个vue项目的时候，我们通常这样部署

```nginx
server {
    listen       80;

    location / { #任何请求都会走这儿
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

}

```

此时打开vue项目的首页没有问题，但是当再次刷新页面或者点击链接跳转的时候，就会出现404，是因为要请求的资源根据我们配置的nginx确实不存在，那么需要怎么修改呢？

```nginx
server {
    listen       80;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $uri $uri/ /index.html; #就只要加这一行
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }


}

```

加个try_files的作用，举例说明：比如 请求 127.0.0.1/images/test.gif 会依次查找 1.文件/usr/share/nginx/html/images/test.gif   2.文件夹 /usr/share/nginx/html/images/test.gif/下的index文件  3. 请求127.0.0.1/index.html文件。然后交由vue的index.html进行处理！