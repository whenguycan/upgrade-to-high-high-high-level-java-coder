## ForkJoinPool的基本使用



准备线程操作资源类

```java
public class LockResouce extends RecursiveTask<Long> { //泛型Long，就是该任务的返回值类型

    private final static Long THRESHOLD = 1000L;

    private long start;
    private long end;

    public LockResouce(long start, long end) { //初始化2个数
        this.start = start;
        this.end = end;
    }

    //重写RecursiveTask的compute()方法
    @Override
    protected Long compute() {

        long sum = 0;

        if ((end - start) < THRESHOLD){ //没有超过1000，不用进行任务分解，注意 这儿只开了一个线程，不会多开！
            for (long i = start; i<=end; i++){
                sum = sum + i;
            }
        }else{ //超过1000，需要对任务进行分解

            ArrayList<LockResouce> taskList = new ArrayList<>();

            //约定分解任务，每次执行100个小任务，分(end-start)/100次执行。
            long step = (end-start)/100; //比如end为20000，start为0，则每次执行100个任务，分200次执行

            long pos = start;

            //循环创建100个小任务，并使用fork()提交
            for (int i = 0; i < 100; i++){

                long lastOne = pos + step;
                System.out.println("start:" + pos + ", end:" + lastOne);
                if(lastOne > end){
                    lastOne = end;
                }

                //创建分出来的小任务
                LockResouce lockResouce = new LockResouce(pos, lastOne);
                taskList.add(lockResouce);
                //调用fork()提交小任务
                lockResouce.fork();

                pos = pos + step + 1;
            }

            //100个小任务运行完毕，使用join()合并100个小任务的结果
            for (LockResouce everyTask: taskList){
                sum = sum + everyTask.join(); //join()会一直等待任务的执行完成并返回执行结果，任务一直没结束，会一直阻塞等待！
            }
        }

        return sum;
    }
}
```



准备线程类

```java
public static void main(String[] args) throws InterruptedException, ExecutionException {

  ForkJoinPool forkJoinPool = new ForkJoinPool();
  ForkJoinTask<Long> result = forkJoinPool.submit(new LockResouce(0L, 2000000L)); //提交一个大任务到ForkJoinPool中
  System.out.println(result.get());

  long s = 0L;
  for(long i=0; i<=2000000; i++){
    s = s + i;
  }
  System.out.println(s);
}
```

