package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * project name : xuecheng
 * Date:2019/1/2
 * Author: yc.guo
 * DESC:
 */
@Configuration
public class RabbitMqConfig {
    
    public static final  String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";
    
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }
    
    
  

}
