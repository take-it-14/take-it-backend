package com.takeit.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.takeit.order.application.interceptor.UserCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final UserCheckInterceptor userCheckInterceptor;

	public WebConfig(UserCheckInterceptor userCheckInterceptor) {
		this.userCheckInterceptor = userCheckInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userCheckInterceptor).addPathPatterns("/api/**");
	}
}
