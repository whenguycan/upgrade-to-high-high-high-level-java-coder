## 配置文件配置https且根据域名转发



在`/etc/nginx/conf.d`目录中新增一个.conf文件，内容如下：

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

	location ^~/iccCarPhoto/ {  #匹配https://ryt.czctown.com/iccCarPhoto/xxx/xxx 域名后以iccCarPhoto开头的
		proxy_set_header Host $host;
    proxy_set_header X-Request-Id $request_id;
    proxy_pass http://121.229.5.12:9480/; # 全部转发到 http://121.229.5.12:9480/xxx/xxx 这儿iccCarPhoto就被过滤掉了，如果写成http://121.229.5.12:9480这样，就不会过滤iccCarPhoto
	}


  location / { #以上规则都没有匹配到则转发到http://127.0.0.1:8085
    proxy_set_header Host $host;
    proxy_set_header X-Request-Id $request_id;
    proxy_pass http://127.0.0.1:8085/;

  }
}
```

