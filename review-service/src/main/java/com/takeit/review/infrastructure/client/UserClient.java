package com.takeit.review.infrastructure.client;

import com.takeit.review.application.dto.user.UserDto;
import com.takeit.review.application.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserClient extends UserService {
    @GetMapping("/feign/v1/auths/{username}")
    UserDto getUser(@PathVariable String username);
}
