package com.takeit.order.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
//                .maxAttempts(3) // 최대 재시도 횟수 설정
//                .recoverer(new RejectAndDontRequeueRecoverer()) // 실패 시 메시지 처리 중단
//                .build());
//        return factory;
//    }
}