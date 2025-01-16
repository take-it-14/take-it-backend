package com.takeit.product.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.queues.product.cancel}")
    private String queueProductCancel;

    @Value("${message.queues.product.save.db}")
    private String queueProductSaveDb;

    @Bean public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue queueProductCancel() {
        return new Queue(queueProductCancel);
    }

    @Bean
    public Queue queueProductSaveDb() {
        return new Queue(queueProductSaveDb);
    }

    @Bean
    public Binding bindingProductCancel() {
        return BindingBuilder.bind(queueProductCancel()).to(exchange()).with(queueProductCancel);
    }

    @Bean
    public Binding bindingProductSaveDb() {
        return BindingBuilder.bind(queueProductSaveDb()).to(exchange()).with(queueProductSaveDb);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(3) // 최대 재시도 횟수 설정
                .recoverer(new RejectAndDontRequeueRecoverer()) // 실패 시 메시지 처리 중단
                .build());
        return factory;
    }
}
