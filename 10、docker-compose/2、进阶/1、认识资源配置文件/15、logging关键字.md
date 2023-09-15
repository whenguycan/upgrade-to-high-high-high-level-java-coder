## logging关键字



#### 1、使用范例

```yaml
version: '3'

services:
  gitlab:
    image: 'gitlab/gitlab-ce:15.8.0-ce.0'
    restart: always
    hostname: 'www.czgitlab.com'
    environment:
      TZ: 'Asia/Shanghai'
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://10.10.210.24:9999'
        gitlab_rails['gitlab_shell_ssh_port'] = 2222
      http_proxy: http://10.10.210.19:8888
      https_proxy: http://10.10.210.19:8888
    ports:
      - '9999:9999'
      - '8443:443'
      - '2222:22'
    volumes:
      - $GITLAB_HOME/config:/etc/gitlab
      - $GITLAB_HOME/data:/var/opt/gitlab
      - $GITLAB_HOME/logs:/var/log/gitlab
    logging:
      driver: "json-file"
      options:
        max-size: "2g"
        max-file: "2"
```



#### 2、范例详述

logging下的driver是配置日志驱动，具体还有哪些驱动自己去查，我们一般仅使用json-file。options是配置日志参数。

