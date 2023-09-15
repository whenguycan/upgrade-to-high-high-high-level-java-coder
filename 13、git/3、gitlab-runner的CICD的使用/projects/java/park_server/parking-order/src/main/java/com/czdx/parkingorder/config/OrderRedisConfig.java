package com.czdx.parkingorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: tangwei
 * @Date: 2023/4/23 11:04 AM
 * @Description: 类描述信息
 */
//@Configuration
//public class OrderRedisConfig {

//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
//
//        RedisTemplate redisTemplate = new RedisTemplate();
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setConnectionFactory(factory);
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
//        return redisTemplate;
//
//    }
//}
