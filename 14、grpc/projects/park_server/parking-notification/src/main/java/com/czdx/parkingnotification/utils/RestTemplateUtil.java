package com.czdx.parkingnotification.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
