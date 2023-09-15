## 创建一个docker网络

因为后续部署nacos、seata、nginx都需要在同一个网段中，所以需要先给docker创建一个网络。

docker network create seata