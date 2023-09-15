## 删除namespace一直处于Terminating状态



- 先正常删除

  ```shell
  kubectl delete namespace NAMESPACENAME
  ```

- 不行就强制删除

  ```shell
  kubectl delete namespace NAMESPACENAME --force --grace-period=0
  ```

- 此时如果删除不了，通过kubectl get ns查看，被删除的ns出现Terminating的时候，有两种解决方案

  - 方案一：

    修改finalizers，删除下面红框中的内容

    ```shell
    kubectl edit namespace NAMESPACE_NAME
    ```

    ![avatar](../images/231.png)

  - 方案二：

    如果方案一中没有finalizers内容，则采用方案二

    - 将namespace内容导出到tmp.json文件中：

      ```shell
      kubectl get namespace NAMESPACE_NAME -o json > tmp.json
      ```

    - 修改tmp.json内容，删除json中以下内容：

      ```json
      {
          //删除spec整个内容，包括spec
          "spec": {
              "finalizers": [
                  "kubernetes"
              ]
          },
          
          "status": {
              "phase": "Terminating"
          }
      }
      ```

    - 开启k8s接口代理，新开一个窗口，执行

      ```shell
      kubectl proxy
      ```

    - 调用接口删除Namespace，注意URL中修改成要删除的NAMESPACE_NAME

      ```shell
      curl -k -H "Content-Type: application/json" -X PUT --data-binary @tmp.json http://127.0.0.1:8001/api/v1/namespaces/NAMESPACE_NAME/finalize
      ```

      