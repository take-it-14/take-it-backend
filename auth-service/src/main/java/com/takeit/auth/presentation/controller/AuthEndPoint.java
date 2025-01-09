package com.takeit.auth.presentation.controller;

import com.takeit.auth.application.dto.UserAuthResponse;
import com.takeit.auth.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/auths")
public class AuthEndPoint {
    private final AuthService authService;

    @GetMapping("/{username}")
    public UserAuthResponse getUser(@PathVariable String username) {
        return authService.getUserByUsername(username);
    }

}
