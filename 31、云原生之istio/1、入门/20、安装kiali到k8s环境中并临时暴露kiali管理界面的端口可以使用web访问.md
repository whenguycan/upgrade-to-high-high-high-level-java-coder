## 安装kiali到k8s环境中并临时暴露kiali管理界面的端口可以使用web访问



#### 1、安装kiali到k8s中

```shell
kubectl apply -f ${ISTIO_HOME}/samples/addons/kiali.yaml
```



#### 2、临时暴露kiali的service的20001端口

```shell
kubectl port-forward --address 0.0.0.0 -n istio-system service/kiali 20001:20001
```

