package com.takeit.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.takeit.order.application.dto.user.UserDto;

@FeignClient(name = "auth-service")
public interface UserClient {

	@GetMapping("/feign/v1/auths/{username}")
	UserDto getUser(@PathVariable String username);
}
