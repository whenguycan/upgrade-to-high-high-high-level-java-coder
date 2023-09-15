## MAT分析jmap导出的文件



#### 1、打开jmap导出的文件

菜单栏 ”File“----->"Open Heap Dump "，随后选择如下入的选项即可

![avatar](./images/MG348.jpeg)



#### 2、如果有内存泄露的嫌疑，他会有如下的图的部分

![avatar](./images/MG349.jpeg)



#### 3、点击上图Details，可以查看详细内容

![avatar](./images/MG351.jpeg)



#### 4、可以点击上面的任意一行蓝色带链接的文字

![avatar](./images/MG352.png)



#### 5、根据第4步逐级打开就能看到，如下图的效果

![avatar](./images/MG353.jpeg)

ShallowHeap是指类本身占用内存大小（byte），

RetainedHeap是指经过一次GC，模拟把该类清理掉，能够节省出来的内存大小（byte），

RetainedHeap大出ShallowHeap那么多，证明该类确实存在问题！