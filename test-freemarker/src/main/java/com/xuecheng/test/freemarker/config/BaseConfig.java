package com.xuecheng.test.freemarker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Date:2019/2/8
 * Author:gyc
 * Desc:
 */

@Configuration
public class BaseConfig   {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
