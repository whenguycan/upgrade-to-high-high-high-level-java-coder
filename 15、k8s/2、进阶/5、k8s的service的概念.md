## Service的概念

> 对于无头service，内部调用就用servicename + port就行。
>
> 对于有头service，且通过nodeport的方式暴露了端口，那么就用对外IP+port就行，也可以使用servicename + port



#### 有头service

- 存在意义：

  1. 给一组pod做负载均衡
  2. 防止pod失联。因为我们经常会对pod进行升级回滚操作，一旦操作了，pod对应的ip会变动，一旦变动了，有些pod就会失联找不到，那么就需要service这个中间件，去给pod提供注册发现服务，然后每次调用pod从service中查询，这样就避免了失联

- 如何跟pod建立关联：

  service定义部分的selector的值key:value，与pod定义部分的labels中的key:value一致，就能实现关联。实现关联之后，会根据service关联到pod的podIP信息+podPort组合成一个endpoint，放到service的Endpoints中。

- service的三种type：

  1. NodePort  对外暴露应用的时候使用，可以使用 IP+Port的形式访问
  2. LoadBalancer   对外暴露应用的时候使用，适用于公有云，<font color="red">它的使用我也一直没弄明白，因为没有地方实践</font>
  3. ClusterIP 集群内部使用，配合无头service使用。

- 使用范例：

  ```yaml
  kind: Service
  apiVersion: v1
  metadata:
    name: order
    namespace: czmall
    labels:
      app: order
  spec:
    ports:
      - name: http-8080
        protocol: TCP
        port: 8080      #当前service的端口
        targetPort: 8080   #pod的端扣 = container的端口 = dockerfile中expose的端口 
        nodePort: 30001   #service对外提供访问的port
    selector:
      app: order
    type: NodePort
    sessionAffinity: None #会话亲和性，可以为None和ClientIP两个值，使用ClientIP时，基于客户端IP地址识别客户端身份，把来自同一源IP地址的请求始终调度至同一个Pod对象。使用None时，随机选择一个Pod对象！
  
  ```

  

#### 无头service

> <font color="red">无头service，无法提供node节点的IP+Port的形式访问，只能在k8s内部通过`服务名:Port`访问。</font>

跟有头service配置一样，无非就是，指定类型为ClusterIP，加一个clusterIP: None，下面是范例：

```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: rabbitmq-standalone-server
  name: rabbitmq-standalone-server
  namespace: legend
spec:
  clusterIP: None  
  ports:
  - name: rabbitmq-port
    port: 5672    #当前service的端口
    protocol: TCP
    targetPort: 5672  #pod的端扣 = container的端口 = dockerfile中expose的端口 
  - name: rabbitmq-manage-port
    port: 15672    #当前service的端口
    protocol: TCP
    targetPort: 15672  #pod的端扣 = container的端口 = dockerfile中expose的端口 
  selector:
    app: rabbitmq-standalone
  sessionAffinity: None #会话亲和性，可以为None和ClientIP两个值，使用ClientIP时，基于客户端IP地址识别客户端身份，把来自同一源IP地址的请求始终调度至同一个Pod对象。使用None时，随机选择一个Pod对象！
  type: ClusterIP   #指定类型为ClusterIP
```



#### 常用命令

kubectl get/edit/describe/delete services xxxx -n yyyy





