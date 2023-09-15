## rebase操作

> 变基操作



#### 1、使用场景

在使用多分支开发的时候，需要将一个分支的代码合并到另一个分支的时候会使用rebase操作

```shell
git rebase master
# 或者
git pull --rebase
```



#### 2、rebase的过程

在我们的项目中，假设当前的分支状态如下：

```yaml
...--o--*--A--B          <-- master
         \
          C--D--...--Z   <-- feature


```

master当前指向commit B，在“*号”commit创建了分支feature，并进行了多次提交：C到Z。

当在分支feature上执行git rebase master后。git定位到*号这次提交。随后需要将Commit C至Z复制到master当前指向的头部。我们暂时用C’、D’代表复制到master头部的这些commit，随后提交的分支图如下：

```javascript
                C'-D'-...-Z'   <-- feature
               /
...--o--*--A--B                <-- master
         \
          C--D--...--Z         [舍弃]
```

原本的feature会被舍弃掉转而使用新的feature分支。



具体的变换过程如下：

将C改变其基准，需要分析`*`号版本、B版本、C版本之间的差异，尝试进行合并，而这次比较中可能存在冲突，因此产生第一次合并冲突。如果没有冲突那么C版本就变基成功了。

```yaml
                C'       <-- HEAD (rebase in progress)
               /
...--o--*--A--B          <-- master
         \
          C--D--...--Z   <-- feature

```

随后移动D，比较D版本、C版本、*号版本的差异。如果产生冲突则需要处理。后续处理时过程类似。即以一个最近公共祖先为比较基础，找出每个块的差异看是否有冲突。

当我们在feature分支上多次对同一个文件的同一段代码多次修改，并多次提交。最后rebase master时，会出现处理多次自身冲突的问题。最终多次解决，并使用`git rebase --continue`完成分支基准修改。



#### 3、合并的底层原理

参考`merge操作`一文中的`合并的底层原理`部分！