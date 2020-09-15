package com.sharon.dataws.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RabbitMQConfig {

    public static final String USER_DATA_REQUEST_QUEUE = "com.sharon.userdatarequests";
    public static final String API_DATA_RESPONSE_QUEUE = "com.sharon.apidataresponses";
    public static final String SITE_DATA_RESPONSE_QUEUE = "com.sharon.sitedataresponses";

    public static final String USER_DATA_REQUEST_ROUTINGKEY = "request";

    public static final String EXCHANGE = "user_data";


    @Bean(USER_DATA_REQUEST_QUEUE)
    public Queue queueRequest() {
        return new Queue(USER_DATA_REQUEST_QUEUE, false);
    }

    @Bean(API_DATA_RESPONSE_QUEUE)
    public Queue queueAPIResponse() {
        return new Queue(API_DATA_RESPONSE_QUEUE, false);
    }

    @Bean(SITE_DATA_RESPONSE_QUEUE)
    public Queue queueSiteResponse() {
        return new Queue(SITE_DATA_RESPONSE_QUEUE, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Binding binding(@Qualifier(USER_DATA_REQUEST_QUEUE) Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(USER_DATA_REQUEST_ROUTINGKEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

//    @Bean
//    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
//        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
//        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
//        simpleMessageListenerContainer.setQueues(queue());
//        simpleMessageListenerContainer.setMessageListener(new UserDataResponseListener());
//        return simpleMessageListenerContainer;
//
//    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
