package com.takeit.favorite.infrastructure.client;

import com.takeit.favorite.application.dto.user.UserDto;
import com.takeit.favorite.application.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service")
public interface UserClient extends UserService {
    @GetMapping("/feign/v1/auths/{username}")
    UserDto getUser(@PathVariable String username);

    @GetMapping("/feign/v1/auths")
    List<UserDto> getUsers(@RequestParam(name = "idList") List<Long> idList);
}
