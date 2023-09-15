## 基于角色的访问控制（RBAC）



#### RBAC概念

用户或者pod，需要去api server调用接口，api server会基于RBAC去决定有没有访问权限。



#### Role与ClusterRole

概念：

​	表示一个角色包含了一组权限的规则。

- Role:

​		一个 `Role` 对象只能用于授予对某一单一命名空间中资源的访问权限。

- ClusterRole: 

​	`ClusterRole`对象可以授予以下几种资源的访问权限：

​		k8s集群范围资源，例如节点，即node

​		所有命名空间内的资源，例如pod

​		非资源类型endpoint，例如/healthz

例子：

​	定义一个Role

```yaml
kind: Role
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  namespace: default
  name: pod-reader
rules:
- apiGroups: [""] # 空字符串"" 表明使用 core API group
  resources: ["pods"]
  verbs: ["get", "watch", "list"]
```

​	定义一个ClusterRole

```yaml
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  # 鉴于 ClusterRole 是集群范围对象，所以这里不需要定义 "namespace" 字段
  name: secret-reader
rules:
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["get", "watch", "list"]
```



#### 资源的引用

概念：

​	在上面Role与ClusterRole中，定义有权限访问的资源，是怎么定义的？

例子：

​	apiGroups可以有：""

> 空字符串"" 表明使用 core API group

​	resources可以有：pods、services、deployments、jobs、configmaps、nodes       

> 例：["pods", "deployments"]   
>
> 因为pods这些资源属于`core API group`

​	verbs可以有："get"、"list"、"watch"、"create"、"update"、 "patch"、 "delete"

> 例： ["get", "list", "watch", "create", "update", "patch", "delete"]





#### User、Group与Service Account

概念：

- 单一用户为User、群组用户为Group，这俩都是给用户用的，不要指定`namespace`
- 服务账户为Service Account，它是给pod内的进程调用api server时提供的身份证明用的，需要指定`namespace`



例子：

- 创建ServiceAccount

  ```yaml
  apiVersion: v1
  kind: ServiceAccount
  metadata:
    name: rabbitmq-cluster
    namespace: public-pro
  ```

  



#### RoleBinding与ClusterRoleBinding

概念：

​	RoleBinding 是将 Role与User、Group、Service Account进行绑定

​	ClusterRoleBinding 是将ClusterRole与User、Group、Service Account进行绑定

例子：

​	定义一个RoleBinding

```yaml
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: read-pods
  namespace: default
subjects:
- kind: User
  name: jane
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: pod-reader
  apiGroup: rbac.authorization.k8s.io
```

​	定义一个ClusterRoleBinding

```yaml
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: read-secrets-global
subjects:
- kind: Group
  name: manager
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: secret-reader
  apiGroup: rbac.authorization.k8s.io
```





#### RoleBinding或ClusterRoleBinding之后，如何在实际中使用

就是使用User、Group、ServiceAccount。

```yaml
kind: StatefulSet
apiVersion: apps/v1
metadata:
  labels:
    app: rabbitmq-cluster
  name: rabbitmq-cluster
  namespace: public-pro
spec:
  replicas: 3
  selector:
    matchLabels:
      app: rabbitmq-cluster
  serviceName: rabbitmq-cluster
  template:
    metadata:
      labels:
        app: rabbitmq-cluster
    spec:
      containers:
      	...
     	serviceAccountName: xxxxxxx     #看这儿就行了！！！
```







