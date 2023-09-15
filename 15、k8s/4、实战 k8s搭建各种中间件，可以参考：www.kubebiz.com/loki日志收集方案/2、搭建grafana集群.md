## 搭建grafana集群



grafana的资源配置文件

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: grafana
  labels:
    app: grafana
  namespace: parking
spec:
  serviceName: grafana-cluster
  replicas: 2
  selector:
    matchLabels:
      app: grafana
  template:
    metadata:
      labels:
        app: grafana
    spec:
      containers:
      - name: grafana
        image: grafana/grafana:8.4.7
        imagePullPolicy: IfNotPresent
        env:
        - name: GF_AUTH_BASIC_ENABLED
          value: "true"
        - name: GF_AUTH_ANONYMOUS_ENABLED
          value: "false"
        resources:
          requests:
            cpu: 100m
            memory: 200Mi
          limits:
            cpu: '1'
            memory: 2Gi
        readinessProbe:
          httpGet:
            path: /login
            port: 3000
        volumeMounts: #挂载目录
        - name: wwwroot
          mountPath: /var/lib/grafana
      volumes: #这儿需要自己去创建一个grafana单独使用的pvc，然后下面使用这个pvc。为什么没有使用storageclass，因为老师测试使用的是k8s的1.25.1的版本，在1.25.1的版本中storageclass部署已经发生了变化！
      - name: wwwroot
        persistentVolumeClaim:
          claimName: my-pvc     
---
apiVersion: v1
kind: Service
metadata:
  name: grafana
  labels:
    app: grafana
  namespace: parking
spec:
  type: NodePort
  ports:
  - port: 3000
    targetPort: 3000
    nodePort: 30030
  selector:
    app: grafana
```



使用`kubectl apply -f xxx.yaml`搭建grafana集群，然后使用admin/admin登录grafana

