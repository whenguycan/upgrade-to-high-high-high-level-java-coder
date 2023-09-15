## 扩展ik分词器词库



#### 1、创建一个nginx的容器

- 在/data目录先新建一个nginx的文件夹，并在nginx的文件夹中新建一个conf文件夹

- 运行如下命令随便启动一个nginx的容器，目的是为了得到nginx的配置文件

  ```shell
  docker run -i -d  --name=nginx-tmp -p 8088:80 nginx:1.23.3
  ```

- 使用如下命令将上面启动的临时nginx的配置文件全部拷出来放到/data/nginx/conf中

  ```shell
  docker cp  0800d0412048:/etc/nginx/ /data/nginx/conf/
  ```

  注意，这样拷贝出来到conf中你会发现是一个nginx的文件夹，需要把nginx文件夹中的所有文件移动到conf中来。

- 启动nginx工作容器

  ```shell
  docker run -i -d -v /data/nginx/html:/usr/share/nginx/html -v /data/nginx/log:/var/login/nginx -v /data/nginx/conf:/etc/nginx --name=nginx-es -p 8089:80 nginx:1.23.3
  ```

  



#### 2、在已经启动的nginx容器中新增一个xxx.txt文件

这个文件确保通过http请求能访问到，文件内容就是一个一个分词，一行就是一个分词

```tex
中国人
乔碧萝
```

每一次修改分词词库文件都需要重启es。



#### 3、配置es的ik分词器的配置文件

在之前ik分词器安装到了es的plugins文件夹中，而plugins文件夹已经被映射到了/data/es/plugins中了，所以我们可以直接到宿主机上修改

- 找到映射的plugins文件夹

- 找到ik分词器的文件夹

- 找到ik分词器的config文件夹

- 找到`IKAnalyzer.cfg.xml`文件，并修改`entry key="remote_ext_dict"`这一行，填入到我们上面nginx中配置好的`xxx.txt`文本的web访问地址！带http的哈！

- 重启es容器

- 使用postman发送一个POST请求到 /_analyze 这个地址，请求体如下

  ```json
  {
  
      "analyzer":"ik_max_word",
      "text":"乔碧萝是中国人"
  
  }
  ```

  

  



