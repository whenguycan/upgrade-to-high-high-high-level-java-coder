package com.czdx.parkingnotification.system.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 *
 * description: Rabbitmq配置类
 * @author mingchenxu
 * @date 2023/2/23 15:38
 */
@Configuration
@EnableRabbit
public class RabbitmqConfig {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    MessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        // 对象字段全部列入
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 取消默认转换timestamps形式
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        // 忽略空bean转json的错误
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 统一日期格式yyyy-MM-dd HH:mm:ss
        mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT));
        mapper.registerModule(new JavaTimeModule());
        // 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(mapper);
    }

}
