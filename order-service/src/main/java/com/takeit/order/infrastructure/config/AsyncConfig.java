package com.takeit.order.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(500);  // 최소 스레드 수
        executor.setMaxPoolSize(20000); // 최대 스레드 수
        executor.setQueueCapacity(40000); // 큐의 크기
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}