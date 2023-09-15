package com.example.agentspringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: tangwei
 * @Date: 2023/5/6 12:33 PM
 * @Description: 类描述信息
 */
@RestController
public class HelloWorldController {

    @GetMapping("/index")
    public void index() throws InterruptedException {

        //打印jvm的进程id号
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(nameOfRunningVM);


        int count = 600;
        for (int i = 0; i < count; i++) {

            String info = String.format("|%03d| %s remains %03d seconds", i, nameOfRunningVM, (count - i));
            System.out.println(info);

            Random random = new Random(System.currentTimeMillis());
            int a = random.nextInt(10);
            int b = random.nextInt(10);
            boolean flag = random.nextBoolean();
            String message;
            if(flag){
                message = String.format("a + b = %d", HelloWorldController.add(a, b));
            }else{
                message = String.format("a - b = %d", HelloWorldController.sub(a, b));
            }
            System.out.println(message);

            TimeUnit.SECONDS.sleep(1);

        }
    }


    public static int add(int a, int b){
        return a + b;
    }

    public static int sub(int a, int b){
        return a - b;
    }
}
