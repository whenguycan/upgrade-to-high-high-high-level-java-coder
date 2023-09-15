## 安装ik分词器



#### 1、为什么要使用ik分词器？

因为在中文下，es内置的分词器不好用，我们可以测试下，使用postman发送一个POST请求到 /_analyze 这个地址，请求体如下

```json
{

    "analyzer":"standard",
    "text":"尚硅谷电商项目"

}
```

得到的结果如下：

```json
{
    "tokens": [
        {
            "token": "尚",
            "start_offset": 0,
            "end_offset": 1,
            "type": "<IDEOGRAPHIC>",
            "position": 0
        },
        {
            "token": "硅",
            "start_offset": 1,
            "end_offset": 2,
            "type": "<IDEOGRAPHIC>",
            "position": 1
        },
        {
            "token": "谷",
            "start_offset": 2,
            "end_offset": 3,
            "type": "<IDEOGRAPHIC>",
            "position": 2
        },
        {
            "token": "电",
            "start_offset": 3,
            "end_offset": 4,
            "type": "<IDEOGRAPHIC>",
            "position": 3
        },
        {
            "token": "商",
            "start_offset": 4,
            "end_offset": 5,
            "type": "<IDEOGRAPHIC>",
            "position": 4
        },
        {
            "token": "项",
            "start_offset": 5,
            "end_offset": 6,
            "type": "<IDEOGRAPHIC>",
            "position": 5
        },
        {
            "token": "目",
            "start_offset": 6,
            "end_offset": 7,
            "type": "<IDEOGRAPHIC>",
            "position": 6
        }
    ]
}
```

将中文拆分成一个字字的，这样很明显不符合分词的要求，所以我们要引入ik分词器



#### 2、安装ik分词器

- ik分词器下载地址（注意版本要跟你的es的版本对应）：

  https://github.com/medcl/elasticsearch-analysis-ik/tags

- 因为我们是docker安装的，es容器内有个plugins目录，我们使用docker部署已经映射出来了，宿主机使用wget下载分词器的zip压缩包，并解压到plugins目录的宿主机映射目录中。注意：<font color="red">解压出来的文件都需要放到一个文件夹中，不能散在plugins文件夹中</font>,给这个文件夹一个777的权限。

- 检查ik分词器有没有成功安装，进入到es的容器中，进入安装的es的bin目录，有个elasticsearch-plugin工具，只要运行`elasticsearch-plugin list`命令，看看有没有ik即可。

- 安装完成之后需要重启es容器



#### 3、使用ik分词器

使用postman发送一个POST请求到 /_analyze 这个地址，请求体如下

```json
{

    "analyzer":"ik_smart",
    "text":"尚硅谷电商项目"

}
```

或者

```json
{

    "analyzer":"ik_max_word",
    "text":"尚硅谷电商项目"

}
```



