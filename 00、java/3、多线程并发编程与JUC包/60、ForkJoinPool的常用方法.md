## ForkJoinPool的常用方法



1. ForkJoinTask<T> submit(ForkJoinTask<T> task)方法：ForkJoinTask任务就是一个支持fork()分解与join()归并结果的任务。

   ForkJoinTask有两个重要的子类：

   - RecursiveAction类，该类初始化的任务没有返回值
   - RecursiveTask类，该类初始化的任务带有返回值