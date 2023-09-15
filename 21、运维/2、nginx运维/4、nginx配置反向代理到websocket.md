## nginx配置反向代理websocket

在`/etc/nginx/conf.d`目录中新增一个.conf文件，文件内容需要区分https协议还是http协议。



#### 在使用https协议的情况下

在域名配置了https的情况下，如果再使用IP：PORT的方式去连接websocket会报错！所以我们需要把websocket的请求通过nginx反向代理到websocket中

```nginx
server{
  listen 443 ssl;   #监听443端口，如果是http则修改为监听80端口
  server_name ryt.czctown.com; #监听的域名
  ssl_certificate /usr/local/nginx/cert/9532817_ryt.czctown.com.pem; #证书文件
  ssl_certificate_key /usr/local/nginx/cert/9532817_ryt.czctown.com.key; #证书文件
  ssl_session_timeout 5m;
  ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
  ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
  ssl_prefer_server_ciphers on;

	location /websocket/ {
		proxy_pass http://192.168.0.177:8080; #websocket的服务
		proxy_http_version 1.1; #往下的配置全是固定模式
    proxy_set_header Upgrade $http_upgrade; 
    proxy_set_header Connection "Upgrade";
    proxy_connect_timeout 10s;
    proxy_read_timeout 120s;
    proxy_send_timeout 12s;
		proxy_set_header X-real-ip $remote_addr;
		proxy_set_header X-Forwarded-For $remote_addr;
		proxy_set_header Host $host;
		proxy_set_header X-Forwarded-Proto   $scheme;
	}

  location / { #以上规则都没有匹配到则转发到http://127.0.0.1:8085
    proxy_set_header Host $host;
    proxy_set_header X-Request-Id $request_id;
    proxy_pass http://127.0.0.1:8085/;

  }
}
```

随后通过`wss://ryt.czctown.com/websocket/xxx/xxx`去连接websocket。





#### 在使用http协议的情况下

