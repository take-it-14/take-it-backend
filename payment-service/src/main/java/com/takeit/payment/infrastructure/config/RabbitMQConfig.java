package com.takeit.payment.infrastructure.config;

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

    @Value("${message.queues.order.cancel}")
    private String queueOrder;

    @Bean
    public TopicExchange paymentExchange() { return new TopicExchange(exchange); }

    @Bean
    public Queue orderQueue() { return new Queue(queueOrder); }

    @Bean
    public Binding orderBinding() { return BindingBuilder.bind(orderQueue()).to(paymentExchange()).with(queueOrder); }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
