package com.ruoyi;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.service.SiteManageService;
import com.ruoyi.project.parking.service.impl.SiteManageServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ParkingLotApplication {



    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ParkingLotApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
        RedisCache bean = SpringUtils.getBean(RedisCache.class);
        SiteManageServiceImpl bean1 = SpringUtils.getBean(SiteManageServiceImpl.class);
      //  RedisTemplate bean2 = SpringUtils.getBean(RedisTemplate.class);

        String key = CacheConstants.PARKNO_ACCOUNT_KEY + "p2022090909";
       // bean.setCacheObject(key,100);
        Integer cacheObject = bean.getCacheObject(key);
     //   bean2.opsForValue().decrement(key);

        bean1.updateCacheParkSpaceAmount("p2022090909",1);
        System.out.println(cacheObject);
        bean1.updateCacheParkSpaceAmount("p2022090909",2);


    }
}
