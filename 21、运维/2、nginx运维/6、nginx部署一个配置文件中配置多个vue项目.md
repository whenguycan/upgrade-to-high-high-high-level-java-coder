## 一个nginx配置文件中配置多个vue项目



```nginx
server {
    listen       80;
    listen  [::]:80;
    index  index.html index.htm;
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /var/www/html;
    }
    client_max_body_size 10m;

    location ^~/prod-api/ { #匹配prod-api开头的把请求代理到后端
        proxy_set_header Host $host;
        proxy_set_header X-Request-Id $request_id;
        proxy_pass http://10.10.0.3:80/;
    }

    location ^~ /admin/ { #后台管理项目，需要指定子路径
        alias           /var/www/html/phondcard/backend/;  #这儿一定要使用alias，如果请求是/admin/ab.html其会寻找/var/www/html/phondcard/backend/ab.html文件，不会带着/admin/
        index          index.html index.htm;
        try_files      $uri $uri/ /admin/index.html; #这儿的/admin/index.html是为了防止打开页面刷新后404
    }

    location / {
        root           /var/www/html/phondcard/front; 
        index          index.html index.htm;
        try_files      $uri $uri/ /index.html;
    }
    
}

```

