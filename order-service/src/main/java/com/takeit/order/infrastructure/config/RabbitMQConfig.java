package com.takeit.order.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.queues.product.cancel}")
    private String queueProduct;

    @Value("${message.queues.order.cancel}")
    private String queueOrder;

    @Bean
    public TopicExchange orderExchange() { return new TopicExchange(exchange); }

    @Bean
    public Queue productQueue() { return new Queue(queueProduct); }

    @Bean
    public Queue orderQueue() { return new Queue(queueOrder); }

    @Bean
    public Binding productBinding() { return BindingBuilder.bind(productQueue()).to(orderExchange()).with(queueProduct); }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}