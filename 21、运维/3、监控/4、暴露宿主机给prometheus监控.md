## 暴露宿主机让prometheus监控



#### 1、下载node_exporter

下载地址：https://github.com/prometheus/node_exporter/tags



#### 2、部署

- 创建node exporter的目录

  ```shell
  mkdir -p /usr/local/node_exporter
  ```

- 解压下载的node exporter的压缩包

  ```shell
  tar -zxvf  node_exporter-1.1.0.linux-amd64.tar.gz -C /usr/local/node_exporter
  ```

- 让node exporter能使用systemctl

  ```shell
  cat > /usr/lib/systemd/system/node-exporter.service <<EOF
  [Unit]
  Description=Node Exporter
  Documentation=https://github.com/prometheus/node_exporter
  After=network.target
  
  [Service]
  User=root
  ExecStart=/usr/local/node_exporter/node_exporter-1.1.0.linux-amd64/node_exporter --web.listen-address=:9100
  Restart=on-failure
  
  [Install]
  WantedBy=multi-user.target
  EOF
  
  ```

  注意：

  1. 如果需要修改端口，在node_exporter后加 `--web.listen-address=:9100`
  2. 如果需要修改监控暴露的地址，在node_exporter后加`--web.telemetry-path="/metrics" `

  

  启动node exporter：

  ```shell
  systemctl daemon-reload 
  systemctl enable node-exporter
  systemctl restart node-exporter
  systemctl status node-exporter
  ```

  如果遇到启动失败了，去看/var/log/message中的日志！

  

  
  
  然后去修改prometheus的配置文件，把当前node加入到监控。
  
  