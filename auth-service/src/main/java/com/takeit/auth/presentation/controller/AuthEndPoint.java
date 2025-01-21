package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.UserAuthResponse;
import com.takeit.auth.application.service.AuthService;
import com.takeit.auth.application.service.UserService;
import com.takeit.auth.domain.entity.SellerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/auths")
public class AuthEndPoint {
    private final AuthService authService;
    private final UserService userService;


    @GetMapping("/{username}")
    public UserAuthResponse getUser(@PathVariable String username) {
        return authService.getUserByUsername(username);
    }

    @GetMapping
    public List<UserAuthResponse> getUsers(@RequestParam(required = false) List<Long> idList) {
        return userService.getUsers(idList);
    }

    @GetMapping("/seller/{userId}")
    public SellerInfo getSellerInfo(@PathVariable Long userId) {
        return userService.getSellerInfoByUserId(userId);
    }

}
