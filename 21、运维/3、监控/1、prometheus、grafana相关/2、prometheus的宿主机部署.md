## 部署prometheus



#### 1、下载prometheus

下载地址：https://github.com/prometheus/prometheus/tags



#### 2、部署并启动

- 创建prometheus的安装目录

  ```shell
  mkdir -P /usr/local/prometheus
  ```

- 解压下载好的软件包

  ```shell
  tar -zxvf prometheus-2.24.1.linux-amd64.tar.gz -C /usr/local/Prometheus/
  ```

- 把解压之后的可执行文件移动到/usr/bin下

  ```shell
  cp promtool prometheus /usr/bin/
  ```

- 让prometheus能使用systemctl命令

  ```shell
  cat > /usr/lib/systemd/system/prometheus.service <<EOF
  [Unit]
  Description=The Prometheus monitoring system and time series database.
  Documentation=https://prometheus.io
  After=network.target
  
  [Service]
  User=root
  LimitNOFILE=8192
  ExecStart=/usr/bin/prometheus \\
      --config.file=/usr/local/prometheus/prometheus-2.24.1.linux-amd64/prometheus.yml \\
      --storage.tsdb.path=/var/lib/prometheus/data \\
      --web.console.templates=/usr/local/prometheus/prometheus-2.24.1.linux-amd64/consoles \\
      --web.console.libraries=/usr/local/prometheus/prometheus-2.24.1.linux-amd64/console_libraries \\
      --web.enable-lifecycle
  ExecReload=/bin/kill -HUP $MAINPID
  Restart=on-failure
  
  [Install]
  WantedBy=multi-user.target
  EOF
  ```

- 启动prometheus

  ```shell
  systemctl daemon-reload 
  systemctl enable prometheus
  systemctl start prometheus
  systemctl status prometheus
  ```

- 访问页面 http://IP:9090 能打开证明prometheus成功部署了！

