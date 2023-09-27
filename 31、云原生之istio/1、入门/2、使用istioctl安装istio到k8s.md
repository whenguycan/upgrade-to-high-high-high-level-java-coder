## 使用istioctl安装istio到k8s

> 官方文档参考：https://istio.io/latest/zh/docs/setup/install/istioctl/



#### 1、下载istio安装包

下载地址：https://github.com/istio/istio/releases



#### 2、解压缩安装包



#### 3、把解压出来的包下的bin目录下的istioctl工具，放到/usr/local/bin去中



#### 4、安装istion

我们这儿选择demo这个profile，具体什么是profile，在下一个课件中详细讲述。

使用如下命令安装

```shell
istioctl install --set profile=demo
```



#### 5、安装istio，会在k8s中创建一个istio-system的namespace，所有的istio相关的组件都安装在这个namespace中

```shell
kubectl get all -n istio-system
```



#### 6、安装好了之后，发现istio-ingressgateway的暴露方式为LoadBalancer，我们需要改成NodePort

```shell
kubectl patch svc istio-ingressgateway -n istio-system -p ‘{“spec”:{“type": “NodePort"}}'
```



#### 7、自行安装istio的插件

在istio1.7之后插件都在samples/addons/中以yaml的形式存放，使用如下命令去安装就行

```shell
kubectl apply -f  samples/addons/ 
```



#### 8、卸载

一、卸载所有安装了的插件：

```shell
kubectl delete -f samples/addons/
```



二、卸载k8s中安装好了的istio组件：

```shell
istioctl manifest generate --set profile=demo | kubectl delete -f -
```







