package com.xuecheng.cms_client.config;

import com.xuecheng.cms_client.adapter.MessageDelegate;
import com.xuecheng.cms_client.convert.TextMessageConverter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";
    
    public static final  String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    
    @Value("${xuecheng.mq.queue}")
    private String queueName;

    @Value("${xuecheng.mq.routingKey}")
    private String routingKey;
    
    @Autowired
    private MessageDelegate messageDelegate;
    
    
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange exchange(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }
    
    
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue queue(){
        return new Queue(queueName, true);
    }
    
    @Bean
    public Binding binding(@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange  exchange,
                           @Qualifier(QUEUE_CMS_POSTPAGE) Queue  queue){
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }



    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        adapter.setMessageConverter(new TextMessageConverter());
//        container.setMessageListener(adapter);
        MessageListenerAdapter adapter = new MessageListenerAdapter(messageDelegate);
        adapter.setDefaultListenerMethod("consumeMessage");

        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        adapter.setMessageConverter(jackson2JsonMessageConverter);

        container.setMessageListener(adapter);
    
    
        return container;

    }

}
