package com.xuecheng.cms_client.mq;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xuecheng.cms_client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * project name : xuecheng
 * Date:2019/1/2
 * Author: yc.guo
 * DESC:
 */
@Component
public class ConsumerPostPage {
    
    
    @Autowired
    private PageService pageService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    
//    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
//    public void postPage(String msg) throws IOException {
//        Map<String,Object> map = objectMapper.readValue(msg, Map.class);
//        String pageId = (String) map.get("pageId");
//        pageService.savePageToServerPath(pageId);
//
//    }


//    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
//    @RabbitHandler
//    public void onOrderMessage(@Payload Map<String,Object> map,
//                               Channel channel,
//                               @Headers Map<String, Object> headers) throws Exception {
//
//        String pageId = (String) map.get("pageId");
//        pageService.savePageToServerPath(pageId);
//    }


    
    
    
    
}
