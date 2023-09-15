package com.czdx.parkingorder.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: tangwei
 * @Date: 2023/3/1 11:17 AM
 * @Description: 类描述信息
 */
//@Configuration
//public class ParkingRedissonConfig {
//    @Bean
//    public RedissonClient getRedissonClient(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://10.10.210.19:30002");
//        RedissonClient redisson = Redisson.create(config);
//        return redisson;
//    }
//
//}
